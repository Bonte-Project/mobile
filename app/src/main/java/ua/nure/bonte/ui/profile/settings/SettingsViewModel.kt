package ua.nure.bonte.ui.profile.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.ui.auth.register.Register
import ua.nure.bonte.ui.profile.settings.Settings
import ua.nure.bonte.ui.profile.settings.Settings.Event.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val TAG by lazy { SettingsViewModel::class.simpleName }
    private val _state = MutableStateFlow(Settings.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Settings.Event>()
    val event = _event.asSharedFlow()


    fun onAction(action: Settings.Action) = viewModelScope.launch {
        when (action) {
            Settings.Action.OnBack -> {
                _event.emit(OnBack)
            }

            is Settings.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }

            is Settings.Action.OnAgeChange -> _state.update { s ->
                s.copy(
                    age = action.age
                )
            }

            is Settings.Action.OnWeightChange -> _state.update { s ->
                s.copy(
                    weight = action.weight
                )
            }

            is Settings.Action.OnHeightChange -> _state.update { s ->
                s.copy(
                    height = action.height
                )
            }

        }
    }
}