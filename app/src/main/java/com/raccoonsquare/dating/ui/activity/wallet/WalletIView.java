package com.thinkin_service.provider.ui.activity.wallet;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.WalletMoneyAddedResponse;
import com.thinkin_service.provider.data.network.model.WalletResponse;

public interface WalletIView extends MvpView {

    void onSuccess(WalletResponse response);

    void onSuccess(WalletMoneyAddedResponse response);

    void onError(Throwable e);
}
