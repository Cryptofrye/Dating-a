package com.thinkin_service.provider.ui.activity.forgot_password;

import com.thinkin_service.provider.base.MvpPresenter;

import java.util.HashMap;

public interface ForgotIPresenter<V extends ForgotIView> extends MvpPresenter<V> {

    void forgot(HashMap<String, Object> obj);

}
