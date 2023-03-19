package com.thinkin_service.provider.ui.activity.card;

import com.thinkin_service.provider.base.MvpPresenter;

public interface CardIPresenter<V extends CardIView> extends MvpPresenter<V> {

    void deleteCard(String cardId);

    void card();

    void changeCard(String cardId);
}
