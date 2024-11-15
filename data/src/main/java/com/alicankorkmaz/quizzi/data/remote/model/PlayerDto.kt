package com.alicankorkmaz.quizzi.data.remote.model

import com.alicankorkmaz.quizzi.domain.model.Player
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(
    val id: String,
    val name: String,
    val avatarUrl: String
) {
    fun toDomain() = Player(
        id = id,
        name = name,
        avatarUrl = avatarUrl
    )
}