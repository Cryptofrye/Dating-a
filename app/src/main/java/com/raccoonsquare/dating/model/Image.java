package com.raccoonsquare.dating.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.raccoonsquare.dating.constants.Constants;

import org.json.JSONObject;


public class Image extends Application implements Constants, Parcelable {

    private long id;
    private int createAt, likesCount, commentsCount, accessMode;
    private String timeAgo, date, comment, imgUrl, previewImgUrl, originImgUrl, area, country, city;
    private String videoUrl, previewVideoImgUrl;
    private int itemType;
    private Double lat = 0.000000, lng = 0.000000;
    private Boolean myLike = false;

    private int removeAt = 0, moderateAt = 0;

    private Profile owner;

    public Image() {

    }

    public Image(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setAccessMode(jsonData.getInt("accessMode"));
                this.setItemType(jsonData.getInt("itemType"));
                this.setComment(jsonData.getString("comment"));
                this.setVideoUrl(jsonData.getString("videoUrl"));
                this.setPreviewVideoImgUrl(jsonData.getString("previewVideoImgUrl"));
                this.setImgUrl(jsonData.getString("imgUrl"));
                this.setPreviewImgUrl(jsonData.getString("previewImgUrl"));
                this.setOriginImgUrl(jsonData.getString("originImgUrl"));
                this.setArea(jsonData.getString("area"));
                this.setCountry(jsonData.getString("country"));
                this.setCity(jsonData.getString("city"));
                this.setCommentsCount(jsonData.getInt("commentsCount"));
                this.setLikesCount(jsonData.getInt("likesCount"));
                this.setLat(jsonData.getDouble("lat"));
                this.setLng(jsonData.getDouble("lng"));
                this.setCreateAt(jsonData.getInt("createAt"));
                this.setDate(jsonData.getString("date"));
                this.setTimeAgo(jsonData.getString("timeAgo"));

                this.setMyLike(jsonData.getBoolean("myLike"));

                if (jsonData.has("removeAt")) {

                    this.setRemoveAt(jsonData.getInt("removeAt"));
                }

                if (jsonData.has("moderateAt")) {

                    this.setModerateAt(jsonData.getInt("moderateAt"));
                }

                if (jsonData.has("owner")) {

                    JSONObject ownerObj = (JSONObject) jsonData.getJSONObject("owner");

                    this.setOwner(new Profile(ownerObj));
                }
            }

        } catch (Throwable t) {

            Log.e("Gallery Item", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Gallery Item", jsonData.toString());
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAccessMode() {

        return accessMode;
    }

    public void setAccessMode(int accessMode) {

        this.accessMode = accessMode;
    }

    public int getItemType() {

        return itemType;
    }

    public void setItemType(int itemType) {

        this.itemType = itemType;
    }

    public int getCommentsCount() {

        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getLikesCount() {

        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCreateAt() {

        return createAt;
    }

    public void setCreateAt(int createAt) {

        this.createAt = createAt;
    }

    public int getRemoveAt() {

        return removeAt;
    }

    public void setRemoveAt(int removeAt) {

        this.removeAt = removeAt;
    }

    public int getModerateAt() {

        return moderateAt;
    }

    public void setModerateAt(int moderateAt) {

        this.moderateAt = moderateAt;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getVideoUrl() {

        if (this.videoUrl == null) {

            this.videoUrl = "";
        }

        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {

        if (API_DOMAIN.equals("http://10.0.2.2/")) {

            this.videoUrl = videoUrl.replace("http://localhost/","http://10.0.2.2/");

        } else {

            this.videoUrl = videoUrl;
        }
    }

    public String getPreviewVideoImgUrl() {

        return previewVideoImgUrl;
    }

    public void setPreviewVideoImgUrl(String previewVideoImgUrl) {

        this.previewVideoImgUrl = previewVideoImgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {

        if (API_DOMAIN.equals("http://10.0.2.2/")) {

            this.imgUrl = imgUrl.replace("http://localhost/","http://10.0.2.2/");

        } else {

            this.imgUrl = imgUrl;
        }
    }

    public String getPreviewImgUrl() {
        return previewImgUrl;
    }

    public void setPreviewImgUrl(String previewImgUrl) {

        if (API_DOMAIN.equals("http://10.0.2.2/")) {

            this.previewImgUrl = previewImgUrl.replace("http://localhost/","http://10.0.2.2/");

        } else {

            this.previewImgUrl = previewImgUrl;
        }
    }

    public String getOriginImgUrl() {
        return originImgUrl;
    }

    public void setOriginImgUrl(String originImgUrl) {
        this.originImgUrl = originImgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {

        if (this.area == null) {

            this.area = "";
        }

        return this.area;
    }

    public void setArea(String area) {

        this.area = area;
    }

    public String getCountry() {

        if (this.country == null) {

            this.country = "";
        }

        return this.country;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    public String getCity() {

        if (this.city == null) {

            this.city = "";
        }

        return this.city;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public Double getLat() {

        return this.lat;
    }

    public void setLat(Double lat) {

        this.lat = lat;
    }

    public Double getLng() {

        return this.lng;
    }

    public void setLng(Double lng) {

        this.lng = lng;
    }

    public String getLink() {

        return WEB_SITE + this.owner.getUsername() + "/gallery/" + this.getId();
    }

    public Boolean isMyLike() {
        return myLike;
    }

    public void setMyLike(Boolean myLike) {

        this.myLike = myLike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.createAt);
        dest.writeInt(this.likesCount);
        dest.writeInt(this.commentsCount);
        dest.writeInt(this.accessMode);
        dest.writeString(this.timeAgo);
        dest.writeString(this.date);
        dest.writeString(this.comment);
        dest.writeString(this.imgUrl);
        dest.writeString(this.previewImgUrl);
        dest.writeString(this.originImgUrl);
        dest.writeString(this.area);
        dest.writeString(this.country);
        dest.writeString(this.city);
        dest.writeString(this.videoUrl);
        dest.writeString(this.previewVideoImgUrl);
        dest.writeInt(this.itemType);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeValue(this.myLike);
        dest.writeInt(this.removeAt);
        dest.writeInt(this.moderateAt);
        dest.writeParcelable(this.owner, flags);
    }

    protected Image(Parcel in) {
        this.id = in.readLong();
        this.createAt = in.readInt();
        this.likesCount = in.readInt();
        this.commentsCount = in.readInt();
        this.accessMode = in.readInt();
        this.timeAgo = in.readString();
        this.date = in.readString();
        this.comment = in.readString();
        this.imgUrl = in.readString();
        this.previewImgUrl = in.readString();
        this.originImgUrl = in.readString();
        this.area = in.readString();
        this.country = in.readString();
        this.city = in.readString();
        this.videoUrl = in.readString();
        this.previewVideoImgUrl = in.readString();
        this.itemType = in.readInt();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.myLike = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.removeAt = in.readInt();
        this.moderateAt = in.readInt();
        this.owner = (Profile) in.readParcelable(Profile.class.getClassLoader());
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
