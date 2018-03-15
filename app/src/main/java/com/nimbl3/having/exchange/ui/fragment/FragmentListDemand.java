package com.nimbl3.having.exchange.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nimbl3.having.exchange.R;
import com.nimbl3.having.exchange.ui.activity.ActivityHome;

/**
 * Created by thuypham on 3/15/2018 AD.
 */

public class FragmentListDemand extends FragmentBase {
    private CardView mDemandCard;
    private View mTvEmpty;

    public static FragmentListDemand newInstance() {
        return new FragmentListDemand();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_demand, container, false);
        mDemandCard = view.findViewById(R.id.demand_card);
        mTvEmpty = view.findViewById(R.id.empty_tv);
        mDemandCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        initData();
        return view;
    }

    private void initData() {
        if (((ActivityHome) getActivity()).isDemandOwner()) {
            showDemands();
        } else {
            hideDemands();
        }
    }

    private void hideDemands() {
        mDemandCard.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.VISIBLE);
    }

    private void showDemands() {
        mDemandCard.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.GONE);
    }

    //todo
    private void initDatabase() {
        // Write a message to the database
        // Fetch list supply, demand
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
