package com.analystlab.app.di

import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.analystlab.app.data.local.TokenStorage
import com.analystlab.app.data.remote.AuthApi
import com.analystlab.app.data.remote.HttpClientFactory
import com.analystlab.app.data.remote.ModulesApi
import com.analystlab.app.data.repository.AuthRepositoryImpl
import com.analystlab.app.data.repository.ModulesRepositoryImpl
import com.analystlab.app.domain.repository.AuthRepository
import com.analystlab.app.domain.repository.ModulesRepository
import com.analystlab.app.presentation.dashboard.DashboardViewModel
import com.analystlab.app.presentation.login.LoginViewModel
import com.analystlab.app.presentation.material.MaterialViewModel
import com.analystlab.app.presentation.module.ModuleViewModel

fun commonModule(enableNetworkLogs: Boolean) = module {
    
    // Settings for token storage
    single { Settings() }
    
    // Token storage
    single { TokenStorage(get()) }
    
    // HTTP Client
    // Важно: передаём tokenProvider, чтобы токен автоматически добавлялся
    // к каждому HTTP-запросу в заголовке Authorization
    single {
        val tokenStorage: TokenStorage = get()
        HttpClientFactory.makeClient(
            enableNetworkLogs = enableNetworkLogs,
            tokenProvider = { tokenStorage.accessToken }
        )
    }
    
    // API clients
    single { AuthApi(get()) }
    single { ModulesApi(get()) }
    
    // Repositories
    single<AuthRepository> { 
        AuthRepositoryImpl(
            authApi = get(),
            tokenStorage = get()
        ) 
    }
    
    single<ModulesRepository> { 
        ModulesRepositoryImpl(
            modulesApi = get(),
            tokenStorage = get()
        ) 
    }
    
    // ViewModels
    singleOf(::LoginViewModel)
    singleOf(::DashboardViewModel)
    singleOf(::ModuleViewModel)
    singleOf(::MaterialViewModel)
}

expect fun platformModule(): Module
