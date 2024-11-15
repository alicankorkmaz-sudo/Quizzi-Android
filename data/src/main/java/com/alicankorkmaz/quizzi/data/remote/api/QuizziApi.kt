package com.alicankorkmaz.quizzi.data.remote.api

import com.alicankorkmaz.quizzi.data.remote.model.CreatePlayerRequestDto
import com.alicankorkmaz.quizzi.data.remote.model.LoginRequestDto
import com.alicankorkmaz.quizzi.data.remote.model.PlayerDto
import com.alicankorkmaz.quizzi.data.remote.model.RoomsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface QuizziApi {
    @POST("api/player/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): PlayerDto

    @POST("api/player/create")
    suspend fun createPlayer(
        @Body request: CreatePlayerRequestDto
    ): PlayerDto

    @GET("api/room/all")
    suspend fun getRooms(): RoomsDto
}