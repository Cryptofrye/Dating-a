package com.thinkin_service.provider.ui.activity.add_card;

import com.thinkin_service.provider.base.MvpView;

public interface AddCardIView extends MvpView {

    void onSuccess(Object card);

    void onError(Throwable e);
}
