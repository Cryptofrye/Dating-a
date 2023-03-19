package com.thinkin_service.provider.ui.activity.past_detail;


import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.HistoryDetail;

public interface PastTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);
    void onError(Throwable e);
}
