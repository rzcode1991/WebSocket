package com.rzcode.websockettest.repository

import com.rzcode.websockettest.data.model.WebSocketEvent
import com.rzcode.websockettest.data.network.MyWebSocketListener
import com.rzcode.websockettest.utils.Constants.API_KEY
import com.rzcode.websockettest.utils.Constants.CLUSTER_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val myWebSocketListener: MyWebSocketListener
) {

    private val scope = CoroutineScope(Dispatchers.Main)

    private var webSocket: WebSocket? = null

    val webSocketEvent = MutableStateFlow<WebSocketEvent>(WebSocketEvent.InitialWebSocketState)

    init {
        scope.launch {
            myWebSocketListener.webSocketEvent.collectLatest {
                webSocketEvent.emit(it)
            }
        }
    }

    fun connect(){
        webSocket = okHttpClient.newWebSocket(createRequest(), myWebSocketListener)
    }

    fun disconnect(){
        webSocket?.close(1000, "Canceled manually.")
    }

    fun shutDown(){
        okHttpClient.dispatcher.executorService.shutdown()
    }

    fun sendMessage(message: String){
        webSocket?.send(message)
    }

    private fun createRequest(): Request {
        val websocketURL = "wss://${CLUSTER_ID}.piesocket.com/v3/1?api_key=${API_KEY}"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

}