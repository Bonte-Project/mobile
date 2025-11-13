package ua.nure.bonte.ui.profile.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.ui.profile.dashboard.Dashboard.Event.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val TAG by lazy { DashboardViewModel::class.simpleName }
    private val _state = MutableStateFlow(Dashboard.State())
    val state = _state.asStateFlow()

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
}

