package com.analystlab.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.koinInject
import com.analystlab.app.data.local.TokenStorage
import com.analystlab.app.presentation.components.BottomNavBar
import com.analystlab.app.presentation.login.LoginScreen
import com.analystlab.app.presentation.navigation.Navigation
import com.analystlab.app.presentation.navigation.NavigationItem
import com.analystlab.app.theme.AnalystLabTheme

// Глобальное состояние авторизации для доступа из любого места
object AuthState {
    var isLoggedIn = mutableStateOf(false)
    
    fun logout() {
        isLoggedIn.value = false
    }
    
    fun login() {
        isLoggedIn.value = true
    }
}

@Composable
fun App() {
    PreComposeApp {
        AnalystLabTheme {
            // Получаем TokenStorage для проверки авторизации
            val tokenStorage: TokenStorage = koinInject()
            
            // Инициализируем состояние при первом запуске
            remember {
                AuthState.isLoggedIn.value = tokenStorage.isLoggedIn()
                true
            }
            
            // Читаем состояние (подписываемся на изменения)
            val isLoggedIn by AuthState.isLoggedIn
            
            // Если пользователь НЕ авторизован - показываем экран входа
            if (!isLoggedIn) {
                LoginScreen(
                    navigator = rememberNavigator(),
                    onLoginSuccess = {
                        // После успешного входа обновляем состояние
                        AuthState.login()
                    }
                )
            } else {
                // Пользователь авторизован - показываем основное приложение
                val navigator = rememberNavigator()
                
                val topLevelDestinations = listOf(
                    NavigationItem.Dashboard,
                    NavigationItem.Modules,
                    NavigationItem.Settings
                )
                
                val currentRoute = navigator.currentEntry.collectAsState(null).value?.route?.route
                
                // Показываем bottom bar только на главных экранах
                val isTopLevelDestination = currentRoute in topLevelDestinations.map { it.route }
                
                Scaffold(
                    bottomBar = {
                        if (isTopLevelDestination) {
                            BottomNavBar(
                                bottomNavItems = topLevelDestinations,
                                navigator = navigator
                            )
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Navigation(
                            navigator = navigator,
                            // Callback вызывается после logout
                            onLogoutComplete = {
                                // Вызываем глобальный logout
                                AuthState.logout()
                            }
                        )
                    }
                }
            }
        }
    }
}
