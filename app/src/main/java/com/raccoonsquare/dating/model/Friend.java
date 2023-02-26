package com.raccoonsquare.dating.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.raccoonsquare.dating.constants.Constants;

import org.json.JSONObject;


public class Friend extends Application implements Constants, Parcelable {

    private long id, friendTo, friendUserId;

    private int verify, vip, pro;

    private String friendUserUsername, friendUserFullname, friendUserPhoto, friendLocation, timeAgo;

    private Boolean online = false;

    private int photoModerateAt = 0;

    public Friend() {


    }

    public Friend(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setFriendUserId(jsonData.getLong("friendUserId"));
                this.setFriendUserVip(jsonData.getInt("friendUserVip"));
                this.setFriendUserVerify(jsonData.getInt("friendUserVerify"));
                this.setFriendUserUsername(jsonData.getString("friendUserUsername"));
                this.setFriendUserFullname(jsonData.getString("friendUserFullname"));
                this.setFriendUserPhotoUrl(jsonData.getString("friendUserPhoto"));
                this.setFriendLocation(jsonData.getString("friendLocation"));
                this.setFriendTo(jsonData.getLong("friendTo"));
                this.setTimeAgo(jsonData.getString("timeAgo"));
                this.setOnline(jsonData.getBoolean("friendUserOnline"));

                if (jsonData.has("friendUserPro")) {

                    this.setFriendUserPro(jsonData.getInt("friendUserPro"));

                } else {

                    this.setFriendUserPro(0);
                }

                if (jsonData.has("photoModerateAt")) {

                    this.setPhotoModerateAt(jsonData.getInt("photoModerateAt"));
                }
            }

        } catch (Throwable t) {

            Log.e("Friend", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Friend", jsonData.toString());
        }
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public void setFriendTo(long friendTo) {

        this.friendTo = friendTo;
    }

    public long getFriendtTo() {

        return this.friendTo;
    }

    public void setFriendUserId(long friendUserId) {

        this.friendUserId = friendUserId;
    }

    public long getFriendUserId() {

        return this.friendUserId;
    }

    public void setFriendUserVip(int friendUserVip) {

        this.vip = friendUserVip;
    }

    public int getFriendUserVip() {

        return this.vip;
    }

    public void setFriendUserPro(int friendUserPro) {

        this.pro = friendUserPro;
    }

    public int getFriendUserPro() {

        return this.pro;
    }

    public Boolean isProMode() {

        return this.pro > 0;

    }

    public void setFriendUserVerify(int friendUserVerify) {

        this.verify = friendUserVerify;
    }

    public int getFriendUserVerify() {

        return this.verify;
    }

    public Boolean isVerify() {

        return this.verify > 0;

    }

    public void setFriendUserUsername(String friendUserUsername) {

        this.friendUserUsername = friendUserUsername;
    }

    public String getFriendUserUsername() {

        return this.friendUserUsername;
    }

    public void setFriendUserFullname(String friendUserFullname) {

        this.friendUserFullname = friendUserFullname;
    }

    public String getFriendUserFullname() {

        return this.friendUserFullname;
    }

    public void setFriendUserPhotoUrl(String friendUserPhoto) {

        this.friendUserPhoto = friendUserPhoto;
    }

    public String getFriendUserPhotoUrl() {

        return this.friendUserPhoto;
    }

    public void setFriendLocation(String friendLocation) {

        this.friendLocation = friendLocation;
    }

    public String getFriendLocation() {

        return this.friendLocation;
    }

    public void setTimeAgo(String ago) {

        this.timeAgo = ago;
    }

    public String getTimeAgo() {

        return this.timeAgo;
    }

    public void setOnline(Boolean online) {

        this.online = online;
    }

    public Boolean isOnline() {

        return this.online;
    }

    public void setPhotoModerateAt(int photoModerateAt) {

        this.photoModerateAt = photoModerateAt;
    }

    public int getPhotoModerateAt() {

        return this.photoModerateAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.friendTo);
        dest.writeLong(this.friendUserId);
        dest.writeInt(this.verify);
        dest.writeInt(this.vip);
        dest.writeInt(this.pro);
        dest.writeString(this.friendUserUsername);
        dest.writeString(this.friendUserFullname);
        dest.writeString(this.friendUserPhoto);
        dest.writeString(this.friendLocation);
        dest.writeString(this.timeAgo);
        dest.writeValue(this.online);
        dest.writeInt(this.photoModerateAt);
    }

    protected Friend(Parcel in) {
        this.id = in.readLong();
        this.friendTo = in.readLong();
        this.friendUserId = in.readLong();
        this.verify = in.readInt();
        this.vip = in.readInt();
        this.pro = in.readInt();
        this.friendUserUsername = in.readString();
        this.friendUserFullname = in.readString();
        this.friendUserPhoto = in.readString();
        this.friendLocation = in.readString();
        this.timeAgo = in.readString();
        this.online = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.photoModerateAt = in.readInt();
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}
