package com.thinkin_service.provider.ui.activity.instant_ride;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.EstimateFare;
import com.thinkin_service.provider.data.network.model.TripResponse;

public interface InstantRideIView extends MvpView {

    void onSuccess(EstimateFare estimateFare);

    void onSuccess(TripResponse response);

    void onError(Throwable e);

}
