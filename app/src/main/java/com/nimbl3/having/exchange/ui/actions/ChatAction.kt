package com.nimbl3.having.exchange.ui.actions

import com.nimbl3.having.exchange.ui.mvibase.MviAction

sealed class ChatAction : MviAction {
    data class Initial(val id: String) : ChatAction()

    data class SubmitChat(val id: String) : ChatAction()
}
