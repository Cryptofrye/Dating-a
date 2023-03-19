package com.thinkin_service.provider.ui.bottomsheetdialog.cancel;

import com.thinkin_service.provider.base.MvpPresenter;

import java.util.HashMap;

public interface CancelDialogIPresenter<V extends CancelDialogIView> extends MvpPresenter<V> {

    void cancelRequest(HashMap<String, Object> obj);
    void getReasons();
}
