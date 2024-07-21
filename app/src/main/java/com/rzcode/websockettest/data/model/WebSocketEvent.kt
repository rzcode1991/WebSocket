package com.rzcode.websockettest.data.model

import okhttp3.Response

sealed class WebSocketEvent {

    data object InitialWebSocketState: WebSocketEvent()

    data object OnOpen: WebSocketEvent()

    data class OnMessage(val text: String): WebSocketEvent()

    data class OnClosing(val code: Int, val reason: String): WebSocketEvent()

    data class OnClosed(val code: Int, val reason: String): WebSocketEvent()

    data class OnFailure(val t: Throwable, val response: Response?): WebSocketEvent()

}