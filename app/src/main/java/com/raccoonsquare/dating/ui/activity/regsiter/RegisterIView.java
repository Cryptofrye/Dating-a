package com.thinkin_service.provider.ui.activity.regsiter;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.SettingsResponse;
import com.thinkin_service.provider.data.network.model.User;

public interface RegisterIView extends MvpView {

    void onSuccess(User user);

    void onSuccess(Object verifyEmail);

    void onSuccess(SettingsResponse response);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);

    void onVerifyEmailError(Throwable e);

}
