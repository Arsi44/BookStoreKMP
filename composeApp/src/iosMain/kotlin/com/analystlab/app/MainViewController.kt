package com.analystlab.app

import androidx.compose.ui.window.ComposeUIViewController
import com.analystlab.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin(enableNetworkLogs = true)
    }
) {
    App()
}
