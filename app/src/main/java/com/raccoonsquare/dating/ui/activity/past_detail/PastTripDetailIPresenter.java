package com.thinkin_service.provider.ui.activity.past_detail;


import com.thinkin_service.provider.base.MvpPresenter;

public interface PastTripDetailIPresenter<V extends PastTripDetailIView> extends MvpPresenter<V> {

    void getPastTripDetail(String request_id);
}
