package com.thinkin_service.provider.ui.fragment.upcoming;

import com.thinkin_service.provider.base.BasePresenter;
import com.thinkin_service.provider.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UpcomingTripPresenter<V extends UpcomingTripIView> extends BasePresenter<V> implements UpcomingTripIPresenter<V>  {
    @Override
    public void getUpcoming() {
        getCompositeDisposable().add(
                APIClient
                        .getAPIClient()
                        .getUpcoming()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                trendsResponse -> getMvpView().onSuccess(trendsResponse),
                                throwable -> getMvpView().onError(throwable)
                        )
        );
    }
}
