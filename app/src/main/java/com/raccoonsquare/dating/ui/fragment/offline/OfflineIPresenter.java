package com.thinkin_service.provider.ui.fragment.offline;

import com.thinkin_service.provider.base.MvpPresenter;

import java.util.HashMap;

public interface OfflineIPresenter<V extends OfflineIView> extends MvpPresenter<V> {

    void providerAvailable(HashMap<String, Object> obj);
}
