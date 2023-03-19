package com.thinkin_service.provider.ui.activity.setting;

import com.thinkin_service.provider.base.MvpView;

public interface SettingsIView extends MvpView {

    void onSuccess(Object o);

    void onError(Throwable e);

}
