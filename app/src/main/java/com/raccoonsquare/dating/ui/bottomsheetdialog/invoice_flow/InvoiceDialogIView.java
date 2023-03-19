package com.thinkin_service.provider.ui.bottomsheetdialog.invoice_flow;

import com.thinkin_service.provider.base.MvpView;

public interface InvoiceDialogIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
