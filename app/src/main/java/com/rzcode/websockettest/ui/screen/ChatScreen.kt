package com.rzcode.websockettest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rzcode.websockettest.viewModel.ChatViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = hiltViewModel()
) {

    var newChatText by remember {
        mutableStateOf("")
    }

    val messages by chatViewModel.messages.collectAsState()
    val status by chatViewModel.socketStatus.collectAsState()

    var statusText = ""
    statusText = if (status){
        "status is: Connected"
    }else{
        "status is: Disconnected"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(text = statusText)

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // TODO: use disposableEffect instead of buttons
                Button(
                    onClick = {
                        chatViewModel.connect()
                    }) {

                    Text(text = "connect")

                }

                Button(
                    onClick = {
                        chatViewModel.disconnect()
                    }) {

                    Text(text = "disconnect")

                }

            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(messages){message ->
                if (message.first){
                    Text(text = "You: ${message.second}")
                }else{
                    Text(text = "Other: ${message.second}")
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = newChatText,
                onValueChange = { newValue ->
                    newChatText = newValue
                },
                modifier = Modifier
                    .weight(1f)
            )

            IconButton(
                onClick = {
                    chatViewModel.sendMessage(newChatText)
                    newChatText = ""
                }) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                )
            }

        }

    }

}