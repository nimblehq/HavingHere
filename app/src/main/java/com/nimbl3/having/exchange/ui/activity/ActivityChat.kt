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
import com.nimbl3.having.exchange.ui.viewstate.chat.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class ActivityChat : ActivityBase() {

    lateinit var edtChat: EditText
    lateinit var btnSubmit: Button
    var adapter: FirebaseListAdapter<ChatMessage>? = null
    var mToolbar: Toolbar? = null
    lateinit var mViewModel: ChatViewModel
    lateinit var listOfMessages: ListView
    lateinit var emptyView: View

    lateinit var mDisposables: CompositeDisposable
    lateinit var newMessageComingSubject: PublishSubject<ChatIntents.NewChatComingIntent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        edtChat = findViewById<View>(R.id.edt_chat) as EditText
        btnSubmit = findViewById<View>(R.id.btn_submit) as Button
        mToolbar = findViewById(R.id.toolbar)
        emptyView = findViewById(R.id.empty_view)
        setSupportActionBar(mToolbar)

        mDisposables = CompositeDisposable()
        newMessageComingSubject = PublishSubject.create()

        mViewModel = ViewModelProviders.of(this).get<ChatViewModel>(ChatViewModel::class.java);

        bindToViewModel()

        listOfMessages = findViewById<View>(R.id.list_chat) as ListView

        adapter = object : FirebaseListAdapter<ChatMessage>(this, ChatMessage::class.java,
                R.layout.incoming_message, FirebaseDatabase.getInstance().getReference("chats")) {
            override fun populateView(v: View, model: ChatMessage, position: Int) {
                newMessageComingSubject.onNext(ChatIntents.NewChatComingIntent(v, model, getUser()))
            }
        }

        listOfMessages.adapter = adapter
    }

//    private fun updateChatContent(v: View, model: ChatMessage) {
//        // Get references to the views of message.xml
//        val messageTextIncoming = v.findViewById<TextView>(R.id.message_text_incoming)
//        val messageUserIncoming = v.findViewById<TextView>(R.id.message_user_incoming)
//        val messageTimeOutGoing = v.findViewById<TextView>(R.id.message_time)
//        val messageTextOutGoing = v.findViewById<TextView>(R.id.message_text)
//        val messageUserOutGoing = v.findViewById<TextView>(R.id.message_user)
//        val messageTimeIncoming = v.findViewById<TextView>(R.id.message_time_incoming)
//        val incomingLayout = v.findViewById<View>(R.id.layout_incoming)
//        val outgoingLayout = v.findViewById<View>(R.id.layout_sent)
//
//        val time = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
//                model.messageTime)
//
//        if (model.messageUser.equals("user_name" + getUser())) {
//            // Outgoing
//            messageUserOutGoing.text = model.messageUser
//            messageTextOutGoing.text = model.messageText
//            messageTimeOutGoing.text = time
//            outgoingLayout.visibility = View.VISIBLE
//            incomingLayout.visibility = View.GONE
//        } else {
//            // Incoming
//            messageUserIncoming.text = model.messageUser
//            messageTextIncoming.text = model.messageText
//            incomingLayout.visibility = View.VISIBLE
//            outgoingLayout.visibility = View.GONE
//            messageTimeIncoming.text = time
//        }
//    }

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
            is ChatInComingViewState -> {
                renderNewMessageComingState(it)
            }
            is ChatOutGoingViewState -> {
                renderNewMessageOutGoing(it)
            }
        }
    }

    private fun renderNewMessageOutGoing(state: ChatOutGoingViewState) {
        emptyView.visibility = View.GONE
        val messageTimeOutGoing = state.v.findViewById<TextView>(R.id.message_time)
        val messageTextOutGoing = state.v.findViewById<TextView>(R.id.message_text)
        val messageUserOutGoing = state.v.findViewById<TextView>(R.id.message_user)
        val incomingLayout = state.v.findViewById<View>(R.id.layout_incoming)
        val outgoingLayout = state.v.findViewById<View>(R.id.layout_sent)

        val time = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                state.model.messageTime)

        messageUserOutGoing.text = state.model.messageUser
        messageTextOutGoing.text = state.model.messageText
        messageTimeOutGoing.text = time
        outgoingLayout.visibility = View.VISIBLE
        incomingLayout.visibility = View.GONE
    }

    private fun renderNewMessageComingState(state: ChatInComingViewState) {
        emptyView.visibility = View.GONE
        emptyView.visibility = View.GONE
        val messageTimeInComing = state.v.findViewById<TextView>(R.id.message_time_incoming)
        val messageTextInComing = state.v.findViewById<TextView>(R.id.message_text_incoming)
        val messageUserInComing = state.v.findViewById<TextView>(R.id.message_user_incoming)
        val incomingLayout = state.v.findViewById<View>(R.id.layout_incoming)
        val outgoingLayout = state.v.findViewById<View>(R.id.layout_sent)

        val time = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                state.model.messageTime)

        messageUserInComing.text = state.model.messageUser
        messageTextInComing.text = state.model.messageText
        messageTimeInComing.text = time
        outgoingLayout.visibility = View.GONE
        incomingLayout.visibility = View.VISIBLE
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
        return Observable.merge(initialIntent(), submitChatIntent(), newMessageComingSubject)
    }

    private fun getUser(): String {
        val sharedPreferences = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        return sharedPreferences.getString("id", "0")
    }
}
