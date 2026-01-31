package com.analystlab.app.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class TokenStorage(private val settings: Settings) {
    
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_USERNAME = "username"
    }
    
    var accessToken: String?
        get() = settings[KEY_ACCESS_TOKEN]
        set(value) { settings[KEY_ACCESS_TOKEN] = value }
    
    var refreshToken: String?
        get() = settings[KEY_REFRESH_TOKEN]
        set(value) { settings[KEY_REFRESH_TOKEN] = value }
    
    var rememberMe: Boolean
        get() = settings[KEY_REMEMBER_ME, false]
        set(value) { settings[KEY_REMEMBER_ME] = value }
    
    var username: String?
        get() = settings[KEY_USERNAME]
        set(value) { settings[KEY_USERNAME] = value }
    
    fun isLoggedIn(): Boolean = accessToken != null
    
    fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        if (!rememberMe) {
            settings.remove(KEY_USERNAME)
        }
    }
    
    fun saveTokens(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
}
