package com.alicankorkmaz.quizzi.domain.model

data class Question(
    val imageUrl: String?,
    val content: String,
    val options: List<Option>
)