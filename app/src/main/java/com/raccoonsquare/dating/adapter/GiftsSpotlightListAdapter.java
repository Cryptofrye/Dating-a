package com.raccoonsquare.dating.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.model.Gift;

import java.util.List;


public class GiftsSpotlightListAdapter extends RecyclerView.Adapter<GiftsSpotlightListAdapter.MyViewHolder> {

    private List<Gift> items;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, Gift obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.mOnItemClickListener = mItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public ProgressBar mProgressBar;
        public MaterialRippleLayout mParent;

        public MyViewHolder(View view) {

            super(view);

            mParent = view.findViewById(R.id.parent);
            thumbnail = view.findViewById(R.id.thumbnail);
            mProgressBar = view.findViewById(R.id.progressBar);
        }
    }


    public GiftsSpotlightListAdapter(Context context, List<Gift> items) {

        mContext = context;
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_spotlight_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Gift item = items.get(position);

        holder.thumbnail.setVisibility(View.VISIBLE);
        holder.mProgressBar.setVisibility(View.VISIBLE);

        if (item.getImgUrl() != null && item.getImgUrl().length() > 0) {

            final ProgressBar progressBar = holder.mProgressBar;
            final ImageView imageView = holder.thumbnail;

            Glide.with(mContext)
                    .load(item.getImgUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            progressBar.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.img_loading);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.thumbnail);

        } else {

            holder.mProgressBar.setVisibility(View.GONE);
            holder.thumbnail.setImageResource(R.drawable.img_loading);
        }

        holder.mParent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mOnItemClickListener != null) {

                    mOnItemClickListener.onItemClick(view, item, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return items.size();
    }
}