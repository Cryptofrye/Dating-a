package com.thinkin_service.provider.ui.activity.summary;


import com.thinkin_service.provider.base.MvpPresenter;

public interface SummaryIPresenter<V extends SummaryIView> extends MvpPresenter<V> {

    void getSummary(String data);
}
