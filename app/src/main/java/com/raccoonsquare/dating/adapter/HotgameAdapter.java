package com.raccoonsquare.dating.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.model.Profile;

import java.util.List;


public class HotgameAdapter extends RecyclerView.Adapter<HotgameAdapter.MyViewHolder> {

	private Context mContext;
	private List<Profile> itemList;

	public class MyViewHolder extends RecyclerView.ViewHolder {

		public TextView title, location, distance;
		public ImageView thumbnail, stamp;
		public ProgressBar progress;

		public MyViewHolder(View view) {

			super(view);

			title = view.findViewById(R.id.item_name);
			location = view.findViewById(R.id.item_location);
			distance = view.findViewById(R.id.item_distance);
			thumbnail = view.findViewById(R.id.item_image);
			stamp = view.findViewById(R.id.item_stamp);
			progress = view.findViewById(R.id.progress_bar);
		}
	}


	public HotgameAdapter(Context mContext, List<Profile> itemList) {

		this.mContext = mContext;
		this.itemList = itemList;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe_card, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {

		holder.progress.setVisibility(View.GONE);

		Profile p = itemList.get(position);

		holder.title.setText(p.getFullname() + ", " + p.getAge());

		holder.distance.setVisibility(View.GONE);

		if (p.getDistance() != 0) {

			holder.distance.setVisibility(View.VISIBLE);

			if (p.getDistance() < 3) {

				holder.distance.setText(mContext.getString(R.string.label_nearby));

			} else {

				holder.distance.setText(Integer.toString((int) p.getDistance()) + "km");
			}
		}

		holder.location.setVisibility(View.GONE);

		if (p.getLocation().length() != 0) {

			holder.location.setVisibility(View.VISIBLE);
			holder.location.setText(p.getLocation());
		}

		if (!p.isMatch() && !p.isMyLike()) {

			holder.stamp.setVisibility(View.GONE);

		} else if (p.isMatch()) {

			holder.stamp.setVisibility(View.VISIBLE);
			holder.stamp.setImageResource(R.drawable.ic_hotgame_match);

		} else if (p.isMyLike() && !p.isMatch()) {

			holder.stamp.setVisibility(View.VISIBLE);
			holder.stamp.setImageResource(R.drawable.ic_hotgame_liked);
		}

		//

		final ImageView imgView = holder.thumbnail;
		final ProgressBar progressView = holder.progress;

		Glide.with(mContext)
				.load(p.getLowPhotoUrl())
				.listener(new RequestListener<Drawable>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

						progressView.setVisibility(View.GONE);
						imgView.setImageResource(R.drawable.profile_default_photo);
						imgView.setVisibility(View.VISIBLE);

						return false;
					}

					@Override
					public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

						progressView.setVisibility(View.GONE);
						imgView.setVisibility(View.VISIBLE);

						return false;
					}
				}).into(imgView);
	}

	@Override
	public int getItemCount() {

		return itemList.size();
	}
}