package com.thinkin_service.provider.ui.activity.Imagehandle;

import com.google.gson.JsonObject;
import com.thinkin_service.provider.base.MvpView;

public interface UploadIView extends MvpView {

    void onSuccess(JsonObject object);

    void onError(Throwable e);

    void onWaitingTimeSuccess(JsonObject jsonObject);

}
