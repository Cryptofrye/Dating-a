package com.thinkin_service.provider.ui.activity.invite_friend;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.UserResponse;

public interface InviteFriendIView extends MvpView {

    void onSuccess(UserResponse response);
    void onError(Throwable e);

}
