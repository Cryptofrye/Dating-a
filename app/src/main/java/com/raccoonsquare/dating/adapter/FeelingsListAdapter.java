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
import com.raccoonsquare.dating.model.Feeling;

import java.util.List;


public class FeelingsListAdapter extends RecyclerView.Adapter<FeelingsListAdapter.MyViewHolder> {

	private Context mContext;
	private List<Feeling> itemList;

	private OnItemClickListener mOnItemClickListener;

	public class MyViewHolder extends RecyclerView.ViewHolder {

		public ImageView mFeelingImg;
		public MaterialRippleLayout mParent;
		public ProgressBar mProgressBar;

		public MyViewHolder(View view) {

			super(view);

			mParent = view.findViewById(R.id.parent);

			mFeelingImg = view.findViewById(R.id.feelingImg);
			mProgressBar = view.findViewById(R.id.progressBar);
		}
	}


	public FeelingsListAdapter(Context mContext, List<Feeling> itemList) {

		this.mContext = mContext;
		this.itemList = itemList;
	}

	public interface OnItemClickListener {

		void onItemClick(View view, Feeling obj, int position);
	}

	public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {

		this.mOnItemClickListener = mItemClickListener;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeling_thumbnail, parent, false);


		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Feeling item = itemList.get(position);

		holder.mProgressBar.setVisibility(View.VISIBLE);
		holder.mFeelingImg.setVisibility(View.VISIBLE);

		if (item.getImgUrl() != null && item.getImgUrl().length() > 0) {

			final ImageView img = holder.mFeelingImg;
			final ProgressBar progressView = holder.mProgressBar;

			Glide.with(mContext)
					.load(item.getImgUrl())
					.listener(new RequestListener<Drawable>() {
						@Override
						public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

							progressView.setVisibility(View.GONE);
							img.setImageResource(R.drawable.profile_default_photo);
							img.setVisibility(View.VISIBLE);
							return false;
						}

						@Override
						public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

							progressView.setVisibility(View.GONE);
							img.setVisibility(View.VISIBLE);
							return false;
						}
					})
					.into(holder.mFeelingImg);

		} else {

			holder.mProgressBar.setVisibility(View.GONE);
			holder.mFeelingImg.setVisibility(View.VISIBLE);

			holder.mFeelingImg.setImageResource(R.drawable.ic_feeling_none);
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

		return itemList.size();
	}
}