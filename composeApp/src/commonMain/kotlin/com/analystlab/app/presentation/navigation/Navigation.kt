package com.analystlab.app.presentation.navigation

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import org.koin.compose.koinInject
import com.analystlab.app.domain.repository.AuthRepository
import com.analystlab.app.presentation.dashboard.DashboardScreen
import com.analystlab.app.presentation.material.MaterialScreen
import com.analystlab.app.presentation.module.ModuleScreen
import com.analystlab.app.presentation.settings.SettingsScreen

@Composable
fun Navigation(
    navigator: Navigator,
    authRepository: AuthRepository = koinInject(),
    // Callback который вызывается ПОСЛЕ logout для показа экрана входа
    onLogoutComplete: () -> Unit = {}
) {
    // Начальный экран - всегда Dashboard (проверка авторизации в App.kt)
    NavHost(
        navigator = navigator,
        initialRoute = NavigationItem.Dashboard.route
    ) {
        // Dashboard Screen
        scene(NavigationItem.Dashboard.route) {
            DashboardScreen(
                navigator = navigator,
                onModuleClick = { module ->
                    navigator.navigate("/module/${module.id}")
                }
            )
        }
        
        // Modules List Screen (same as Dashboard for now)
        scene(NavigationItem.Modules.route) {
            DashboardScreen(
                navigator = navigator,
                onModuleClick = { module ->
                    navigator.navigate("/module/${module.id}")
                }
            )
        }
        
        // Module Detail Screen
        scene(NavigationItem.Module.route) { backStackEntry ->
            backStackEntry.path<String>("id")?.let { moduleId ->
                ModuleScreen(
                    navigator = navigator,
                    moduleId = moduleId,
                    onMaterialClick = { id ->
                        navigator.navigate("/material/$id")
                    },
                    onTestClick = { id ->
                        // Тест пока не реализован
                    }
                )
            }
        }
        
        // Material Screen
        scene(NavigationItem.Material.route) { backStackEntry ->
            backStackEntry.path<String>("moduleId")?.let { moduleId ->
                MaterialScreen(
                    navigator = navigator,
                    moduleId = moduleId,
                    onBack = { navigator.goBack() }
                )
            }
        }
        
        // Settings Screen
        scene(NavigationItem.Settings.route) {
            SettingsScreen(
                navigator = navigator,
                onLogout = {
                    // 1. Очищаем токены (выход из аккаунта)
                    authRepository.logout()
                    
                    // 2. Вызываем callback который переключит UI на LoginScreen
                    onLogoutComplete()
                }
            )
        }
    }
}
