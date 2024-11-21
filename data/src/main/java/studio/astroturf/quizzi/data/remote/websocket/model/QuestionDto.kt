package studio.astroturf.quizzi.data.remote.websocket.model

import kotlinx.serialization.Serializable
import studio.astroturf.quizzi.domain.model.Question

@Serializable
data class QuestionDto(
    val categoryId: Int,
    val imageUrl: String?,
    val content: String,
    val options: List<OptionDto>
) {
    fun toDomain() = Question(
        categoryId = categoryId,
        imageUrl = imageUrl,
        content = content,
        options = options.map { it.toDomain() }
    )
}