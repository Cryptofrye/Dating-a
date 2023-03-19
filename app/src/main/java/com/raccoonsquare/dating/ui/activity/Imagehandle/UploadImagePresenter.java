package com.thinkin_service.provider.ui.activity.Imagehandle;


import com.thinkin_service.provider.base.MvpPresenter;

import java.io.File;

public interface UploadImagePresenter<V extends UploadIView> extends MvpPresenter<V> {

    void uploadImageApi(String status, String comment, File before, String requestId);
}
