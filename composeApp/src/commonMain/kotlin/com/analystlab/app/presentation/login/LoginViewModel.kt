package com.analystlab.app.presentation.login

import com.analystlab.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    
    // Сбрасываем состояние (вызывается при показе LoginScreen после logout)
    fun resetState() {
        _state.update { 
            LoginState(
                // Сохраняем username если был "запомнить меня"
                username = if (it.rememberMe) it.username else "",
                rememberMe = it.rememberMe,
                isLoggedIn = false  // Явно устанавливаем false
            )
        }
    }
    
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UsernameChanged -> {
                _state.update { it.copy(username = event.username, error = null) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, error = null) }
            }
            is LoginEvent.RememberMeChanged -> {
                _state.update { it.copy(rememberMe = event.rememberMe) }
            }
            is LoginEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(passwordVisible = !it.passwordVisible) }
            }
            is LoginEvent.Login -> login()
            is LoginEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }
    
    private fun login() {
        val currentState = _state.value
        
        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _state.update { it.copy(error = "Заполните все поля") }
            return
        }
        
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            authRepository.login(currentState.username, currentState.password)
                .onSuccess { response ->
                    authRepository.saveCredentials(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken,
                        rememberMe = currentState.rememberMe,
                        username = currentState.username
                    )
                    _state.update { it.copy(isLoading = false, isLoggedIn = true) }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            error = "Неверный логин или пароль"
                        ) 
                    }
                }
        }
    }
}
