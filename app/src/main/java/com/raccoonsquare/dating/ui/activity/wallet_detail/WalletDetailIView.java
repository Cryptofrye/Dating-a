package com.thinkin_service.provider.ui.activity.wallet_detail;

import com.thinkin_service.provider.base.MvpView;
import com.thinkin_service.provider.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIView extends MvpView {
    void setAdapter(ArrayList<Transaction> myList);
}
