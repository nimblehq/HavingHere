package com.nimbl3.having.exchange.ui.viewstate;

import com.nimbl3.having.exchange.ui.mvibase.MviViewState;

public abstract class DemandListViewState implements MviViewState {
    public static DemandListViewState idle() {
        return new DemandListviewStateEmpty() {
        };
    }
}
