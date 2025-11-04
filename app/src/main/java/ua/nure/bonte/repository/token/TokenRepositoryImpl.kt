package ua.nure.bonte.repository.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.nure.bonte.config.PreferencesKeys

class TokenRepositoryImpl (
    private val dataStore: DataStore<Preferences>
) : TokenRepository {
    override val token
        get() = _token

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _token = dataStore.data.map {
                it[PreferencesKeys.token]
            }.firstOrNull()
        }
    }

    override suspend fun setToken(newToken: String?): Unit = withContext(Dispatchers.IO) {
        _token = newToken
        if (newToken == null) {
            dataStore.edit {
                it.remove(PreferencesKeys.token)
            }
        } else {
            dataStore.edit {
                it[PreferencesKeys.token] = newToken
            }
        }
    }

    companion object {
        private var _token: String? = null
    }
}