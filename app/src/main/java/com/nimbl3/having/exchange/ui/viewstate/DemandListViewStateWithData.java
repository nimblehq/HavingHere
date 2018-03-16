package com.nimbl3.having.exchange.ui.viewstate;

import com.nimbl3.having.exchange.ui.model.Demand;

public class DemandListViewStateWithData extends DemandListViewState {
    public Demand mDemand;

    public DemandListViewStateWithData(Demand demand) {
        mDemand = demand;
    }
}
