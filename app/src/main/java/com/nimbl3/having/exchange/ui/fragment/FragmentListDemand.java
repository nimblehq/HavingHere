package com.nimbl3.having.exchange.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nimbl3.having.exchange.R;
import com.nimbl3.having.exchange.ui.activity.ActivityDemandDetail;
import com.nimbl3.having.exchange.ui.database.FirebaseDatabaseHelper;
import com.nimbl3.having.exchange.ui.model.Demands;

public class FragmentListDemand extends FragmentBase {
    private CardView mDemandCard;
    private View mTvEmpty;
    private TextView mTvTitle;
    private TextView mTvDetail;

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
        mTvTitle = view.findViewById(R.id.title);
        mTvDetail = view.findViewById(R.id.detail);
        mDemandCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityDemandDetail.class);
                startActivity(intent);
            }
        });
        initData();
        return view;
    }

    protected boolean isShouldShowDemand() {
        return FirebaseDatabaseHelper.getInstance(getContext()).getUser(getContext()) == 0;
    }

    protected void initData() {
        if (isShouldShowDemand()) {
            showDemands();
            setDemandsData();
        } else {
            showEmptyState();
        }
    }

    protected void setDemandsData() {
        Demands demands = new Demands("I need a private English class",
            "The class have maximum two students, price is 1000bath per lesson");
        mTvTitle.setText(demands.title);
        mTvDetail.setText(demands.description);
    }

    protected void showEmptyState() {
        mDemandCard.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.VISIBLE);
    }

    protected void showDemands() {
        mDemandCard.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.GONE);
    }
}
