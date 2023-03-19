package com.thinkin_service.provider.ui.activity.upcoming_detail;


import com.thinkin_service.provider.base.MvpPresenter;

public interface UpcomingTripDetailIPresenter<V extends UpcomingTripDetailIView> extends MvpPresenter<V> {

    void getUpcomingDetail(String request_id);

}
