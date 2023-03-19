package com.thinkin_service.provider.ui.bottomsheetdialog.cancel;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.CancelResponse;

import java.util.List;

public interface CancelDialogIView extends MvpView {

    void onSuccessCancel(Object object);
    void onError(Throwable e);
    void onSuccess(List<CancelResponse> response);
    void onReasonError(Throwable e);
}
