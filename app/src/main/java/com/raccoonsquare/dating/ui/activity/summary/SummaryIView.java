package com.thinkin_service.provider.ui.activity.summary;


import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.Summary;

public interface SummaryIView extends MvpView {

    void onSuccess(Summary object);

    void onError(Throwable e);
}
