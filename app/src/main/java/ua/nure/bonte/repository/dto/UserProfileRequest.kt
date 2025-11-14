package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileRequest(
    val fullName: String? = null,
    val avatarUrl: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val age: Int? = null,
)
