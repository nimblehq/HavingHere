package com.nimbl3.having.exchange.ui.database;


import android.content.Context;
import android.content.SharedPreferences;

import com.nimbl3.having.exchange.R;
import com.nimbl3.having.exchange.ui.model.Demand;

/**
 * As a singleton object access database helper
 * Can read and write in records: users, chats, demands
 */
public class FirebaseDatabaseHelper implements FirebaseDbHelper {
    private static FirebaseDatabaseHelper mInstance;
    private final Context mContext;

    private FirebaseDatabaseHelper(Context context) {
        mContext = context;
    }

    public static FirebaseDatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FirebaseDatabaseHelper(context);
        }
        return mInstance;
    }

    // todo fetch user id from firebase instead user input
    private int getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("id", "0");
        if (!user.equals("0")) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getUser() {
        return getUser(mContext);
    }

    @Override
    public Demand getDemand() {
        return new Demand("I need a private English class",
            "The class have maximum two students, price is 1000bath per lesson");
    }
}
