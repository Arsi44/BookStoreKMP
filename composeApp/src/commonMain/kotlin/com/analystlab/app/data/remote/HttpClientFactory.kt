package com.analystlab.app.data.remote

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

object HttpClientFactory {
    
    // Для Android эмулятора используйте 10.0.2.2 вместо localhost
    // Для iOS симулятора используйте localhost
    // Для реального устройства - IP адрес вашего компьютера в локальной сети
    private const val BASE_URL = "http://10.0.2.2:8000"
    
    fun makeClient(
        enableNetworkLogs: Boolean,
        tokenProvider: (() -> String?)? = null
    ): HttpClient {
        return HttpClient {
            install(DefaultRequest) {
                url(BASE_URL)
                headers {
                    appendIfNameAbsent(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                    // Добавляем Bearer токен если он есть
                    tokenProvider?.invoke()?.let { token ->
                        appendIfNameAbsent(
                            HttpHeaders.Authorization,
                            "Bearer $token"
                        )
                    }
                }
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 2)
            }

            install(ContentNegotiation) {
                val json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    encodeDefaults = true
                }
                json(json = json)
                register(ContentType.Text.Plain, KotlinxSerializationConverter(json))
            }

            if (enableNetworkLogs) {
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.i(tag = "AnalystLab HTTP", message = message)
                        }
                    }
                }.also {
                    Napier.base(DebugAntilog())
                }
            }
        }
    }
}
