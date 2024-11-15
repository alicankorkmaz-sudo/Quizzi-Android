package com.alicankorkmaz.quizzi.ui.screen.rooms

import com.alicankorkmaz.quizzi.domain.model.GameRoom

data class RoomsUiState(
    val rooms: List<GameRoom> = emptyList(),
    val isConnected: Boolean = false,
    val error: String? = null
)
