package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatPartnerListResponse(
    val message: String? = null,
    val data: List<String> = emptyList()
)
