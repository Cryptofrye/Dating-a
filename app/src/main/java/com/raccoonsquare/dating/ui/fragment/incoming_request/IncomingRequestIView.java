package com.thinkin_service.provider.ui.fragment.incoming_request;

import com.thinkin_service.provider.base.MvpView;

public interface IncomingRequestIView extends MvpView {

    void onSuccessAccept(Object responseBody);
    void onSuccessCancel(Object object);
    void onError(Throwable e);
}
