package com.nimbl3.having.exchange.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nimbl3.having.exchange.R;

import io.reactivex.Observable;

/**
 * Created by thuypham on 3/15/2018 AD.
 */

public class FragmentListDemand extends FragmentBase {
    public static FragmentListDemand newInstance() {
        return new FragmentListDemand();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_demand, container, false);
        Observable<String> testImplementRx = Observable.just("");
        return view;
    }
}
