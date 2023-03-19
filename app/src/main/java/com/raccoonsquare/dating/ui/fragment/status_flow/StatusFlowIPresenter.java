package com.thinkin_service.provider.ui.fragment.status_flow;

import com.thinkin_service.provider.base.MvpPresenter;

import java.io.File;
import java.util.HashMap;

public interface StatusFlowIPresenter<V extends StatusFlowIView> extends MvpPresenter<V> {

    void statusUpdate(HashMap<String, Object> obj, Integer id);

    void waitingTime(String time, String requestId);


    void uploadImageApi(String status, File before, String requestId);

    void checkWaitingTime(String requestId);
}
