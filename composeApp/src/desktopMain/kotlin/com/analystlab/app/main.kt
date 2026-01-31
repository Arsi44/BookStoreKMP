package com.analystlab.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.analystlab.app.di.initKoin

fun main() = application {
    initKoin(enableNetworkLogs = true)
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "AnalystLab"
    ) {
        App()
    }
}
