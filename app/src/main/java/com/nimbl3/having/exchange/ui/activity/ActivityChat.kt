package com.nimbl3.having.exchange.ui.activity

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
import com.nimbl3.having.exchange.R
import com.nimbl3.having.exchange.ui.model.ChatMessage

class ActivityChat : ActivityBase() {

    private var mChatEdt: EditText? = null
    private var mBtnSubmit: Button? = null
    private var adapter: FirebaseListAdapter<ChatMessage>? = null
    private var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mChatEdt = findViewById<View>(R.id.edt_chat) as EditText
        mBtnSubmit = findViewById<View>(R.id.btn_submit) as Button
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        mBtnSubmit!!.setOnClickListener {
            if (mChatEdt!!.text.toString().trim { it <= ' ' }.length > 0) {
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                val user = getUser()
                FirebaseDatabase.getInstance()
                        .getReference("chats")
                        .push()
                        .setValue(ChatMessage(mChatEdt!!.text.toString(), "user_name$user"))

                // Clear the input
                mChatEdt!!.setText("")
            }
        }

        val listOfMessages = findViewById<View>(R.id.list_chat) as ListView

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

    private fun getUser(): String {
        val sharedPreferences = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        return sharedPreferences.getString("id", "0")
    }
}
