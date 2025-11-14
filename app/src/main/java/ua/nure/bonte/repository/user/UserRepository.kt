package ua.nure.bonte.repository.user

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.db.data.entity.ProfileEntity
import ua.nure.bonte.repository.dto.ProfileDataDto

interface UserRepository {
    suspend fun loadMe(): Result<ProfileDataDto, DataError>
    suspend fun patchMe(
        firstName: String,
        lastName: String,
        height: Int?,
        weight: Int?,
        age: Int?,
        avatarUrl: String?,
    ): Result<ProfileDataDto, DataError>

    fun getMe(): Flow<ProfileEntity>

}