package com.example.pragyan.network

import com.example.pragyan.viewmodel.CommandBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RobotApi {
    @POST("command")
    suspend fun sendCommand(
        @Body command: CommandBody  // Now matches server format
    ): Response<Unit>
}