package com.raccoonsquare.dating.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.raccoonsquare.dating.ProfileActivity;
import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.model.Gift;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconTextView;



public class GiftsListAdapter extends RecyclerView.Adapter<GiftsListAdapter.ViewHolder> implements Constants {

    private List<Gift> items = new ArrayList<>();

    private Context context;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

    private OnItemMenuButtonClickListener onItemMenuButtonClickListener;

    public interface OnItemMenuButtonClickListener {

        void onItemClick(View view, Gift obj, int position);
    }

    public void setOnMoreButtonClickListener(final OnItemMenuButtonClickListener onItemMenuButtonClickListener) {

        this.onItemMenuButtonClickListener = onItemMenuButtonClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView mItemAuthorPhoto, mItemAuthorIcon;
        public TextView mItemAuthor;
        public ImageView mItemAuthorOnlineIcon;
        public ImageView mItemMenuButton;
        public ImageView mItemImg;
        public RelativeLayout mImageLayout;
        public EmojiconTextView mItemDescription;
        public TextView mItemTimeAgo;
        public ProgressBar mImageProgressBar;

        public ViewHolder(View v) {

            super(v);

            mItemAuthorPhoto = (CircularImageView) v.findViewById(R.id.itemAuthorPhoto);
            mItemAuthorIcon = (CircularImageView) v.findViewById(R.id.itemAuthorIcon);

            mItemAuthor = (TextView) v.findViewById(R.id.itemAuthor);
            mItemAuthorOnlineIcon = (ImageView) v.findViewById(R.id.itemAuthorOnlineIcon);

            mImageLayout = (RelativeLayout) v.findViewById(R.id.image_layout);

            mItemImg = (ImageView) v.findViewById(R.id.item_image);

            mImageProgressBar = (ProgressBar) v.findViewById(R.id.image_progress_bar);

            mItemDescription = (EmojiconTextView) v.findViewById(R.id.itemDescription);

            mItemMenuButton = (ImageView) v.findViewById(R.id.itemMenuButton);
            mItemTimeAgo = (TextView) v.findViewById(R.id.itemTimeAgo);
        }

    }

    public GiftsListAdapter(Context ctx, List<Gift> items) {

        this.context = ctx;
        this.items = items;

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_list_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Gift p = items.get(position);

        holder.mImageProgressBar.setVisibility(View.GONE);

        holder.mImageLayout.setVisibility(View.GONE);

        holder.mItemAuthorPhoto.setVisibility(View.VISIBLE);

        holder.mItemAuthorPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profileId", p.getGiftFromUserId());
                context.startActivity(intent);
            }
        });

        if (p.getGiftFromUserPhotoUrl().length() != 0) {

            imageLoader.get(p.getGiftFromUserPhotoUrl(), ImageLoader.getImageListener(holder.mItemAuthorPhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            holder.mItemAuthorPhoto.setVisibility(View.VISIBLE);
            holder.mItemAuthorPhoto.setImageResource(R.drawable.profile_default_photo);
        }

        if (p.getGiftFromUserVerified() == 1) {

            holder.mItemAuthorIcon.setVisibility(View.VISIBLE);

        } else {

            holder.mItemAuthorIcon.setVisibility(View.GONE);
        }

        holder.mItemAuthor.setVisibility(View.VISIBLE);
        holder.mItemAuthor.setText(p.getGiftFromUserFullname());

        holder.mItemAuthor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profileId", p.getGiftFromUserId());
                context.startActivity(intent);
            }
        });

        holder.mItemAuthorOnlineIcon.setVisibility(View.GONE);

        if (p.getGiftFromUserOnline()) {

            holder.mItemAuthorOnlineIcon.setVisibility(View.VISIBLE);

        } else {

            holder.mItemAuthorOnlineIcon.setVisibility(View.GONE);
        }

        if (p.getImgUrl().length() != 0){

            holder.mImageLayout.setVisibility(View.VISIBLE);
            holder.mItemImg.setVisibility(View.VISIBLE);
            holder.mImageProgressBar.setVisibility(View.VISIBLE);

            final ProgressBar progressView = holder.mImageProgressBar;
            final ImageView imageView = holder.mItemImg;

            Picasso.with(context)
                    .load(p.getImgUrl())
                    .into(holder.mItemImg, new Callback() {

                        @Override
                        public void onSuccess() {

                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                            progressView.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.img_loading);
                        }
                    });

        }

        if (p.getMessage().length() != 0) {

            holder.mItemDescription.setVisibility(View.VISIBLE);
            holder.mItemDescription.setText(p.getMessage());

        } else {

            holder.mItemDescription.setVisibility(View.GONE);
        }

        holder.mItemTimeAgo.setVisibility(View.VISIBLE);
        holder.mItemTimeAgo.setText(p.getTimeAgo());

        holder.mItemMenuButton.setVisibility(View.GONE);

        if (p.getGiftToUserId() == App.getInstance().getId()) {

            holder.mItemMenuButton.setVisibility(View.VISIBLE);
        }

        holder.mItemMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                onItemMenuButtonClick(view, p, position);
            }
        });

        final ImageView mItemMenuButton = holder.mItemMenuButton;

        holder.mItemMenuButton.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    animateIcon(mItemMenuButton);
                }

                return false;
            }
        });
    }

    private void onItemMenuButtonClick(final View view, final Gift gift, final int position){

        onItemMenuButtonClickListener.onItemClick(view, gift, position);
    }

    private void animateIcon(ImageView icon) {

        ScaleAnimation scale = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(175);
        scale.setInterpolator(new LinearInterpolator());

        icon.startAnimation(scale);
    }


    @Override
    public int getItemCount() {

        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }
}