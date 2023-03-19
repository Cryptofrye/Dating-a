package com.thinkin_service.provider.ui.activity.notification_manager;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.NotificationManager;

import java.util.List;

public interface NotificationManagerIView extends MvpView {

    void onSuccess(List<NotificationManager> managers);

    void onError(Throwable e);

}