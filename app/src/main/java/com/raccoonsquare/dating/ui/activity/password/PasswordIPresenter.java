package com.thinkin_service.provider.ui.activity.password;

import com.thinkin_service.provider.base.MvpPresenter;

import java.util.HashMap;

public interface PasswordIPresenter<V extends PasswordIView> extends MvpPresenter<V> {

    void login(HashMap<String, Object> obj);
    void forgot(HashMap<String, Object> obj);

}
