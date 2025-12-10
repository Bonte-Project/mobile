package ua.nure.bonte.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.activity.ActivityRepository
import ua.nure.bonte.repository.nutrition.NutritionRepository
import ua.nure.bonte.repository.sleeplog.SleepLogRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
    private val sleepRepository: SleepLogRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(Analytics.State())
    val state = _state.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), Analytics.State()
    )

    private val _event = MutableSharedFlow<Analytics.Event>()
    val event = _event.asSharedFlow()

    init {
        observeData()
        refreshAllLogs()
    }

    private fun refreshAllLogs() {
        viewModelScope.launch {
            _state.update { it.copy(inProgress = true) }
            nutritionRepository.refreshNutritionLogs()
            sleepRepository.refreshSleepLogs()
            activityRepository.refreshActivityLogs()
            _state.update { it.copy(inProgress = false) }
        }
    }

    fun onAction(action: Analytics.Action) = viewModelScope.launch {
        when (action) {
            Analytics.Action.OnBack -> _event.emit(Analytics.Event.OnBack)
            is Analytics.Action.OnNavigate -> _event.emit(Analytics.Event.OnNavigate(action.route))
            Analytics.Action.Refresh -> refreshAllLogs()
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            nutritionRepository.getLogs().collect { logs ->
                _state.update { it.copy(nutritionLogs = logs) }
                calculateWeeklyStats()
            }
        }

        viewModelScope.launch {
            sleepRepository.getSleepLogs().collect { logs ->
                _state.update { it.copy(sleepLogs = logs) }
                calculateWeeklyStats()
            }
        }

        viewModelScope.launch {
            activityRepository.getActivityLogs().collect { logs ->
                _state.update { it.copy(activityLogs = logs) }
                calculateWeeklyStats()
            }
        }
    }

    private fun calculateWeeklyStats() {
        fun safeRound(v: Double): Int =
            if (v.isNaN() || v.isInfinite()) 0 else v.roundToInt()

        fun Iterable<Int>.safeAverage(): Double =
            if (this.none()) 0.0 else this.average()

        val currentState = _state.value
        val now = LocalDate.now()
        val weekStart = now.with(DayOfWeek.MONDAY)

        val sleepDataByDay = mutableMapOf<LocalDate, Int>()
        currentState.sleepLogs.forEach { log ->
            try {
                val startDate = OffsetDateTime.parse(log.startTime).toLocalDate()
                if (!startDate.isBefore(weekStart) && !startDate.isAfter(now)) {
                    val start = OffsetDateTime.parse(log.startTime)
                    val end = OffsetDateTime.parse(log.endTime)
                    val minutes = ChronoUnit.MINUTES.between(start, end).toInt()
                    sleepDataByDay[startDate] = sleepDataByDay.getOrDefault(startDate, 0) + minutes
                }
            } catch (_: Exception) {}
        }

        val weekSleepData = (0..6).map { dayOffset ->
            val date = weekStart.plusDays(dayOffset.toLong())
            val minutes = sleepDataByDay[date] ?: 0
            val hours = minutes / 60
            val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            Triple(hours, dayName, date.atStartOfDay())
        }

        val activityDataByDay = mutableMapOf<LocalDate, Int>()
        currentState.activityLogs.forEach { log ->
            try {
                val completedDate = OffsetDateTime.parse(log.completedAt).toLocalDate()
                if (!completedDate.isBefore(weekStart) && !completedDate.isAfter(now)) {
                    activityDataByDay[completedDate] =
                        activityDataByDay.getOrDefault(completedDate, 0) + log.durationMinutes
                }
            } catch (_: Exception) {}
        }

        val weekActivityData = (0..6).map { dayOffset ->
            val date = weekStart.plusDays(dayOffset.toLong())
            val minutes = activityDataByDay[date] ?: 0
            val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            Triple(minutes / 10, dayName, date.atStartOfDay())
        }

        val nutritionDataByDay = mutableMapOf<LocalDate, Int>()
        currentState.nutritionLogs.forEach { log ->
            try {
                val eatenDate = OffsetDateTime.parse(log.eatenAt).toLocalDate()
                if (!eatenDate.isBefore(weekStart) && !eatenDate.isAfter(now)) {
                    nutritionDataByDay[eatenDate] =
                        nutritionDataByDay.getOrDefault(eatenDate, 0) + log.calories
                }
            } catch (_: Exception) {}
        }

        val weekNutritionData = (0..6).map { dayOffset ->
            val date = weekStart.plusDays(dayOffset.toLong())
            val calories = nutritionDataByDay[date] ?: 0
            val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            Triple(calories, dayName, date.atStartOfDay())
        }

        val thisWeekSleep = sleepDataByDay.values.safeAverage()
        val thisWeekActivity = safeRound(activityDataByDay.values.safeAverage())
        val thisWeekCalories = nutritionDataByDay.values.safeAverage()

        val prevWeekStart = weekStart.minusWeeks(1)
        val prevWeekSleepData = mutableMapOf<LocalDate, Int>()
        val prevWeekActivityData = mutableMapOf<LocalDate, Int>()
        val prevWeekNutritionData = mutableMapOf<LocalDate, Int>()

        currentState.sleepLogs.forEach { log ->
            try {
                val startDate = OffsetDateTime.parse(log.startTime).toLocalDate()
                if (!startDate.isBefore(prevWeekStart) && startDate.isBefore(weekStart)) {
                    val start = OffsetDateTime.parse(log.startTime)
                    val end = OffsetDateTime.parse(log.endTime)
                    val minutes = ChronoUnit.MINUTES.between(start, end).toInt()
                    prevWeekSleepData[startDate] =
                        prevWeekSleepData.getOrDefault(startDate, 0) + minutes
                }
            } catch (_: Exception) {}
        }

        currentState.activityLogs.forEach { log ->
            try {
                val completedDate = OffsetDateTime.parse(log.completedAt).toLocalDate()
                if (!completedDate.isBefore(prevWeekStart) && completedDate.isBefore(weekStart)) {
                    prevWeekActivityData[completedDate] =
                        prevWeekActivityData.getOrDefault(completedDate, 0) + log.durationMinutes
                }
            } catch (_: Exception) {}
        }

        currentState.nutritionLogs.forEach { log ->
            try {
                val eatenDate = OffsetDateTime.parse(log.eatenAt).toLocalDate()
                if (!eatenDate.isBefore(prevWeekStart) && eatenDate.isBefore(weekStart)) {
                    prevWeekNutritionData[eatenDate] =
                        prevWeekNutritionData.getOrDefault(eatenDate, 0) + log.calories
                }
            } catch (_: Exception) {}
        }

        val prevWeekSleep = prevWeekSleepData.values.safeAverage()
        val prevWeekActivity = prevWeekActivityData.values.safeAverage()
        val prevWeekCalories = prevWeekNutritionData.values.safeAverage()

        val sleepChange = if (prevWeekSleep > 0)
            safeRound(((thisWeekSleep - prevWeekSleep) / prevWeekSleep) * 100)
        else 0

        val activityChange = if (prevWeekActivity > 0)
            safeRound(((thisWeekActivity - prevWeekActivity) / prevWeekActivity) * 100)
        else 0

        val caloriesChange = if (prevWeekCalories > 0)
            safeRound(((thisWeekCalories - prevWeekCalories) / prevWeekCalories) * 100)
        else 0

        _state.update {
            it.copy(
                weekSleepData = weekSleepData,
                weekActivityData = weekActivityData,
                weekNutritionData = weekNutritionData,
                averageSleepTime = thisWeekSleep / 60.0,
                sleepChangePercent = sleepChange,
                averageActivityTime = thisWeekActivity,
                activityChangePercent = activityChange,
                averageCalories = thisWeekCalories.roundToInt(),
                caloriesChangePercent = caloriesChange
            )
        }
    }

}