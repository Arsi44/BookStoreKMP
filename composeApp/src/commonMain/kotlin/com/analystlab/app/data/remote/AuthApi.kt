package com.analystlab.app.data.remote

import com.analystlab.app.domain.model.LoginRequest
import com.analystlab.app.domain.model.LoginResponse
import com.analystlab.app.domain.model.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(private val httpClient: HttpClient) {
    
    suspend fun login(username: String, password: String): LoginResponse {
        return httpClient.post("/api/auth/login") {
            setBody(LoginRequest(username, password))
        }.body()
    }
    
    suspend fun refreshToken(refreshToken: String): LoginResponse {
        return httpClient.post("/api/auth/refresh") {
            setBody(RefreshTokenRequest(refreshToken))
        }.body()
    }
}
