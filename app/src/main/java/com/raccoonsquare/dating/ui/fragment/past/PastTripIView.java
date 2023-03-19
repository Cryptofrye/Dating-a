package com.thinkin_service.provider.ui.fragment.past;


import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.HistoryList;

import java.util.List;

public interface PastTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
