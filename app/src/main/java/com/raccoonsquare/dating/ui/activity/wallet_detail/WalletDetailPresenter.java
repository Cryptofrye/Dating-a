package com.thinkin_service.provider.ui.activity.wallet_detail;

import com.thinkin_service.provider.base.BasePresenter;
import com.thinkin_service.provider.data.network.model.Transaction;

import java.util.ArrayList;

public class WalletDetailPresenter<V extends WalletDetailIView> extends BasePresenter<V> implements WalletDetailIPresenter<V> {
    @Override
    public void setAdapter(ArrayList<Transaction> myList) {
        getMvpView().setAdapter(myList);
    }
}
