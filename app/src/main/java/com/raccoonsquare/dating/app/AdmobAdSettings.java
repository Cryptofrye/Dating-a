package com.raccoonsquare.dating.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.constants.Constants;

import org.json.JSONObject;

public class AdmobAdSettings extends Application implements Constants {

	public static final String TAG = AdmobAdSettings.class.getSimpleName();

    private SharedPreferences sharedPref;
    private static Resources res;

    private String admob_app_id = "ca-app-pub-3940256099942544~3347511713";
    private String admob_banner_ad_unit_id = "ca-app-pub-3940256099942544/6300978111";
    private String admob_rewarded_ad_unit_id = "ca-app-pub-3940256099942544/5224354917";
    private String admob_interstitial_ad_unit_id = "ca-app-pub-3940256099942544/1033173712";
    private String admob_banner_native_ad_unit_id = "ca-app-pub-3940256099942544/2247696110";

	@Override
	public void onCreate() {

		super.onCreate();

        this.res = getResources();

        sharedPref = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE);
	}

    public void read_from_json(JSONObject jsonData) {

        try {

            if (jsonData.has("android_admob_app_id")) {

                this.setAppId(jsonData.getString("android_admob_app_id"));
            }

            if (jsonData.has("android_admob_banner_ad_unit_id")) {

                this.setBannerAdUnitId(jsonData.getString("android_admob_banner_ad_unit_id"));
            }

            if (jsonData.has("android_admob_rewarded_ad_unit_id")) {

                this.setRewardedAdUnitId(jsonData.getString("android_admob_rewarded_ad_unit_id"));
            }

            if (jsonData.has("android_admob_interstitial_ad_unit_id")) {

                this.setInterstitialAdUnitId(jsonData.getString("android_admob_interstitial_ad_unit_id"));
            }

            if (jsonData.has("android_admob_banner_native_ad_unit_id")) {

                this.setBannerNativeAdUnitId(jsonData.getString("android_admob_banner_native_ad_unit_id"));
            }

        } catch (Throwable t) {

            Log.e("AdmobAdSettings", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.e("AdmobAdSettings", "");
        }
    }

    //

    public void setAppId(String appId) {

        this.admob_app_id = appId;
    }

    public String getAppId() {

        return this.admob_app_id;
    }

    public void setBannerAdUnitId(String bannerAdUnitId) {

        this.admob_banner_ad_unit_id = bannerAdUnitId;
    }

    public String getBannerAdUnitId() {

        return this.admob_banner_ad_unit_id;
    }

    public void setRewardedAdUnitId(String rewardedAdUnitId) {

        this.admob_rewarded_ad_unit_id = rewardedAdUnitId;
    }

    public String getRewardedAdUnitId() {

        return this.admob_rewarded_ad_unit_id;
    }

    public void setInterstitialAdUnitId(String interstitialAdUnitId) {

        this.admob_interstitial_ad_unit_id = interstitialAdUnitId;
    }

    public String getInterstitialAdUnitId() {

        return this.admob_interstitial_ad_unit_id;
    }

    public void setBannerNativeAdUnitId(String nativeAdUnitId) {

        this.admob_banner_native_ad_unit_id = nativeAdUnitId;
    }

    public String getBannerNativeAdUnitId() {

        return this.admob_banner_native_ad_unit_id;
    }
}