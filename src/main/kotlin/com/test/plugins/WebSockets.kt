package com.test.plugins

import com.fasterxml.jackson.databind.SerializationFeature
import com.test.Post
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.kotlinx.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.jackson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureWebSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    val httpClient = HttpClient()
    val jsonSerializer = Json {
        ignoreUnknownKeys = true
        isLenient = false
    }

    routing {
        webSocket("/updates") {
            try {
                while (true) {
                    val postId = (1..100).random() // jsonplaceholder provides 100 posts
                    val response: HttpResponse = httpClient.request("https://jsonplaceholder.typicode.com/posts/$postId") {
                        method = HttpMethod.Get
                    }
                    val responseBody = response.bodyAsText()
                    val post: Post? = jsonSerializer.decodeFromString(responseBody)
                    if (post != null) {
                        val postJson = Json.encodeToString(post)
                        send(postJson)
                    }
                    delay(1000) // Delay for 1 second
                }
            } catch (e: Exception) {
                println("Error sending data: ${e.message}")
            }
        }
    }
}