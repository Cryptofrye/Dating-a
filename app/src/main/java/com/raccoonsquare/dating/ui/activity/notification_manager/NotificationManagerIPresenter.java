package com.thinkin_service.provider.ui.activity.notification_manager;

import com.thinkin_service.provider.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
