package com.nimbl3.having.exchange.ui.intents

import android.view.View
import com.nimbl3.having.exchange.ui.model.ChatMessage
import com.nimbl3.having.exchange.ui.mvibase.MviIntent

interface ChatIntents : MviIntent {
    class InitialIntents : ChatIntents

    class SubmitChatIntent(val message: String, val user: String) : ChatIntents

    class NewChatComingIntent(val view: View, val model: ChatMessage, val user: String) : ChatIntents
}