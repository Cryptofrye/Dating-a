package com.thinkin_service.provider.ui.fragment.status_flow;

import com.thinkin_service.provider.base.BasePresenter;
import com.thinkin_service.provider.data.network.APIClient;

import java.io.File;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StatusFlowPresenter<V extends StatusFlowIView> extends BasePresenter<V> implements StatusFlowIPresenter<V> {

    @Override
    public void statusUpdate(HashMap<String, Object> obj, Integer id) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .updateRequest(obj, id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void waitingTime(String time, String requestId) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .waitingTime(time, requestId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onWaitingTimeSuccess, getMvpView()::onError));
    }

    @Override
    public void uploadImageApi(String status, File before, String requestId) {
        MultipartBody.Part filePart = null,afterfile = null;
        filePart = MultipartBody.Part.createFormData("image", before.getName(), RequestBody.create(MediaType.parse("image/*"), before));

        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .uploadImage(requestId,filePart)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onWaitingTimeSuccess, getMvpView()::onError));
    }

    @Override
    public void checkWaitingTime(String requestId) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .CheckWaitingTime(requestId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onWaitingTimeSuccess, getMvpView()::onError));
    }
}
