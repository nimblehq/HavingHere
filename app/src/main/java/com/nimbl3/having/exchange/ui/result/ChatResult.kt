package com.nimbl3.having.exchange.ui.result

import com.nimbl3.having.exchange.ui.mvibase.MviResult

sealed class ChatResult : MviResult {
    sealed class ChatInitialResult : MviResult {

    }

    sealed class ChatSubmitResult : MviResult {

    }
}