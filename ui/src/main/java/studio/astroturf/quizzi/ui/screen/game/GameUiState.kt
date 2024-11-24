package studio.astroturf.quizzi.ui.screen.game

import studio.astroturf.quizzi.domain.model.Question
import studio.astroturf.quizzi.domain.model.RoomState
import studio.astroturf.quizzi.domain.model.websocket.ServerMessage

data class GameUiState(
    val currentQuestion: Question? = null,
    val timeRemaining: Long? = null,
    val roomState: RoomState? = null,
    val error: String? = null,
    val cursorPosition: Float = 0.5f,
    val winner: String? = null,
    val lastAnswer: ServerMessage.AnswerResult? = null,
    val hasAnswered: Boolean = false,
    val playerId: String? = null,
    val playerName: String? = null,
    val showRoundResult: Boolean = false,
    val correctAnswer: Int? = null,
    val winnerPlayerName: String? = null,
    val isWinner: Boolean = false,
    val roomId: String? = null,
    val playerIdToNameMap: Map<String, String> = emptyMap(),
    val showCountdown: Boolean = false,
    val countdown: Int = 0,
    val correctAnswerText: String? = null,
)