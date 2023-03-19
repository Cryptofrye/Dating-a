package com.thinkin_service.provider.ui.activity.profile;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.UserResponse;

public interface ProfileIView extends MvpView {

    void onSuccess(UserResponse user);

    void onSuccessUpdate(UserResponse object);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);

}
