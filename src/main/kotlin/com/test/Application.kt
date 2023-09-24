package com.test

import com.test.plugins.*
import io.ktor.client.*
import io.ktor.client.plugins.kotlinx.serializer.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureWebSockets()
    configureSerialization()
    configureRouting()
}
