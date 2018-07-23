package com.soywiz.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat


/**
 *
 * テスト用Curl
 *
curl -X POST \
http://172.16.148.162:7070/ \
-H 'Content-Type: application/json' \
-d '{"id":1,"name":"myname"}'
 *
 */
class KtorFetchService : Service() {
    data class Item(val id: Int, val name: String)

    override fun onBind(intent: Intent?): IBinder {
        Log.d(this.javaClass.simpleName, "onBind")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val HTTP_PORT = 7070
    override fun onCreate() {
        Log.d(this.javaClass.simpleName, "onCreate")
        embeddedServer(Netty, HTTP_PORT) {
//            install(ContentNegotiation) {
//                jackson {
//                    configure(SerializationFeature.INDENT_OUTPUT, true)
//                }
//            }
            install(ContentNegotiation) {
                gson {
                    setDateFormat(DateFormat.LONG)
                    setPrettyPrinting()
                }
            }
            install(DefaultHeaders)
            install(CallLogging)
            routing {
                get("/") {
                    call.respond(Item(1, "Hoge"))
                }
                post("/") {
                    val item = call.receive<Item>()
                    Log.d(javaClass.simpleName,"id:${item.id} name:${item.name}")
                    call.respond(item)
                }
            }
        }.start(wait = false)
        super.onCreate()
    }
}