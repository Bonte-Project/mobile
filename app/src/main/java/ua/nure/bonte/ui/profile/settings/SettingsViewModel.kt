package ua.nure.bonte.ui.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.ui.auth.register.Register
import ua.nure.bonte.ui.profile.settings.Settings
import ua.nure.bonte.ui.profile.settings.Settings.Event.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val TAG by lazy { SettingsViewModel::class.simpleName }
    private val _state = MutableStateFlow(Settings.State())
    val state = _state.onStart {
        observeMe()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Settings.State()

    )

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
                    profile = state.value.profile?.copy(
                        age = action.age
                    )
                )
            }

            is Settings.Action.OnWeightChange -> _state.update { s ->
                s.copy(
                    profile = state.value.profile?.copy(
                        weight = action.weight
                    )
                )
            }

            is Settings.Action.OnHeightChange -> _state.update { s ->
                s.copy(
                    profile = state.value.profile?.copy(
                        height = action.height
                    )
                )
            }

            is Settings.Action.OnAvatarChange -> _state.update { s ->
                s.copy(
                    profile = state.value.profile?.copy(
                        avatarUrl = action.avatarUrl
                    )
                )
            }

            is Settings.Action.OnFirstNameChange -> _state.update { s ->
                val (first: String?, last: String?) = state.value.profile
                    ?.fullName?.split(" ")
                    ?.let {
                        it.getOrNull(0) to it.getOrNull(1)
                    } ?: (null to null)

                s.copy(
                    profile = state.value.profile?.copy(
                        fullName = "${action.firstName} $last"
                    )
                )
            }

            is Settings.Action.OnLastNameChange -> _state.update { s ->
                val (first: String?, last: String?) = state.value.profile
                    ?.fullName?.split(" ")
                    ?.let {
                        it.getOrNull(0) to it.getOrNull(1)
                    } ?: (null to null)

                s.copy(
                    profile = state.value.profile?.copy(
                        fullName = "$first ${action.lastName}"
                    )
                )
            }

            is Settings.Action.OnEmailChange -> _state.update { s ->
                s.copy(
                    profile = state.value.profile?.copy(
                        email = action.email
                    )
                )
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