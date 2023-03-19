package com.thinkin_service.provider.ui.activity.reset_password;

import com.thinkin_service.provider.base.MvpView;

public interface ResetIView extends MvpView{

    void onSuccess(Object object);
    void onError(Throwable e);
}
