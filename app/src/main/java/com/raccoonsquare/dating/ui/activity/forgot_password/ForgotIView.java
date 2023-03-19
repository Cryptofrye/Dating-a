package com.thinkin_service.provider.ui.activity.forgot_password;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.ForgotResponse;

public interface ForgotIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);
    void onError(Throwable e);
}
