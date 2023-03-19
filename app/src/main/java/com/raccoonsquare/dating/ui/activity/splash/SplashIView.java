package com.thinkin_service.provider.ui.activity.splash;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.CheckVersion;

public interface SplashIView extends MvpView {

    void verifyAppInstalled();

    void onSuccess(Object user);

    void onSuccess(CheckVersion user);

    void onError(Throwable e);

    void onCheckVersionError(Throwable e);
}
