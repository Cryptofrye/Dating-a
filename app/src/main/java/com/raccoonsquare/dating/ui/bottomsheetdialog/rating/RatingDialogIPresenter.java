package com.thinkin_service.provider.ui.bottomsheetdialog.rating;

import com.thinkin_service.provider.base.MvpPresenter;

import java.util.HashMap;

public interface RatingDialogIPresenter<V extends RatingDialogIView> extends MvpPresenter<V> {

    void rate(HashMap<String, Object> obj, Integer id);
}
