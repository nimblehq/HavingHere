package com.nimbl3.having.exchange.ui.viewmodel

import android.arch.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.nimbl3.having.exchange.ui.intents.ChatIntents
import com.nimbl3.having.exchange.ui.model.ChatMessage
import com.nimbl3.having.exchange.ui.mvibase.MviViewModel
import com.nimbl3.having.exchange.ui.viewstate.chat.ChatClearInputTextViewState
import com.nimbl3.having.exchange.ui.viewstate.chat.ChatEmptyViewState
import com.nimbl3.having.exchange.ui.viewstate.chat.ChatViewState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ChatViewModel : ViewModel(), MviViewModel<ChatIntents, ChatViewState> {
    val intentsSubject = PublishSubject.create<ChatIntents>()

    /**
     * Compose all components to create the stream logic
     */
    private fun compose(): Observable<ChatViewState> {
        return intentsSubject
                // Cache each state and pass it to the reducer to create a new state from
                // the previous cached one and the latest Result emitted from the action processor.
                // The Scan operator is used here for the caching.
                .scan(ChatViewState.idle(), reducer)
                // When a reducer just emits previousState, there's no reason to call render. In fact,
                // redrawing the UI in cases like this can cause jank (e.g. messing up snackbar animations
                // by showing the same snackbaFr twice in rapid succession).
                .distinctUntilChanged()
                // Emit the last one event of the stream on subscription
                // Useful when a View rebinds to the ViewModel after rotation.
                .replay(1)
                // Create the stream on creation without waiting for anyone to subscribe
                // This allows the stream to stay alive even when the UI disconnects and
                // match the stream's lifecycle to the ViewModel's one.
                .autoConnect(0)
    }

    override fun processIntents(intents: Observable<ChatIntents>) {
        intents.subscribe(intentsSubject)
    }

    override fun states() = mStatesObservable

    val reducer = { _: ChatViewState, intent: ChatIntents? ->
        if (intent == null) {
            ChatEmptyViewState()
        } else {
            when (intent) {
                is ChatIntents.InitialIntents -> {
                    ChatEmptyViewState()
                }
                is ChatIntents.SubmitChatIntent -> {
                    executeSubmitChat(intent)
                    ChatClearInputTextViewState()
                }

                else -> {
                    ChatEmptyViewState()
                }
            }
        }
    }

    private fun executeSubmitChat(intent: ChatIntents.SubmitChatIntent) {
        val message = intent.message
        val user = intent.user
        FirebaseDatabase.getInstance()
                .getReference("chats")
                .push()
                .setValue(ChatMessage(message, "user_name$user"))
    }

    val mStatesObservable: Observable<ChatViewState> = compose()
}
