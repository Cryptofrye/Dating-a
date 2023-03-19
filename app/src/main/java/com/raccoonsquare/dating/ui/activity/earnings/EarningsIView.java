package com.thinkin_service.provider.ui.activity.earnings;


import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.EarningsList;

public interface EarningsIView extends MvpView {

    void onSuccess(EarningsList earningsLists);

    void onError(Throwable e);
}
