package com.quizmakers.core.data.battle.socketio

import com.quizmakers.core.api.Server
import com.quizmakers.core.api.interceptors.BearerTokenInterceptor
import com.quizmakers.core.data.battle.utils.log
import com.quizmakers.core.domain.battle.socketio.WebSocketService
import io.socket.client.IO
import io.socket.engineio.client.transports.Polling
import okhttp3.OkHttpClient
import org.json.JSONObject
import org.koin.core.annotation.Single

@Single
class WebSocketImpl(bearerTokenInterceptor: BearerTokenInterceptor) : WebSocketService {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(bearerTokenInterceptor)
        .build()

    private val options = IO.Options().apply {
        transports = arrayOf("websocket", Polling.NAME)
        callFactory = okHttpClient
        webSocketFactory = okHttpClient
    }

    private val socket = IO.socket(Server.BASE_URL_battle, options)

    override fun connect() {
        socket.connect()
    }

    override fun disconnect() {
        socket.disconnect()
    }

    override fun eventObserver(socketEvents: SocketEvents, eventObserver: (Array<Any>) -> Unit) {
        socket.on(socketEvents.eventName) {
            eventObserver(it).run { socketEvents.log(this@WebSocketImpl.javaClass.name, it) }
        }
    }

    override fun emitEvent(socketEvents: SocketEvents, message: JSONObject) {
        socket.emit(socketEvents.eventName, message).run {
            socketEvents.log(
                this@WebSocketImpl.javaClass.name,
                arrayOf(socketEvents.eventName + " " + message)
            )
        }
    }
}