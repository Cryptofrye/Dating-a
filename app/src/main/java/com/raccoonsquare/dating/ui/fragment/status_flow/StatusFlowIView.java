package com.thinkin_service.provider.ui.fragment.status_flow;

import com.google.gson.JsonObject;
import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.TimerResponse;

public interface StatusFlowIView extends MvpView {

    void onSuccess(Object object);

    void onWaitingTimeSuccess(TimerResponse object);

    void onError(Throwable e);

    void onWaitingTimeSuccess(JsonObject jsonObject);
}
