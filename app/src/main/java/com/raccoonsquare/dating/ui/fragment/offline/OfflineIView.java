package com.thinkin_service.provider.ui.fragment.offline;

import com.thinkin_service.provider.base.MvpView;

public interface OfflineIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
