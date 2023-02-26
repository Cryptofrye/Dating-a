package com.raccoonsquare.dating.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.raccoonsquare.dating.ProfileActivity;
import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.model.Comment;

import java.util.ArrayList;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconTextView;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> implements Constants {

    private List<Comment> items = new ArrayList<>();

    private Context context;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

    private OnItemMenuButtonClickListener onItemMenuButtonClickListener;

    public interface OnItemMenuButtonClickListener {

        void onItemClick(View view, Comment obj, int actionId, int position);
    }

    public void setOnMoreButtonClickListener(final OnItemMenuButtonClickListener onItemMenuButtonClickListener) {

        this.onItemMenuButtonClickListener = onItemMenuButtonClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView mItemAuthorPhoto, mItemAuthorIcon;
        public TextView mItemAuthor;
        public ImageView mItemAuthorOnlineIcon;
        public ImageView mItemMenuButton;
        public EmojiconTextView mItemDescription;
        public TextView mItemTimeAgo;

        public ViewHolder(View v) {

            super(v);

            mItemAuthorPhoto = v.findViewById(R.id.itemAuthorPhoto);
            mItemAuthorIcon = v.findViewById(R.id.itemAuthorIcon);

            mItemAuthor = v.findViewById(R.id.itemAuthor);
            mItemAuthorOnlineIcon = v.findViewById(R.id.itemAuthorOnlineIcon);

            mItemDescription = v.findViewById(R.id.itemDescription);

            mItemMenuButton = v.findViewById(R.id.itemMenuButton);
            mItemTimeAgo = v.findViewById(R.id.itemTimeAgo);
        }

    }

    public CommentsListAdapter(Context ctx, List<Comment> items) {

        this.context = ctx;
        this.items = items;

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Comment p = items.get(position);

        holder.mItemAuthorIcon.setVisibility(View.GONE);
        holder.mItemAuthorOnlineIcon.setVisibility(View.GONE);

        holder.mItemAuthorPhoto.setVisibility(View.VISIBLE);

        holder.mItemAuthorPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profileId", p.getOwner().getId());
                context.startActivity(intent);
            }
        });

        if (p.getOwner().getLowPhotoUrl().length() != 0 &&
                (App.getInstance().getSettings().isAllowShowNotModeratedProfilePhotos() || App.getInstance().getId() == p.getOwner().getId() || p.getOwner().getPhotoModerateAt() != 0)) {

            imageLoader.get(p.getOwner().getLowPhotoUrl(), ImageLoader.getImageListener(holder.mItemAuthorPhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            holder.mItemAuthorPhoto.setVisibility(View.VISIBLE);
            holder.mItemAuthorPhoto.setImageResource(R.drawable.profile_default_photo);
        }

        holder.mItemAuthor.setVisibility(View.VISIBLE);
        holder.mItemAuthor.setText(p.getOwner().getFullname());

        holder.mItemAuthor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profileId", p.getOwner().getId());
                context.startActivity(intent);
            }
        });

        if (p.getText().length() != 0) {

            holder.mItemDescription.setVisibility(View.VISIBLE);
            holder.mItemDescription.setText(p.getText().replaceAll("<br>", "\n"));

        } else {

            holder.mItemDescription.setVisibility(View.GONE);
        }

        holder.mItemTimeAgo.setVisibility(View.VISIBLE);

        String timeAgo;

        timeAgo = p.getTimeAgo();

        if (p.getReplyToUserId() != 0) {

            if (p.getReplyToUserFullname().length() != 0) {

                timeAgo = timeAgo + " " + context.getString(R.string.label_to) + " " + p.getReplyToUserFullname();

            } else {

                timeAgo = timeAgo + " " + context.getString(R.string.label_to) + " @" + p.getReplyToUserUsername();
            }
        }

        holder.mItemTimeAgo.setText(timeAgo);

        holder.mItemMenuButton.setVisibility(View.VISIBLE);
        holder.mItemMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                onItemMenuButtonClick(view, p, position);
            }
        });
    }

    private void onItemMenuButtonClick(final View view, final Comment comment, final int position){

        PopupMenu popupMenu = new PopupMenu(context, view);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                onItemMenuButtonClickListener.onItemClick(view, comment, item.getItemId(), position);

                return true;
            }
        });

        if (comment.getOwner().getId() == App.getInstance().getId()) {

            // only delete option
            popupMenu.inflate(R.menu.menu_comment_popup_1);

        } else {

            // reply option
            popupMenu.inflate(R.menu.menu_comment_popup_3);
        }

        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}