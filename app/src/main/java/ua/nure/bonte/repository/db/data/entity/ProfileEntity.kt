package ua.nure.bonte.repository.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileEntity(
    @PrimaryKey val id: String,
    val email: String? = null,
    val fullName: String? = null,
    val avatarUrl: String? = null,
    val role: String,
    val isEmailVerified: Boolean,
    val height: Int? = null,
    val weight: Int? = null,
    val age: Int? = null,
    val createdAt: String,
    val isPremium: Boolean,
) {
    companion object {
        val profilePreview = ProfileEntity(
            id = "8bef67ec-cdc6-4434-a4e7-fb03b763317a",
            email = "john.dow@gmail.com",
            fullName = "John Dow",
            avatarUrl = "",
            role = "user",
            isEmailVerified = true,
            height = 178,
            weight = 61,
            age = 17,
            createdAt = "",
            isPremium = false
        )
    }
}