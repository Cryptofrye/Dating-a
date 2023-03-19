package com.thinkin_service.provider.ui.activity.change_password;

import com.thinkin_service.provider.base.MvpView;

public interface ChangePasswordIView extends MvpView {


    void onSuccess(Object object);
    void onError(Throwable e);
}
