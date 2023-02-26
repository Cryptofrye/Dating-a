package com.raccoonsquare.dating.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import com.raccoonsquare.dating.constants.Constants;

public class Settings extends Application implements Constants, Parcelable {

    private int defaultGhostModeCost = 100, defaultVerifiedBadgeCost = 150, defaultDisableAdsCost = 200, defaultProModeCost = 170, defaultSpotlightCost = 30, defaultMessagesPackageCost = 20;
    private Boolean allowShowNotModeratedProfilePhotos = true;

    public Settings() {

    }

    public void setGhostModeCost(int defaultGhostModeCost) {

        this.defaultGhostModeCost = defaultGhostModeCost;
    }

    public int getGhostModeCost() {

        return this.defaultGhostModeCost;
    }

    public void setVerifiedBadgeCost(int defaultVerifiedBadgeCost) {

        this.defaultVerifiedBadgeCost = defaultVerifiedBadgeCost;
    }

    public int getVerifiedBadgeCost() {

        return this.defaultVerifiedBadgeCost;
    }

    public void setDisableAdsCost(int defaultDisableAdsCost) {

        this.defaultDisableAdsCost = defaultDisableAdsCost;
    }

    public int getDisableAdsCost() {

        return this.defaultDisableAdsCost;
    }

    public void setProModeCost(int defaultProModeCost) {

        this.defaultProModeCost = defaultProModeCost;
    }

    public int getProModeCost() {

        return this.defaultProModeCost;
    }

    public void setSpotlightCost(int defaultSpotlightCost) {

        this.defaultSpotlightCost = defaultSpotlightCost;
    }

    public int getSpotlightCost() {

        return this.defaultSpotlightCost;
    }

    public void setMessagePackageCost(int defaultMessagesPackageCost) {

        this.defaultMessagesPackageCost = defaultMessagesPackageCost;
    }

    public int getMessagePackageCost() {

        return this.defaultMessagesPackageCost;
    }

    public void setAllowShowNotModeratedProfilePhotos(Boolean allowShowNotModeratedProfilePhotos) {

        this.allowShowNotModeratedProfilePhotos = allowShowNotModeratedProfilePhotos;
    }

    public Boolean isAllowShowNotModeratedProfilePhotos() {

        return this.allowShowNotModeratedProfilePhotos;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.defaultGhostModeCost);
        dest.writeInt(this.defaultVerifiedBadgeCost);
        dest.writeInt(this.defaultDisableAdsCost);
        dest.writeInt(this.defaultProModeCost);
        dest.writeInt(this.defaultSpotlightCost);
        dest.writeInt(this.defaultMessagesPackageCost);
        dest.writeBoolean(this.allowShowNotModeratedProfilePhotos);
    }

    protected Settings(Parcel in) {
        this.defaultGhostModeCost = in.readInt();
        this.defaultVerifiedBadgeCost = in.readInt();
        this.defaultDisableAdsCost = in.readInt();
        this.defaultProModeCost = in.readInt();
        this.defaultSpotlightCost = in.readInt();
        this.defaultMessagesPackageCost = in.readInt();
        this.allowShowNotModeratedProfilePhotos = in.readBoolean();
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel source) {
            return new Settings(source);
        }

        @Override
        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };
}
