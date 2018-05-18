package com.nimbl3.having.exchange.ui.viewstate.chat

import com.nimbl3.having.exchange.ui.mvibase.MviViewState

abstract class ChatViewState : MviViewState {
    companion object {
        fun idle(): ChatEmptyViewState {
            return ChatEmptyViewState()
        }
    }
}