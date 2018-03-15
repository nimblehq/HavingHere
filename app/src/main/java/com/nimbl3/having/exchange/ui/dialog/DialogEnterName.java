package com.nimbl3.having.exchange.ui.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nimbl3.having.exchange.R;

/**
 * Created by thuypham on 3/15/2018 AD.
 */

public class DialogEnterName extends DialogFragment {
    private EditText mEdtName;
    private Button mBtnDone;

    public DialogEnterName() {

    }

    public static DialogEnterName newInstance() {
        return new DialogEnterName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_name, container);
        mEdtName = view.findViewById(R.id.edt_name);
        mBtnDone = view.findViewById(R.id.btn_done);
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("id", mEdtName.getText().toString().trim()).apply();
                dismiss();
            }
        });
        getDialog().setTitle("Enter name");
        return view;
    }
}
