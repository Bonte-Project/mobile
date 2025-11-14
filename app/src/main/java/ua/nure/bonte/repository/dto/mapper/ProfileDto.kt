package ua.nure.bonte.repository.dto.mapper

import ua.nure.bonte.repository.db.data.entity.ProfileEntity
import ua.nure.bonte.repository.dto.ProfileDto

fun ProfileDto.toEntity() =
    ProfileEntity(
        id = id,
        email = email,
        fullName = fullName,
        avatarUrl = avatarUrl,
        role = role,
        isEmailVerified = isEmailVerified,
        height = height,
        weight = weight,
        age = age,
        createdAt = createdAt,
        isPremium = isPremium,
    )