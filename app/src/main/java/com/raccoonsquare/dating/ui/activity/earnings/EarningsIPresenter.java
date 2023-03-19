package com.thinkin_service.provider.ui.activity.earnings;


import com.thinkin_service.provider.base.MvpPresenter;

public interface EarningsIPresenter<V extends EarningsIView> extends MvpPresenter<V> {

    void getEarnings();
}
