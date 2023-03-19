package com.thinkin_service.provider.ui.activity.wallet_detail;

import com.thinkin_service.provider.base.MvpPresenter;
import com.thinkin_service.provider.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIPresenter<V extends WalletDetailIView> extends MvpPresenter<V> {
    void setAdapter(ArrayList<Transaction> myList);
}
