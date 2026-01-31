package com.analystlab.app.domain.repository

import com.analystlab.app.domain.model.LoginResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun refreshToken(): Result<LoginResponse>
    fun isLoggedIn(): Boolean
    fun logout()
    fun getAccessToken(): String?
    fun saveCredentials(accessToken: String, refreshToken: String, rememberMe: Boolean, username: String)
}
