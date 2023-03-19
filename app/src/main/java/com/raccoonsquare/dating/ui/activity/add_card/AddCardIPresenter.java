package com.thinkin_service.provider.ui.activity.add_card;

import com.thinkin_service.provider.base.MvpPresenter;

public interface AddCardIPresenter<V extends AddCardIView> extends MvpPresenter<V> {

    void addCard(String stripeToken);
}
