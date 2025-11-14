package ua.nure.bonte.ui.profile.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.ui.profile.dashboard.Dashboard.Event.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val TAG by lazy { DashboardViewModel::class.simpleName }
    private val _state = MutableStateFlow(Dashboard.State())
    val state = _state.onStart {
        loadMe()
        observeMe()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Dashboard.State()
    )

    private var loadMeJob: Job? = null

    private val _event = MutableSharedFlow<Dashboard.Event>()
    val event = _event.asSharedFlow()


    fun onAction(action: Dashboard.Action) = viewModelScope.launch {
        when (action) {
            Dashboard.Action.OnBack -> {
                _event.emit(OnBack)
            }

            is Dashboard.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }

        }
    }

    private fun loadMe() {
        loadMeJob?.cancel()
        loadMeJob = viewModelScope.launch {
            userRepository.loadMe()
                .onSuccess {

                }.onError {

                }
        }
    }

    private fun observeMe() = viewModelScope.launch {
        userRepository.getMe().collect { profile ->
            _state.update { s ->
                s.copy(
                    profile = profile
                )
            }
        }
    }
}

