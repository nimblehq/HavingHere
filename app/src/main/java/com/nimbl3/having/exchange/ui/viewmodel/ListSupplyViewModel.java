package com.nimbl3.having.exchange.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.nimbl3.having.exchange.ui.HavingApplication;
import com.nimbl3.having.exchange.ui.database.FirebaseDatabaseHelper;
import com.nimbl3.having.exchange.ui.database.FirebaseDbHelper;
import com.nimbl3.having.exchange.ui.intents.DemandListIntents;
import com.nimbl3.having.exchange.ui.model.Demand;
import com.nimbl3.having.exchange.ui.mvibase.MviViewModel;
import com.nimbl3.having.exchange.ui.viewstate.DemandListViewState;
import com.nimbl3.having.exchange.ui.viewstate.DemandListViewStateWithData;
import com.nimbl3.having.exchange.ui.viewstate.DemandListviewStateEmpty;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;

public class ListSupplyViewModel extends ViewModel implements MviViewModel<DemandListIntents, DemandListViewState> {
    protected PublishSubject<DemandListIntents> mIntentsSubject = PublishSubject.create();
    protected Observable<DemandListViewState> mStatesObservable = PublishSubject.create();
    protected Context mContext;
    protected FirebaseDbHelper dbHelper;

    private BiFunction<DemandListViewState, DemandListIntents, DemandListViewState> mReducer =
        (previousState, result) -> {
            int userId = dbHelper.getUser();
            Demand demand = dbHelper.getDemand();
            return userId == 0
                ? new DemandListviewStateEmpty()
                : new DemandListViewStateWithData(demand);
        };

    public ListSupplyViewModel() {
        super();
        mIntentsSubject = PublishSubject.create();
        mStatesObservable = compose();
        mContext = HavingApplication.getInstance().getApplicationContext();
        dbHelper = FirebaseDatabaseHelper.getInstance(mContext);
    }

    protected Observable<DemandListViewState> compose() {
        return mIntentsSubject
            .take(1)
            // Cache each state and pass it to the reducer to create a new state from
            // the previous cached one and the latest Result emitted from the action processor.
            // The Scan operator is used here for the caching.
            .scan(DemandListViewState.idle(), mReducer)
            // When a reducer just emits previousState, there's no reason to call render. In fact,
            // redrawing the UI in cases like this can cause jank (e.g. messing up snackbar animations
            // by showing the same snackbar twice in rapid succession).
            .distinctUntilChanged()
            // Emit the last one event of the stream on subscription
            // Useful when a View rebinds to the ViewModel after rotation.
            .replay(1)
            // Create the stream on creation without waiting for anyone to subscribe
            // This allows the stream to stay alive even when the UI disconnects and
            // match the stream's lifecycle to the ViewModel's one.
            .autoConnect(0);
    }

    @Override
    public void processIntents(Observable<DemandListIntents> intents) {
        intents.subscribe(mIntentsSubject);
    }

    @Override
    public Observable<DemandListViewState> states() {
        return mStatesObservable;
    }
}
