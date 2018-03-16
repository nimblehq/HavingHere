package com.nimbl3.having.exchange.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.nimbl3.having.exchange.R;
import com.nimbl3.having.exchange.ui.model.ChatMessage;

/**
 * Created by thuypham on 3/15/2018 AD.
 */

public class ActivityChat extends ActivityBase {

    private EditText mChatEdt;
    private Button mBtnSubmit;
    private FirebaseListAdapter<ChatMessage> adapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatEdt = (EditText) findViewById(R.id.edt_chat);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChatEdt.getText().toString().trim().length() > 0) {
                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    String user = getUser();
                    FirebaseDatabase.getInstance()
                        .getReference("chats")
                        .push()
                        .setValue(new ChatMessage(mChatEdt.getText().toString(), "user_name" + user));

                    // Clear the input
                    mChatEdt.setText("");
                }
            }
        });

        ListView listOfMessages = (ListView) findViewById(R.id.list_chat);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
            R.layout.message, FirebaseDatabase.getInstance().getReference("chats")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                    model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    private String getUser() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", "0");
    }
}
