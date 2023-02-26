package com.raccoonsquare.dating.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.toolbox.ImageLoader;
import com.pkmmte.view.CircularImageView;
import com.raccoonsquare.dating.PhotoViewActivity;
import com.raccoonsquare.dating.ProfileActivity;
import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.model.ChatItem;
import com.raccoonsquare.dating.view.ResizableImageView;

import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconTextView;

public class ChatListAdapter extends BaseAdapter implements Constants {

	private FragmentActivity activity;
	private LayoutInflater inflater;
	private List<ChatItem> dialogList;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

	public ChatListAdapter(FragmentActivity activity, List<ChatItem> dialogList) {

		this.activity = activity;
		this.dialogList = dialogList;
	}

	@Override
	public int getCount() {

		return dialogList.size();
	}

	@Override
	public Object getItem(int location) {

		return dialogList.get(location);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}
	
	static class ViewHolder {

        public ResizableImageView mLeft_Img;
        public ResizableImageView mRight_Img;

        public TextView mLeft_TimeAgo;
        public EmojiconTextView mLeft_Message;
        public TextView mLeftReport;
		public CircularImageView mLeft_FromUser;

        public TextView mRight_TimeAgo;
        public EmojiconTextView mRight_Message;
        public CircularImageView mRight_FromUser;

        public LinearLayout mLeftItem;
        public LinearLayout mRightItem;

        public ImageView mSeenIcon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;

		final ChatItem chatItem = dialogList.get(position);

