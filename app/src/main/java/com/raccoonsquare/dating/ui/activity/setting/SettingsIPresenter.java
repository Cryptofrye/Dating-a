package com.thinkin_service.provider.ui.activity.setting;

import com.thinkin_service.provider.base.MvpPresenter;

public interface SettingsIPresenter<V extends SettingsIView> extends MvpPresenter<V> {
    void changeLanguage(String languageID);
}
