package com.raccoonsquare.dating.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.constants.Constants;

import org.json.JSONObject;

public class InterstitialAdSettings extends Application implements Constants {

	public static final String TAG = InterstitialAdSettings.class.getSimpleName();

    private SharedPreferences sharedPref;
    private static Resources res;

    private int interstitialAdAfterProfileView = 2, interstitialAdAfterNewProfileLike = 2, interstitialAdAfterNewGalleryItem = 2, interstitialAdAfterNewComment = 2, interstitialAdAfterNewLike = 2;
    private int currentInterstitialAdAfterProfileView = 0, currentInterstitialAdAfterNewProfileLike = 0, currentInterstitialAdAfterNewGalleryItem = 0, currentInterstitialAdAfterNewComment = 0, currentInterstitialAdAfterNewLike = 0;

	@Override
	public void onCreate() {

		super.onCreate();

        this.res = getResources();

        sharedPref = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE);
	}

    public void read_from_json(JSONObject jsonData) {

        try {

            if (jsonData.has("interstitialAdAfterProfileView")) {

                this.setInterstitialAdAfterProfileView(jsonData.getInt("interstitialAdAfterProfileView"));
            }

            if (jsonData.has("interstitialAdAfterNewGalleryItem")) {

                this.setInterstitialAdAfterNewGalleryItem(jsonData.getInt("interstitialAdAfterNewGalleryItem"));
            }

            if (jsonData.has("interstitialAdAfterNewProfileLike")) {

                this.setInterstitialAdAfterNewProfileLike(jsonData.getInt("interstitialAdAfterNewProfileLike"));
            }

            if (jsonData.has("interstitialAdAfterNewLike")) {

                this.setInterstitialAdAfterNewLike(jsonData.getInt("interstitialAdAfterNewLike"));
            }

            if (jsonData.has("interstitialAdAfterNewComment")) {

                this.setInterstitialAdAfterNewComment(jsonData.getInt("interstitialAdAfterNewComment"));
            }

        } catch (Throwable t) {

            Log.e("InterstitialAdSettings", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.e("InterstitialAdSettings", "");
        }
    }

    //

    public void setInterstitialAdAfterNewProfileLike(int interstitialAdAfterNewProfileLike) {

        this.interstitialAdAfterNewProfileLike = interstitialAdAfterNewProfileLike;
    }

    public int getInterstitialAdAfterNewProfileLike() {

        return this.interstitialAdAfterNewProfileLike;
    }

    public void setCurrentInterstitialAdAfterNewProfileLike(int currentInterstitialAdAfterNewProfileLike) {

        this.currentInterstitialAdAfterNewProfileLike = currentInterstitialAdAfterNewProfileLike;
    }

    public int getCurrentInterstitialAdAfterNewProfileLike() {

        return this.currentInterstitialAdAfterNewProfileLike;
    }

    //

    public void setInterstitialAdAfterProfileView(int interstitialAdAfterProfileView) {

        this.interstitialAdAfterProfileView = interstitialAdAfterProfileView;
    }

    public int getInterstitialAdAfterProfileView() {

        return this.interstitialAdAfterProfileView;
    }

    public void setCurrentInterstitialAdAfterProfileView(int currentInterstitialAdAfterProfileView) {

        this.currentInterstitialAdAfterProfileView = currentInterstitialAdAfterProfileView;
    }

    public int getCurrentInterstitialAdAfterProfileView() {

        return this.currentInterstitialAdAfterProfileView;
    }

    //

    public void setInterstitialAdAfterNewGalleryItem(int interstitialAdAfterNewGalleryItem) {

        this.interstitialAdAfterNewGalleryItem = interstitialAdAfterNewGalleryItem;
    }

    public int getInterstitialAdAfterNewGalleryItem() {

        return this.interstitialAdAfterNewGalleryItem;
    }

    public void setCurrentInterstitialAdAfterNewGalleryItem(int currentInterstitialAdAfterNewGalleryItem) {

        this.currentInterstitialAdAfterNewGalleryItem = currentInterstitialAdAfterNewGalleryItem;
    }

    public int getCurrentInterstitialAdAfterNewGalleryItem() {

        return this.currentInterstitialAdAfterNewGalleryItem;
    }

    //

    public void setInterstitialAdAfterNewComment(int interstitialAdAfterNewComment) {

        this.interstitialAdAfterNewComment = interstitialAdAfterNewComment;
    }

    public int getInterstitialAdAfterNewComment() {

        return this.interstitialAdAfterNewComment;
    }

    public void setCurrentInterstitialAdAfterNewComment(int currentInterstitialAdAfterNewComment) {

        this.currentInterstitialAdAfterNewComment = currentInterstitialAdAfterNewComment;
    }

    public int getCurrentInterstitialAdAfterNewComment() {

        return this.currentInterstitialAdAfterNewComment;
    }

    //

    public void setInterstitialAdAfterNewLike(int interstitialAdAfterNewLike) {

        this.interstitialAdAfterNewLike = interstitialAdAfterNewLike;
    }

    public int getInterstitialAdAfterNewLike() {

        return this.interstitialAdAfterNewLike;
    }

    public void setCurrentInterstitialAdAfterNewLike(int currentInterstitialAdAfterNewLike) {

        this.currentInterstitialAdAfterNewLike = currentInterstitialAdAfterNewLike;
    }

    public int getCurrentInterstitialAdAfterNewLike() {

        return this.currentInterstitialAdAfterNewLike;
    }
}