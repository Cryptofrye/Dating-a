package com.thinkin_service.provider.ui.activity.document;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.DriverDocumentResponse;

public interface DocumentIView extends MvpView {

    void onSuccess(DriverDocumentResponse response);

    void onDocumentSuccess(DriverDocumentResponse response);

    void onError(Throwable e);

    void onSuccessLogout(Object object);

}
