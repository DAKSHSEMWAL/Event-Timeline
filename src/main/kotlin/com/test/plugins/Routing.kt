package com.test.plugins

import TimelineItem
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay

fun Application.configureRouting() {
    routing {
        get("/timeline") {
            val timeline = listOf(
                TimelineItem(
                    type = "birthday",
                    title = "John's Birthday",
                    date = "2023-01-01",
                    description = "John turns 30 today!"
                ),
                TimelineItem(
                    type = "release",
                    title = "Version 2.0 Released",
                    date = "2023-02-01",
                    description = "We've released version 2.0 with exciting new features."
                ),
                TimelineItem(
                    type = "event",
                    title = "Annual Meetup",
                    date = "2023-03-01",
                    description = "Join us for our annual meetup at Central Park."
                ),
                TimelineItem(
                    type = "birthday",
                    title = "Shruti Khare",
                    date = "2023-07-18",
                    description = "Join us for our annual meetup at Central Park."
                )
            )
            call.respond(timeline)
        }
        webSocket("/updates") {
            try {
                var counter = 0
                while (true) {
                    val uniqueData = "Data: ${counter++}"
                    send(uniqueData)
                    delay(1) // Delay for 1 millisecond
                }
            } catch (e: Exception) {
                println("Error sending data: ${e.message}")
            }
        }
    }
}
