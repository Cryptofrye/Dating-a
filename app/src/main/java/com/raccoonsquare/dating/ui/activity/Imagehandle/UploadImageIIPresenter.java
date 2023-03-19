package com.thinkin_service.provider.ui.activity.Imagehandle;

import com.thinkin_service.provider.base.BasePresenter;
import com.thinkin_service.provider.data.network.APIClient;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadImageIIPresenter<V extends UploadIView> extends BasePresenter<V> implements UploadImagePresenter<V> {


    @Override
    public void uploadImageApi(String status,String comment, File before, String requestId) {
        MultipartBody.Part filePart = null,afterfile = null;
        String befComment="",aftercomment="";
        if (status.equalsIgnoreCase("before")){
            befComment=comment;
            filePart = MultipartBody.Part.createFormData("before_image", before.getName(), RequestBody.create(MediaType.parse("image/*"), before));

        }
        else{
            aftercomment=comment;
            filePart = MultipartBody.Part.createFormData("after_image", before.getName(), RequestBody.create(MediaType.parse("image/*"), before));

        }

        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .uploadImage(requestId,filePart,befComment,aftercomment)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onWaitingTimeSuccess, getMvpView()::onError));
    }
}
