package com.thinkin_service.provider.ui.activity.request_money;

import com.thinkin_service.provider.base.BasePresenter;
import com.thinkin_service.provider.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RequestMoneyPresenter<V extends RequestMoneyIView> extends BasePresenter<V> implements RequestMoneyIPresenter<V> {

    @Override
    public void getRequestedData() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getRequestAmtData()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void requestMoney(Double requestedAmt) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .postRequestAmt(requestedAmt, "provider")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void removeRequestMoney(int id) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getRemoveRequestAmt(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
