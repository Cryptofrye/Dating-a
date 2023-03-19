package com.thinkin_service.provider.ui.bottomsheetdialog.rating;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.Rating;

public interface RatingDialogIView extends MvpView {

    void onSuccess(Rating rating);
    void onError(Throwable e);
}
