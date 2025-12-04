package ua.nure.bonte.repository.user

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.repository.dto.ProfileDataDto

interface UserRepository {
    suspend fun loadMe(): Result<ProfileDataDto, DataError>
    suspend fun patchMe(
        firstName: String? = null,
        lastName: String? = null,
        height: Int? = null,
        weight: Int? = null,
        age: Int? = null,
        avatarUrl: String? = null,
    ): Result<ProfileDataDto, DataError>

    fun getMe(): Flow<Profile>

    suspend fun getUserById(id: String): Result<ProfileDataDto, DataError>

}