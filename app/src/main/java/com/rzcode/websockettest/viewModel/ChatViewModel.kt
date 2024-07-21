package com.rzcode.websockettest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rzcode.websockettest.data.model.WebSocketEvent
import com.rzcode.websockettest.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    val socketStatus = MutableStateFlow(false)

    val messages = MutableStateFlow<List<Pair<Boolean, String>>>(emptyList())

    init {
        viewModelScope.launch {
            repository.webSocketEvent.collectLatest { webSocketEvent ->
                when(webSocketEvent){
                    is WebSocketEvent.OnOpen -> {
                        setStatus(true)
                    }
                    is WebSocketEvent.OnMessage -> {
                        addMessage(Pair(false, webSocketEvent.text))
                    }
                    is WebSocketEvent.OnClosing -> {

                    }
                    is WebSocketEvent.OnClosed -> {
                        setStatus(false)
                    }
                    is WebSocketEvent.OnFailure -> {

                    }
                    is WebSocketEvent.InitialWebSocketState -> {}
                }
            }
        }
    }

    fun addMessage(message: Pair<Boolean, String>){
        viewModelScope.launch(Dispatchers.Main) {
            if (socketStatus.value){
                messages.update { currentMessages ->
                    currentMessages + message
                }
            }
        }
    }

    fun setStatus(status: Boolean){
        viewModelScope.launch(Dispatchers.Main) {
            socketStatus.emit(status)
        }
    }

    fun connect(){
        repository.connect()
    }

    fun disconnect(){
        repository.disconnect()
    }

    fun shutDown(){
        repository.shutDown()
    }

    fun sendMessage(message: String){
        repository.sendMessage(message)
        addMessage(Pair(true, message))
    }

}