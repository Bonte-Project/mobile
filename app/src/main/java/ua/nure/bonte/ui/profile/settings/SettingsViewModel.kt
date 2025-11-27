package ua.nure.bonte.ui.profile.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.config.sendEditTimeout
import ua.nure.bonte.extension.firstName
import ua.nure.bonte.extension.lastName
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.token.TokenRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.ui.auth.register.Register
import ua.nure.bonte.ui.profile.settings.Settings
import ua.nure.bonte.ui.profile.settings.Settings.Event.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(Settings.State())
    val state = _state.onStart {
        observeMe()
        observeAge()
        observeWeight()
        observeHeight()
        observeFullName()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Settings.State()

    )

    private val _event = MutableSharedFlow<Settings.Event>()
    val event = _event.asSharedFlow()

    private var updateProfileJob: Job? = null


    fun onAction(action: Settings.Action) = viewModelScope.launch {
        when (action) {
            Settings.Action.OnBack -> {
                _event.emit(OnBack)
            }

            is Settings.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }

            is Settings.Action.OnAgeChange -> {
                _state.update { s ->
                    s.copy(
                        profile = state.value.profile?.copy(
                            age = action.age
                        )
                    )
                }
            }

            is Settings.Action.OnWeightChange -> {
                _state.update { s ->
                    s.copy(
                        profile = state.value.profile?.copy(
                            weight = action.weight
                        )
                    )
                }
            }

            is Settings.Action.OnHeightChange -> {
                _state.update { s ->
                    s.copy(
                        profile = state.value.profile?.copy(
                            height = action.height
                        )
                    )
                }
            }

            is Settings.Action.OnAvatarChange -> {
                _state.update { s ->
                    s.copy(
                        profile = state.value.profile?.copy(
                            avatarUrl = action.avatarUrl
                        ),
                        showChangeAvatarDialog = false
                    )
                }
                patchProfile(
                    firstName = state.value.profile?.fullName.firstName(),
                    lastName = state.value.profile?.fullName.lastName(),
                    height = state.value.profile?.height,
                    weight = state.value.profile?.weight,
                    age = state.value.profile?.age,
                    avatarUrl = action.avatarUrl
                )
            }

            is Settings.Action.OnFirstNameChange -> {
                _state.update { s ->
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
            }

            is Settings.Action.OnLastNameChange -> {
                _state.update { s ->
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
            }

            is Settings.Action.OnEmailChange -> {
                _state.update { s ->
                    s.copy(
                        profile = state.value.profile?.copy(
                            email = action.email
                        )
                    )
                }
            }

            Settings.Action.OnSubscription -> {}

            Settings.Action.OnLogOut -> {
                tokenRepository.setToken(newToken = null)
                tokenRepository.setUserName(newUserName = null)
                _event.emit(OnNavigate(route = Screen.Auth.SignIn))
            }

            Settings.Action.OnDismissChangeAvatarDialog -> {
                _state.update { s ->
                    s.copy(
                        showChangeAvatarDialog = false
                    )
                }
            }

            Settings.Action.OnShowChangeAvatarDialog -> {
                _state.update { s ->
                    s.copy(
                        showChangeAvatarDialog = true
                    )
                }
            }
        }
    }

    private fun observeMe() = viewModelScope.launch {
        userRepository.getMe().collect { profile ->
            _state.update { s ->
                s.copy(
                    profile = profile.profileEntity
                )
            }
        }
    }

    private fun patchProfile(
        firstName: String? = null,
        lastName: String? = null,
        height: Int? = null,
        weight: Int? = null,
        age: Int? = null,
        avatarUrl: String? = null,
    ) {
        updateProfileJob?.cancel()
        updateProfileJob = viewModelScope.launch {
            userRepository.patchMe(
                firstName = firstName,
                lastName = lastName,
                height = height,
                weight = weight,
                age = age,
                avatarUrl = avatarUrl,
            )
        }

    }

    @OptIn(FlowPreview::class)
    private fun observeAge() {
        state.mapNotNull { it.profile?.age }
            .distinctUntilChanged()
            .debounce(sendEditTimeout)
            .onEach { query ->
                patchProfile(
                    firstName = state.value.profile?.fullName.firstName(),
                    lastName = state.value.profile?.fullName.lastName(),
                    height = state.value.profile?.height,
                    weight = state.value.profile?.weight,
                    age = query,
                    avatarUrl = state.value.profile?.avatarUrl
                )
            }.launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeWeight() {
        state.mapNotNull { it.profile?.weight }
            .distinctUntilChanged()
            .debounce(sendEditTimeout)
            .onEach { query ->
                patchProfile(
                    firstName = state.value.profile?.fullName.firstName(),
                    lastName = state.value.profile?.fullName.lastName(),
                    height = state.value.profile?.height,
                    weight = query,
                    age = state.value.profile?.age,
                    avatarUrl = state.value.profile?.avatarUrl
                )
            }.launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeHeight() {
        state.mapNotNull { it.profile?.height }
            .distinctUntilChanged()
            .debounce(sendEditTimeout)
            .onEach { query ->
                patchProfile(
                    firstName = state.value.profile?.fullName.firstName(),
                    lastName = state.value.profile?.fullName.lastName(),
                    height = query,
                    weight = state.value.profile?.weight,
                    age = state.value.profile?.age,
                    avatarUrl = state.value.profile?.avatarUrl
                )
            }.launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeFullName() {
        state.mapNotNull { it.profile?.fullName }
            .distinctUntilChanged()
            .debounce(sendEditTimeout)
            .onEach { query ->
                patchProfile(
                    firstName = query.firstName(),
                    lastName = query.lastName(),
                    height = state.value.profile?.height,
                    weight = state.value.profile?.weight,
                    age = state.value.profile?.age,
                    avatarUrl = state.value.profile?.avatarUrl
                )
            }.launchIn(viewModelScope)
    }

}