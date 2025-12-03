package ua.nure.bonte.db.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

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
    val isOwned: Boolean = false,
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

data class Profile(
    @Embedded val profileEntity: ProfileEntity,
    @Relation(
        entity = TrainerEntity::class,
        parentColumn = "id",
        entityColumn = "userId",
    ) val trainer: Trainer?
)