		if (inflater == null) {

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

		if (convertView == null) {

            convertView = inflater.inflate(R.layout.chat_left_item_list_row, null);
			
			viewHolder = new ViewHolder();

            viewHolder.mLeft_Img = convertView.findViewById(R.id.left_img);
            viewHolder.mRight_Img = convertView.findViewById(R.id.right_img);

            viewHolder.mLeftItem = convertView.findViewById(R.id.leftItem);
            viewHolder.mRightItem = convertView.findViewById(R.id.rightItem);

            viewHolder.mLeft_FromUser = convertView.findViewById(R.id.left_fromUser);
            viewHolder.mLeft_Message = convertView.findViewById(R.id.left_message);
			viewHolder.mLeft_TimeAgo = convertView.findViewById(R.id.left_timeAgo);
            viewHolder.mLeftReport = convertView.findViewById(R.id.left_report);

            viewHolder.mRight_FromUser = convertView.findViewById(R.id.right_fromUser);
            viewHolder.mRight_Message = convertView.findViewById(R.id.right_message);
            viewHolder.mRight_TimeAgo = convertView.findViewById(R.id.right_timeAgo);

            viewHolder.mSeenIcon = convertView.findViewById(R.id.seenIcon);

            convertView.setTag(viewHolder);

            viewHolder.mLeft_FromUser.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int getPosition = (Integer) v.getTag();

                    ChatItem chatItem = dialogList.get(getPosition);

                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("profileId", chatItem.getFromUserId());
                    activity.startActivity(intent);
                }
            });

		} else {
			
			viewHolder = (ViewHolder) convertView.getTag();
		}

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }

        viewHolder.mRight_Img.setTag(position);
        viewHolder.mLeft_Img.setTag(position);

        viewHolder.mLeft_TimeAgo.setTag(position);
        viewHolder.mLeft_Message.setTag(position);
        viewHolder.mLeft_FromUser.setTag(position);

        viewHolder.mRight_TimeAgo.setTag(position);
        viewHolder.mRight_Message.setTag(position);
        viewHolder.mRight_FromUser.setTag(position);

        viewHolder.mLeftItem.setTag(position);
        viewHolder.mRightItem.setTag(position);

        viewHolder.mSeenIcon.setTag(position);

        if (App.getInstance().getId() == chatItem.getFromUserId()) {

            viewHolder.mLeftItem.setVisibility(View.GONE);

            viewHolder.mRightItem.setVisibility(View.VISIBLE);

            if (chatItem.getFromUserPhotoUrl().length() > 0) {

                imageLoader.get(chatItem.getFromUserPhotoUrl(), ImageLoader.getImageListener(viewHolder.mRight_FromUser, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

            } else {

                viewHolder.mRight_FromUser.setImageResource(R.drawable.profile_default_photo);
            }

            if (chatItem.getStickerId() != 0) {

                viewHolder.mRight_Img.getLayoutParams().width = 256;
                viewHolder.mRight_Img.requestLayout();

                viewHolder.mRight_Img.setOnClickListener(null);

                imageLoader.get(chatItem.getStickerImgUrl(), ImageLoader.getImageListener(viewHolder.mRight_Img, R.drawable.img_loading, R.drawable.img_loading));
                viewHolder.mRight_Img.setVisibility(View.VISIBLE);

            } else {

                if (chatItem.getImgUrl().length() != 0) {

                    viewHolder.mRight_Img.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    viewHolder.mRight_Img.requestLayout();

                    viewHolder.mRight_Img.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(activity, PhotoViewActivity.class);
                            i.putExtra("imgUrl", chatItem.getImgUrl());
                            activity.startActivity(i);
                        }
                    });

                    imageLoader.get(chatItem.getImgUrl(), ImageLoader.getImageListener(viewHolder.mRight_Img, R.drawable.img_loading, R.drawable.img_loading));
                    viewHolder.mRight_Img.setVisibility(View.VISIBLE);

                } else {

                    viewHolder.mRight_Img.setVisibility(View.GONE);
                }
            }

            viewHolder.mRight_FromUser.setVisibility(View.GONE);

            if (chatItem.getMessage().length() > 0) {

                viewHolder.mRight_Message.setVisibility(View.VISIBLE);

            } else {

                viewHolder.mRight_Message.setVisibility(View.GONE);
            }

            viewHolder.mRight_Message.setText(chatItem.getMessage().replaceAll("<br>", "\n"));

            if (chatItem.getSeenAt() > 0) {

                viewHolder.mSeenIcon.setVisibility(View.VISIBLE);

            } else {

                viewHolder.mSeenIcon.setVisibility(View.GONE);
            }

            viewHolder.mRight_TimeAgo.setText(chatItem.getTimeAgo());

            viewHolder.mRight_Message.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("msg", chatItem.getMessage().replaceAll("<br>", "\n"));
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(activity, activity.getString(R.string.msg_copied_to_clipboard), Toast.LENGTH_SHORT).show();

                    return false;
                }
            });

        } else {

            viewHolder.mRightItem.setVisibility(View.GONE);

            viewHolder.mLeftItem.setVisibility(View.VISIBLE);

            if (chatItem.getFromUserPhotoUrl().length() > 0) {

                imageLoader.get(chatItem.getFromUserPhotoUrl(), ImageLoader.getImageListener(viewHolder.mLeft_FromUser, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

            } else {

                viewHolder.mLeft_FromUser.setImageResource(R.drawable.profile_default_photo);
            }

            if (chatItem.getImgUrl().length() != 0) {

                if (chatItem.getStickerId() == 0) {

                    viewHolder.mRight_Img.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    viewHolder.mRight_Img.requestLayout();

                    viewHolder.mLeft_Img.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(activity, PhotoViewActivity.class);
                            i.putExtra("imgUrl", chatItem.getImgUrl());
                            activity.startActivity(i);
                        }
                    });

                } else {

                    viewHolder.mRight_Img.setOnClickListener(null);

                    viewHolder.mLeft_Img.getLayoutParams().width = 256;
                    viewHolder.mLeft_Img.requestLayout();
                }

                imageLoader.get(chatItem.getImgUrl(), ImageLoader.getImageListener(viewHolder.mLeft_Img, R.drawable.img_loading, R.drawable.img_loading));
                viewHolder.mLeft_Img.setVisibility(View.VISIBLE);

            } else {

                viewHolder.mLeft_Img.setVisibility(View.GONE);
            }

            if (chatItem.getMessage().length() > 0) {

                viewHolder.mLeft_Message.setVisibility(View.VISIBLE);

            } else {

                viewHolder.mLeft_Message.setVisibility(View.GONE);
            }

            viewHolder.mLeft_Message.setText(chatItem.getMessage().replaceAll("<br>", "\n"));
            viewHolder.mLeft_TimeAgo.setText(chatItem.getTimeAgo());

            viewHolder.mLeft_Message.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("msg", chatItem.getMessage().replaceAll("<br>", "\n"));
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(activity, activity.getString(R.string.msg_copied_to_clipboard), Toast.LENGTH_SHORT).show();

                    return false;
                }
            });

            //

            viewHolder.mLeftReport.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            viewHolder.mLeftReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    report();
                }
            });
        }

		return convertView;
	}

    public void report() {

        String[] profile_report_categories = new String[] {

                activity.getText(R.string.label_profile_report_0).toString(),
                activity.getText(R.string.label_profile_report_1).toString(),
                activity.getText(R.string.label_profile_report_2).toString(),
                activity.getText(R.string.label_profile_report_3).toString(),

        };

        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getText(R.string.label_item_report_title));

        alertDialog.setSingleChoiceItems(profile_report_categories, 0, null);
        alertDialog.setCancelable(true);

        alertDialog.setNegativeButton(activity.getText(R.string.action_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        alertDialog.setPositiveButton(activity.getText(R.string.action_ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                androidx.appcompat.app.AlertDialog alert = (androidx.appcompat.app.AlertDialog) dialog;
                int reason = alert.getListView().getCheckedItemPosition();

                Toast.makeText(activity, activity.getString(R.string.label_item_report_sent), Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }
}