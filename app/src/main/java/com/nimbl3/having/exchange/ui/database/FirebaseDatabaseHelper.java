package com.nimbl3.having.exchange.ui.database;


import android.content.Context;
import android.content.SharedPreferences;

import com.nimbl3.having.exchange.R;

/**
 * As a singleton object access database helper
 * Can read and write in records: users, chats, demands
 */
public class FirebaseDatabaseHelper {
    private static FirebaseDatabaseHelper mInstance;

    private FirebaseDatabaseHelper() {
    }

    public static FirebaseDatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FirebaseDatabaseHelper();
        }
        return mInstance;
    }

    // todo fetch user id from firebase instead user input
    public int getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("id", "0");
        if (!user.equals("0")) {
            return 1;
        }
        return 0;
    }
}
