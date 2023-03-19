package com.thinkin_service.provider.ui.activity.card;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.Card;

import java.util.List;

public interface CardIView extends MvpView {

    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onError(Throwable e);

    void onSuccessChangeCard(Object card);
}
