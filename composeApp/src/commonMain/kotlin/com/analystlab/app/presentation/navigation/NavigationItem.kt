package com.analystlab.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector?
) {
    data object Login : NavigationItem("/login", "Вход", null)
    data object Dashboard : NavigationItem("/dashboard", "Главная", Icons.Rounded.Home)
    data object Modules : NavigationItem("/modules", "Модули", Icons.Filled.List)
    data object Settings : NavigationItem("/settings", "Настройки", Icons.Filled.Settings)
    data object Module : NavigationItem("/module/{id}", "Модуль", null)
    data object Material : NavigationItem("/material/{moduleId}", "Материал", null)
    data object Test : NavigationItem("/test/{moduleId}", "Тест", null)
}
