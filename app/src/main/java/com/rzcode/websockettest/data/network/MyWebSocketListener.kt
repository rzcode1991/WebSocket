package com.rzcode.websockettest.data.network

import android.util.Log
import com.rzcode.websockettest.data.model.WebSocketEvent
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyWebSocketListener @Inject constructor(

): WebSocketListener() {

    val webSocketEvent = MutableStateFlow<WebSocketEvent>(WebSocketEvent.InitialWebSocketState)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        webSocketEvent.value = WebSocketEvent.OnOpen
        webSocket.send("Android Device Connected")
        Log.e("myTag", "onOpen:")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        webSocketEvent.value = WebSocketEvent.OnMessage(text)
        Log.e("myTag", "onMessage: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        WebSocketEvent.OnClosing(code, reason)
        Log.e("myTag", "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        webSocketEvent.value = WebSocketEvent.OnClosed(code, reason)
        Log.e("myTag", "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        webSocketEvent.value = WebSocketEvent.OnFailure(t, response)
        Log.d("myTag", "onFailure: ${t.message} $response")
    }

}