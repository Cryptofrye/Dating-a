package com.thinkin_service.provider.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinkin_service.provider.MvpApplication;
import com.thinkin_service.provider.R;
import com.thinkin_service.provider.common.Constants;
import com.thinkin_service.provider.data.network.model.RequestedDataItem;
import com.thinkin_service.provider.ui.activity.request_money.RequestMoneyActivity;

import java.util.List;

public class RequestAmtAdapter extends RecyclerView.Adapter<RequestAmtAdapter.MyViewHolder> {

    private List<RequestedDataItem> mRequestedDataItems;
    private RequestMoneyActivity.RequestedItem mListener;

    public RequestAmtAdapter(List<RequestedDataItem> requestedDataItems, RequestMoneyActivity.RequestedItem mItemListener) {
        this.mRequestedDataItems = requestedDataItems;
        this.mListener = mItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_wallet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvId.setText(mRequestedDataItems.get(position).getAliasId());
        holder.tvDate.setText(String.format("%s%s", Constants.Currency,
                MvpApplication.getInstance().getNewNumberFormat(mRequestedDataItems.get(position).getAmount())));
        holder.ivDelete.setVisibility(View.VISIBLE);
        holder.ivDelete.setOnClickListener(view -> {
            mListener.onDelete(mRequestedDataItems.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return mRequestedDataItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvId, tvDate;
        private ImageView ivDelete;

        private MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tvId);
            tvDate = view.findViewById(R.id.tvDate);
            ivDelete = view.findViewById(R.id.ivDelete);
        }
    }
}