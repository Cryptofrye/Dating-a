package com.thinkin_service.provider.ui.fragment.incoming_request;

import com.thinkin_service.provider.base.MvpPresenter;

public interface IncomingRequestIPresenter<V extends IncomingRequestIView> extends MvpPresenter<V> {

    void accept(Integer id);
    void cancel(Integer id);
}
