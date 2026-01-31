package com.analystlab.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import com.analystlab.app.di.initKoin
import com.analystlab.app.theme.AnalystLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        initKoin(enableNetworkLogs = true) {
            androidLogger(level = Level.NONE)
            androidContext(androidContext = this@MainActivity)
        }
        
        setContent {
            AnalystLabTheme {
                App()
            }
        }
    }
}
