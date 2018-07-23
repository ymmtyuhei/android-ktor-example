package com.soywiz.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class KtorFetchService : Service() {
    data class Item(val id: Int, val name: String)

    override fun onBind(intent: Intent?): IBinder {
        Log.d(this.javaClass.simpleName, "onBind")
    }

    private val HTTP_PORT = 7070
    override fun onCreate() {
        Log.d(this.javaClass.simpleName, "onCreate")
        embeddedServer(Netty, HTTP_PORT) {
            install(ContentNegotiation) {
                jackson {
                    configure(SerializationFeature.INDENT_OUTPUT, true)
                }
            }
            routing {
                get("/") {
                    call.respond(Item(1, "Hoge"))
                }
            }
        }.start(wait = false)
        super.onCreate()
    }
}