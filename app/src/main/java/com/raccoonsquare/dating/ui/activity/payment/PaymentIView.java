package com.thinkin_service.provider.ui.activity.payment;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.Card;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface PaymentIView extends MvpView {
    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onError(Throwable e);
}
