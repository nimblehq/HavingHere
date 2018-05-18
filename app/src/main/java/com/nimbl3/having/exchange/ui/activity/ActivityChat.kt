package com.nimbl3.having.exchange.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.view.RxView
import com.nimbl3.having.exchange.R
import com.nimbl3.having.exchange.ui.intents.ChatIntents
import com.nimbl3.having.exchange.ui.model.ChatMessage
import com.nimbl3.having.exchange.ui.viewmodel.ChatViewModel
import com.nimbl3.having.exchange.ui.viewstate.chat.ChatClearInputTextViewState
import com.nimbl3.having.exchange.ui.viewstate.chat.ChatEmptyViewState
import com.nimbl3.having.exchange.ui.viewstate.chat.ChatViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class ActivityChat : ActivityBase() {

    lateinit var edtChat: EditText
    lateinit var btnSubmit: Button
    private var adapter: FirebaseListAdapter<ChatMessage>? = null
    private var mToolbar: Toolbar? = null
    lateinit var mViewModel: ChatViewModel
    lateinit var listOfMessages: ListView
    lateinit var emptyView: View
    lateinit var mDisposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        edtChat = findViewById<View>(R.id.edt_chat) as EditText
        btnSubmit = findViewById<View>(R.id.btn_submit) as Button
        mToolbar = findViewById(R.id.toolbar)
        emptyView = findViewById(R.id.empty_view)

        setSupportActionBar(mToolbar)

        mViewModel = ViewModelProviders.of(this).get<ChatViewModel>(ChatViewModel::class.java);
        mDisposables = CompositeDisposable()
        bindToViewModel()

        listOfMessages = findViewById<View>(R.id.list_chat) as ListView

        adapter = object : FirebaseListAdapter<ChatMessage>(this, ChatMessage::class.java,
                R.layout.message, FirebaseDatabase.getInstance().getReference("chats")) {
            override fun populateView(v: View, model: ChatMessage, position: Int) {
                // Get references to the views of message.xml
                val messageText = v.findViewById<TextView>(R.id.message_text)
                val messageUser = v.findViewById<TextView>(R.id.message_user)
                val messageTime = v.findViewById<TextView>(R.id.message_time)

                // Set their text
                messageText.text = model.messageText
                messageUser.text = model.messageUser

                // Format the date before showing it
                messageTime.text = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.messageTime)
            }
        }

        listOfMessages.adapter = adapter
    }

    private fun bindToViewModel() {
        // Subscribe to the ViewModel and call render for every emitted state
        mDisposables.add(mViewModel.states().subscribe({ this.render(it) }))
        // Pass the UI's intents to the ViewModel
        mViewModel.processIntents(intents())
    }

    private fun render(it: ChatViewState?) {
        when (it) {
            is ChatClearInputTextViewState -> {
                renderClearInputTextState()
            }
            is ChatEmptyViewState -> {
                renderEmptyViewState()
            }
        }
    }

    private fun renderEmptyViewState() {
        emptyView.visibility = View.VISIBLE
    }

    private fun renderClearInputTextState() {
        edtChat.setText("")
    }

    private fun initialIntent(): Observable<ChatIntents.InitialIntents> {
        return Observable.just(ChatIntents.InitialIntents())
    }

    private fun submitChatIntent(): Observable<ChatIntents.SubmitChatIntent> {
        return RxView
                .clicks(btnSubmit)
                .map { _ -> ChatIntents.SubmitChatIntent(edtChat.text.toString().trim(), getUser()) }
    }

    private fun intents(): Observable<ChatIntents> {
        return Observable.merge(initialIntent(), submitChatIntent())
    }

    private fun getUser(): String {
        val sharedPreferences = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        return sharedPreferences.getString("id", "0")
    }
}
