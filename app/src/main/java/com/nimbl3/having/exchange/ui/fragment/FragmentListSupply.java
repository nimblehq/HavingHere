package com.nimbl3.having.exchange.ui.fragment;

/**
 * Created by thuypham on 3/15/2018 AD.
 */

public class FragmentListSupply extends FragmentListDemand {

    public static FragmentListSupply newInstance() {
        return new FragmentListSupply();
    }

    @Override
    protected void initData() {
        if (!isShouldShowDemand()) {
            showDemands();
            setDemandsData();
        } else {
            hideDemands();
        }
    }
}
