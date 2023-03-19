package com.thinkin_service.provider.ui.activity.wallet_detail;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.thinkin_service.provider.R;
import com.thinkin_service.provider.base.BaseActivity;
import com.thinkin_service.provider.data.network.model.Transaction;
import com.thinkin_service.provider.ui.adapter.WalletDetailAdapter;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletDetailActivity extends BaseActivity implements WalletDetailIView {


    @BindView(R.id.rvWalletDetailData)
    RecyclerView rvWalletDetailData;


    WalletDetailPresenter<WalletDetailActivity> presenter = new WalletDetailPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        ArrayList<Transaction> mTransactionArrayList = (ArrayList<Transaction>) getIntent().getSerializableExtra("transaction_list");
        String alias = getIntent().getStringExtra("alias");
        Objects.requireNonNull(getSupportActionBar()).setTitle(alias);
        rvWalletDetailData.setLayoutManager(new LinearLayoutManager(activity(), LinearLayoutManager.VERTICAL, false));
        rvWalletDetailData.setItemAnimator(new DefaultItemAnimator());
        rvWalletDetailData.setHasFixedSize(true);
        presenter.setAdapter(mTransactionArrayList);
    }

    @Override
    public void setAdapter(ArrayList<Transaction> myList) {
        rvWalletDetailData.setAdapter(new WalletDetailAdapter(myList));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
