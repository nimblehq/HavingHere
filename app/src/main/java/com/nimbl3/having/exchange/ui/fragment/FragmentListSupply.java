package com.nimbl3.having.exchange.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nimbl3.having.exchange.R;
import com.nimbl3.having.exchange.ui.activity.ActivityChat;
import com.nimbl3.having.exchange.ui.intents.DemandListIntents;
import com.nimbl3.having.exchange.ui.model.Demand;
import com.nimbl3.having.exchange.ui.viewmodel.ListSupplyViewModel;
import com.nimbl3.having.exchange.ui.viewstate.DemandListViewState;
import com.nimbl3.having.exchange.ui.viewstate.DemandListViewStateWithData;
import com.nimbl3.having.exchange.ui.viewstate.DemandListviewStateEmpty;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class FragmentListSupply extends FragmentBase {

    private CardView mDemandCard;
    private View mTvEmpty;
    private TextView mTvTitle;
    private TextView mTvDetail;
    private ListSupplyViewModel mViewModel;
    private CompositeDisposable mDisposables = new CompositeDisposable();

    public static FragmentListSupply newInstance() {
        return new FragmentListSupply();
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
        mDemandCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityChat.class);
            startActivity(intent);
        });

        mViewModel = ViewModelProviders.of(this).get(ListSupplyViewModel.class);
        bind();
        return view;
    }

    private void bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        mDisposables.add(mViewModel.states().subscribe(this::render));
        // Pass the UI's intents to the ViewModel
        mViewModel.processIntents(intents());
    }

    private Observable<DemandListIntents> intents() {
        return Observable.just(new DemandListIntents() {
        });
    }

    protected void render(DemandListViewState demandListViewState) {
        if (demandListViewState instanceof DemandListviewStateEmpty) {
            showEmptyState();
        } else if (demandListViewState instanceof DemandListViewStateWithData) {
            DemandListViewStateWithData demandListViewStateWithData = (DemandListViewStateWithData) demandListViewState;
            showDemands(demandListViewStateWithData.mDemand);
        }
    }

    protected void setDemandsData(Demand demand) {
        mTvTitle.setText(demand.title);
        mTvDetail.setText(demand.description);
    }

    protected void showEmptyState() {
        mDemandCard.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.VISIBLE);
    }

    protected void showDemands(Demand demand) {
        mDemandCard.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.GONE);
        setDemandsData(demand);
    }
}
