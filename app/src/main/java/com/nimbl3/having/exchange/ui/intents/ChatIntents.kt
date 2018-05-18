package com.nimbl3.having.exchange.ui.intents

import com.nimbl3.having.exchange.ui.mvibase.MviIntent

interface ChatIntents : MviIntent {
    class InitialIntents : ChatIntents {

    }

    class SubmitChatIntent(val message: String, val user: String) : ChatIntents {
    }

    class NewChatComingIntent : ChatIntents {

    }
}