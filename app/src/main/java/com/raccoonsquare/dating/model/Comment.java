package com.raccoonsquare.dating.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.raccoonsquare.dating.constants.Constants;

import org.json.JSONObject;

public class Comment extends Application implements Constants, Parcelable {

    private long id, itemId, replyToUserId;
    private int createAt;
    private String comment, replyToUserUsername, replyToUserFullname, timeAgo;
    private Profile owner;

    public Comment() {

    }

    public Comment(JSONObject jsonData) {

        try {

            this.setId(jsonData.getLong("id"));
            this.setReplyToUserId(jsonData.getLong("replyToUserId"));
            this.setReplyToUserUsername(jsonData.getString("replyToUserUsername"));
            this.setReplyToUserFullname(jsonData.getString("replyToFullname"));
            this.setText(jsonData.getString("comment"));
            this.setTimeAgo(jsonData.getString("timeAgo"));
            this.setCreateAt(jsonData.getInt("createAt"));

            if (jsonData.has("itemId")) {

                this.setItemId(jsonData.getLong("itemId"));

            } else {

                if (jsonData.has("imageId")) {

                    this.setItemId(jsonData.getLong("imageId"));
                }
            }

            if (jsonData.has("owner")) {

                JSONObject ownerObj = (JSONObject) jsonData.getJSONObject("owner");

                this.setOwner(new Profile(ownerObj));
            }

        } catch (Throwable t) {

            Log.e("Comment", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Comment", jsonData.toString());
        }
    }

    public Profile getOwner() {

        if (this.owner == null) {

            this.owner = new Profile();
        }

        return this.owner;
    }

    public void setOwner(Profile owner) {

        this.owner = owner;
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public void setItemId(long itemId) {

        this.itemId = itemId;
    }

    public long getItemId() {

        return this.itemId;
    }

    public void setReplyToUserId(long replyToUserId) {

        this.replyToUserId = replyToUserId;
    }

    public long getReplyToUserId() {

        return this.replyToUserId;
    }

    public void setText(String comment) {

        this.comment = comment;
    }

    public String getText() {

        return this.comment;
    }

    public void setTimeAgo(String timeAgo) {

        this.timeAgo = timeAgo;
    }

    public String getTimeAgo() {

        return this.timeAgo;
    }

    public void setReplyToUserUsername(String replyToUserUsername) {

        this.replyToUserUsername = replyToUserUsername;
    }

    public String getReplyToUserUsername() {

        return this.replyToUserUsername;
    }

    public void setReplyToUserFullname(String replyToUserFullname) {

        this.replyToUserFullname = replyToUserFullname;
    }

    public String getReplyToUserFullname() {

        return this.replyToUserFullname;
    }

    public void setCreateAt(int createAt) {

        this.createAt = createAt;
    }

    public int getCreateAt() {

        return this.createAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.itemId);
        dest.writeLong(this.replyToUserId);
        dest.writeInt(this.createAt);
        dest.writeString(this.comment);
        dest.writeString(this.replyToUserUsername);
        dest.writeString(this.replyToUserFullname);
        dest.writeString(this.timeAgo);
        dest.writeParcelable(this.owner, flags);
    }

    protected Comment(Parcel in) {
        this.id = in.readLong();
        this.itemId = in.readLong();
        this.replyToUserId = in.readLong();
        this.createAt = in.readInt();
        this.comment = in.readString();
        this.replyToUserUsername = in.readString();
        this.replyToUserFullname = in.readString();
        this.timeAgo = in.readString();
        this.owner = (Profile) in.readParcelable(Profile.class.getClassLoader());
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
