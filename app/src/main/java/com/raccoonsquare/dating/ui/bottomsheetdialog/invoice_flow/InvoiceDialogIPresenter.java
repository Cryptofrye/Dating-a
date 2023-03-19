package com.thinkin_service.provider.ui.bottomsheetdialog.invoice_flow;

import com.thinkin_service.provider.base.MvpPresenter;

import java.util.HashMap;

public interface InvoiceDialogIPresenter<V extends InvoiceDialogIView> extends MvpPresenter<V> {

    void statusUpdate(HashMap<String, Object> obj, Integer id);

}
