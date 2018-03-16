package com.nimbl3.having.exchange.ui.fragment;

public class FragmentListSupply extends FragmentListDemand {

    public static FragmentListSupply newInstance() {
        return new FragmentListSupply();
    }

    @Override
    protected boolean isShouldShowDemand() {
        return !super.isShouldShowDemand();
    }

}
