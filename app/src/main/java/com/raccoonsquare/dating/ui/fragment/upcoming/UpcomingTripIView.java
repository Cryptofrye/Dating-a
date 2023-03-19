package com.thinkin_service.provider.ui.fragment.upcoming;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.HistoryList;

import java.util.List;

public interface UpcomingTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
