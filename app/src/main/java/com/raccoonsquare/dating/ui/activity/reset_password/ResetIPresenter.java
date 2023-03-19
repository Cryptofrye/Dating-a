package com.thinkin_service.provider.ui.activity.reset_password;

import com.thinkin_service.provider.base.MvpPresenter;

import java.util.HashMap;

public interface ResetIPresenter<V extends ResetIView> extends MvpPresenter<V> {

    void reset(HashMap<String, Object> obj);

}
