package com.thinkin_service.provider.ui.activity.sociallogin;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.Token;

public interface SocialLoginIView extends MvpView {

    void onSuccess(Token token);
    void onError(Throwable e);
}
