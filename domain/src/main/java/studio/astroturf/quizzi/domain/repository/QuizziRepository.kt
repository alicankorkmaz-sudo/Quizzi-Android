package studio.astroturf.quizzi.domain.repository

import kotlinx.coroutines.flow.Flow
import studio.astroturf.quizzi.domain.model.GameRoom
import studio.astroturf.quizzi.domain.model.Player
import studio.astroturf.quizzi.domain.model.websocket.ClientMessage
import studio.astroturf.quizzi.domain.model.websocket.ServerMessage
import studio.astroturf.quizzi.domain.result.QuizziResult

interface QuizziRepository {
    suspend fun login(playerId: String): QuizziResult<Player>

    suspend fun createPlayer(
        name: String,
        avatarUrl: String,
    ): QuizziResult<Player>

    fun getCurrentPlayerId(): String?

    suspend fun getRooms(): QuizziResult<List<GameRoom>>

    fun connect(): QuizziResult<Unit>

    fun observeMessages(): Flow<ServerMessage>

    fun sendMessage(message: ClientMessage)

    fun disconnect()
}
