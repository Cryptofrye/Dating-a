package com.thinkin_service.provider.ui.activity.help;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help object);

    void onError(Throwable e);
}
