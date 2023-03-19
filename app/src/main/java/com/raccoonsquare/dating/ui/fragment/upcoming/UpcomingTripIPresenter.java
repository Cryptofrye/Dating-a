package com.thinkin_service.provider.ui.fragment.upcoming;


import com.thinkin_service.provider.base.MvpPresenter;

public interface UpcomingTripIPresenter<V extends UpcomingTripIView> extends MvpPresenter<V> {

    void getUpcoming();

}
