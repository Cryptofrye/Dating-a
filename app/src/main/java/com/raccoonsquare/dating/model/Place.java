package com.raccoonsquare.dating.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.raccoonsquare.dating.constants.Constants;

import org.json.JSONObject;

public class Place extends Application implements Constants, Parcelable {

    private long id;
    private Double lat = 0.0, lng = 0.0;
    private String title;

    public Place() {

    }

    public Place(JSONObject jsonData) {

        try {

            this.setId(jsonData.getLong("id"));
            this.setTitle(jsonData.getString("city"));
            this.setLat(jsonData.getDouble("lat"));
            this.setLng(jsonData.getDouble("lng"));

        } catch (Throwable t) {

            Log.e("Place", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Place", jsonData.toString());
        }
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public void setLat(Double lat) {

        this.lat = lat;
    }

    public Double getLat() {

        if (this.lat == null) {

            this.lat = 0.000000;
        }

        return this.lat;
    }

    public void setLng(Double lng) {

        this.lng = lng;
    }

    public Double getLng() {

        if (this.lng == null) {

            this.lng = 0.000000;
        }

        return this.lng;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getTitle() {

        return this.title;
    }

    protected Place(Parcel in) {
        id = in.readLong();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lng);
        }
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
