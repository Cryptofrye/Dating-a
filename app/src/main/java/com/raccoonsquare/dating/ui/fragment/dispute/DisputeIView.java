package com.thinkin_service.provider.ui.fragment.dispute;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.DisputeResponse;

import java.util.List;

public interface DisputeIView extends MvpView {

    void onSuccessDispute(List<DisputeResponse> responseList);

    void onSuccess(Object object);

    void onError(Throwable e);
}
