package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDataDto(
    val message: String,
    val user: ProfileDto
)

@Serializable
data class ProfileDto(
    val id: String,
    val email: String,
    val fullName: String,
    val avatarUrl: String? = null,
    val role: String,
    val isEmailVerified: Boolean,
    val height: Int? = null,
    val weight: Int? = null,
    val age: Int? = null,
    val createdAt: String,
    val isPremium: Boolean,
)


