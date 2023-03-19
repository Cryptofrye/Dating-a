package com.thinkin_service.provider.ui.activity.request_money;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.RequestDataResponse;

public interface RequestMoneyIView extends MvpView {

    void onSuccess(RequestDataResponse response);
    void onSuccess(Object response);
    void onError(Throwable e);

}
