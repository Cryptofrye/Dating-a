package com.thinkin_service.provider.ui.activity.password;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.ForgotResponse;
import com.thinkin_service.provider.data.network.model.User;

public interface PasswordIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);

    void onSuccess(User object);

    void onError(Throwable e);
}
