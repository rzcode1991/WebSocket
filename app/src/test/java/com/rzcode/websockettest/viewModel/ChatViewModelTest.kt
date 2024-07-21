package com.rzcode.websockettest.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rzcode.websockettest.data.model.WebSocketEvent
import com.rzcode.websockettest.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val repository: ChatRepository = mock(ChatRepository::class.java)

    @Test
    fun `testing send message`() = runTest{
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val testWebSocketEventFlow = MutableStateFlow<WebSocketEvent>(WebSocketEvent.InitialWebSocketState)
        `when`(repository.webSocketEvent).thenReturn(testWebSocketEventFlow)

        try {
            val testCase = "Hello world!"
            val viewModel = ChatViewModel(repository)

            viewModel.setStatus(true)

            viewModel.sendMessage(testCase)
            verify(repository).sendMessage(testCase)
            advanceUntilIdle()

            val result = viewModel.messages.value
            assertEquals(listOf(Pair(true, testCase)), result)
        } finally {
            reset(repository)
            Dispatchers.resetMain()
        }

    }

    /*@get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `testing send message second`() = runTest {
        val testWebSocketEventFlow = MutableStateFlow<WebSocketEvent>(WebSocketEvent.InitialWebSocketState)
        `when`(repository.webSocketEvent).thenReturn(testWebSocketEventFlow)

        val testCase = "Hello world second!"
        val viewModel = ChatViewModel(repository)

        viewModel.setStatus(true)

        viewModel.sendMessage(testCase)
        verify(repository).sendMessage(testCase)
        advanceUntilIdle()

        val result = viewModel.messages.value
        assertEquals(listOf(Pair(true, testCase)), result)
    }*/

}