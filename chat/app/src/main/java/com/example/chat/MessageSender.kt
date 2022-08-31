package com.example.chat

import android.telecom.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageSender {
    @POST("webhook")
    fun messageSender(@Body userMessage: messageClass):retrofit2.Call<ArrayList<BotResponse>>
}