package com.thinkin_service.provider.ui.fragment.past;


import com.thinkin_service.provider.base.MvpPresenter;

public interface PastTripIPresenter<V extends PastTripIView> extends MvpPresenter<V> {

    void getHistory();

}
