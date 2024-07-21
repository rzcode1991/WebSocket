package com.rzcode.websockettest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.rzcode.websockettest.ui.screen.ChatScreen
import com.rzcode.websockettest.ui.theme.theme.WebSocketTestTheme
import com.rzcode.websockettest.viewModel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setContent {
            WebSocketTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(

                    ) { paddingValues ->

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                                .padding(paddingValues)
                        ) {

                            ChatScreen()

                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chatViewModel.shutDown()
    }

}