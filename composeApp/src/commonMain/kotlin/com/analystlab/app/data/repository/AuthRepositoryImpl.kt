package com.analystlab.app.data.repository

import com.analystlab.app.data.local.TokenStorage
import com.analystlab.app.data.remote.AuthApi
import com.analystlab.app.domain.model.LoginResponse
import com.analystlab.app.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    
    override suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = authApi.login(username, password)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun refreshToken(): Result<LoginResponse> {
        return try {
            val refreshToken = tokenStorage.refreshToken
                ?: return Result.failure(Exception("No refresh token"))
            val response = authApi.refreshToken(refreshToken)
            tokenStorage.saveTokens(response.accessToken, response.refreshToken)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun isLoggedIn(): Boolean = tokenStorage.isLoggedIn()
    
    override fun logout() {
        tokenStorage.clearTokens()
    }
    
    override fun getAccessToken(): String? = tokenStorage.accessToken
    
    override fun saveCredentials(
        accessToken: String,
        refreshToken: String,
        rememberMe: Boolean,
        username: String
    ) {
        tokenStorage.rememberMe = rememberMe
        tokenStorage.username = username
        tokenStorage.saveTokens(accessToken, refreshToken)
    }
}
