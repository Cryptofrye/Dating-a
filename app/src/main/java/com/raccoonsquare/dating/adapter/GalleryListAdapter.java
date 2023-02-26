package com.raccoonsquare.dating.adapter;

import static com.raccoonsquare.dating.R.id.progressBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.model.Image;

import java.util.List;


public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.MyViewHolder> {

    private List<Image> images;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mStateIconContainer;

        public ImageView thumbnail, mStateIcon;
        public ImageView playImg;

        public ProgressBar mProgressBar;

        public MyViewHolder(View view) {

            super(view);

            mStateIconContainer = view.findViewById(R.id.state_icon_container);

            thumbnail = view.findViewById(R.id.thumbnail);
            playImg = view.findViewById(R.id.playImg);

            mProgressBar = view.findViewById(progressBar);

            mStateIcon = view.findViewById(R.id.state_icon);
        }
    }


    public GalleryListAdapter(Context context, List<Image> images) {

        mContext = context;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Image image = images.get(position);

        holder.mStateIconContainer.setVisibility(View.GONE);

        holder.thumbnail.setVisibility(View.VISIBLE);
        holder.mProgressBar.setVisibility(View.VISIBLE);
        holder.playImg.setVisibility(View.GONE);
        holder.mStateIcon.setVisibility(View.GONE);

        final ProgressBar progressBar = holder.mProgressBar;
        final ImageView playImg = holder.playImg;

        if (image.getItemType() == Constants.GALLERY_ITEM_TYPE_VIDEO) {

            if (image.getPreviewImgUrl().length() != 0) {

                Glide.with(mContext)
                        .load(image.getPreviewImgUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                progressBar.setVisibility(View.GONE);
                                playImg.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                progressBar.setVisibility(View.GONE);
                                playImg.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(holder.thumbnail);

            } else {

                holder.thumbnail.setImageResource(R.drawable.ic_video_preview);
                holder.mProgressBar.setVisibility(View.GONE);
            }

        } else {

            Glide.with(mContext)
                    .load(image.getImgUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.thumbnail);
        }

        if (image.getOwner().getId() == App.getInstance().getId()) {

            holder.mStateIconContainer.setVisibility(View.VISIBLE);
            holder.mStateIcon.setVisibility(View.VISIBLE);

            if (image.getModerateAt() == 0) {

                holder.mStateIcon.setImageResource(R.drawable.ic_wait_small);
                holder.mStateIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

            } else {

                holder.mStateIcon.setImageResource(R.drawable.ic_accept);
                holder.mStateIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    public int getItemCount() {

        return images.size();
    }

    public interface ClickListener {

        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryListAdapter.ClickListener clickListener) {

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child != null && clickListener != null) {

                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());

            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {

                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}