package com.raccoonsquare.dating;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.raccoonsquare.dating.adapter.GalleryListAdapter;
import com.raccoonsquare.dating.adapter.ProfilesSpotlightListAdapter;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.model.Image;
import com.raccoonsquare.dating.model.Profile;
import com.raccoonsquare.dating.util.CustomRequest;
import com.raccoonsquare.dating.util.Helper;
import com.raccoonsquare.dating.view.SpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MainFragment extends Fragment implements Constants, SwipeRefreshLayout.OnRefreshListener {

    private static final String STATE_LIST = "State Adapter Data";
    private static final String STATE_LIST_2 = "State Adapter Data 2";
    private static final String STATE_LIST_3 = "State Adapter Data 3";

    TextView mMessage;
    ImageView mSplash;

    public CardView mAdCard;
    public NativeAdView mAdView;
    public LinearLayout mAdBannerContainer;
    public ProgressBar mAdProgressBar;

    CardView mSpotlightCard, mFeedCard;
    Button mSpotlightMoreButton;

    RecyclerView mRecyclerView, mSpotlightRecyclerView;
    NestedScrollView mNestedView;

    SwipeRefreshLayout mItemsContainer;

    private TextView mModePanelTitle;
    private SwitchCompat mModeSwitch;

    private ArrayList<Profile> spotlightList;
    private ProfilesSpotlightListAdapter spotlightAdapter;

    private ArrayList<Image> itemsList;
    private GalleryListAdapter itemsAdapter;

    long itemId = 0;
    private int arrayLength = 0;
    private int arrayLengthStream = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean restore = false;

    private Double lat = 39.9199, lng = 32.8543; // Ankara

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Intent i = getActivity().getIntent();

        //itemId = i.getLongExtra("itemId", 0);

        if (savedInstanceState != null) {

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            itemsAdapter = new GalleryListAdapter(getActivity(), itemsList);

            spotlightList = savedInstanceState.getParcelableArrayList(STATE_LIST_2);
            spotlightAdapter = new ProfilesSpotlightListAdapter(getActivity(), spotlightList);

            restore = savedInstanceState.getBoolean("restore");
            itemId = savedInstanceState.getLong("itemId");

        } else {

            itemsList = new ArrayList<Image>();
            itemsAdapter = new GalleryListAdapter(getActivity(), itemsList);

            spotlightList = new ArrayList<Profile>();
            spotlightAdapter = new ProfilesSpotlightListAdapter(getActivity(), spotlightList);

            restore = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        getActivity().setTitle(R.string.app_name);

        mItemsContainer = rootView.findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);

        mSpotlightCard = rootView.findViewById(R.id.spotlight_card);
        mFeedCard = rootView.findViewById(R.id.media_feed_card);

        mSpotlightCard.setVisibility(View.GONE);
        mFeedCard.setVisibility(View.GONE);

        // Ad

        mAdCard = (CardView) rootView.findViewById(R.id.adCard);
        mAdView = (NativeAdView) rootView.findViewById(R.id.ad_native_view);
        mAdBannerContainer = rootView.findViewById(R.id.ad_banner_container);
        mAdProgressBar = (ProgressBar) rootView.findViewById(R.id.ad_progress_bar);

        mAdCard.setVisibility(View.GONE);

        //

        mSpotlightMoreButton = rootView.findViewById(R.id.spotlight_card_button);

        mSpotlightRecyclerView = rootView.findViewById(R.id.spotlight_recycler_view);

        //

        mSpotlightRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mSpotlightRecyclerView.setAdapter(spotlightAdapter);

        spotlightAdapter.setOnItemClickListener(new ProfilesSpotlightListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Profile obj, int position) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", obj.getId());
                startActivity(intent);
            }
        });

        mSpotlightMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SpotlightActivity.class);
                startActivity(intent);
            }
        });

        //

        mMessage = rootView.findViewById(R.id.message);
        mSplash = rootView.findViewById(R.id.splash);

        mNestedView = rootView.findViewById(R.id.nested_view);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);

        int columns = 3;

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), columns);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacingItemDecoration(columns, Helper.dpToPx(getActivity(), 4), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(itemsAdapter);

        mRecyclerView.addOnItemTouchListener(new GalleryListAdapter.RecyclerTouchListener(getActivity(), mRecyclerView, new GalleryListAdapter.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                Image img = itemsList.get(position);

                Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra("itemId", img.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mRecyclerView.setNestedScrollingEnabled(false);

        mNestedView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY < oldScrollY) { // up

                }

                if (scrollY > oldScrollY) { // down

                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (!loadingMore && (viewMore) && !(mItemsContainer.isRefreshing())) {

                        mItemsContainer.setRefreshing(true);

                        loadingMore = true;

                        Log.e("ViewMore", "asd");

                        getItems();
                    }
                }
            }
        });

        // Mode panel

        mModePanelTitle = (TextView) rootView.findViewById(R.id.mode_switch_panel_title);
        mModeSwitch = (SwitchCompat) rootView.findViewById(R.id.mode_switch);

        mModeSwitch.setOnCheckedChangeListener(null);

        if (App.getInstance().getFeedMode() == 1) {

            mModeSwitch.setChecked(true);
            mModePanelTitle.setText(R.string.label_feed_mode_1);

        } else {

            mModeSwitch.setChecked(false);
            mModePanelTitle.setText(R.string.label_feed_mode_0);
        }

        mModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    App.getInstance().setFeedMode(1);
                    mModePanelTitle.setText(R.string.label_feed_mode_1);

                } else {

                    App.getInstance().setFeedMode(0);
                    mModePanelTitle.setText(R.string.label_feed_mode_0);
                }

                App.getInstance().saveData();

                itemId = 0;
                getItems();
            }
        });

        //

        if (!restore) {

            showMessage(getText(R.string.msg_loading_2).toString());

            getItems();

        } else {

            updateView();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateView() {

        hideMessage();

        mSpotlightCard.setVisibility(View.VISIBLE);
        mFeedCard.setVisibility(View.VISIBLE);

        if (spotlightAdapter.getItemCount() == 0) {

            mSpotlightRecyclerView.setVisibility(View.GONE);

        } else {

            mSpotlightRecyclerView.setVisibility(View.VISIBLE);
        }

        // Ad

        mAdCard.setVisibility(View.GONE);
        mAdProgressBar.setVisibility(View.GONE);

        mAdView.setVisibility(View.GONE);

        if (App.getInstance().getAdmob() == ADMOB_DISABLED) {

            mAdCard.setVisibility(View.VISIBLE);
            mAdProgressBar.setVisibility(View.VISIBLE);

            AdLoader.Builder builder = new AdLoader.Builder(getActivity(), App.getInstance().getAdmobSettings().getBannerNativeAdUnitId());

            // OnUnifiedNativeAdLoadedListener implementation.
            builder.forNativeAd(

                    (NativeAd.OnNativeAdLoadedListener) nativeAd -> {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.

                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.

                        mAdView.setMediaView((MediaView) mAdView.findViewById(R.id.ad_media));

                        // Set other ad assets.
                        mAdView.setHeadlineView(mAdView.findViewById(R.id.ad_headline));
                        mAdView.setBodyView(mAdView.findViewById(R.id.ad_body));
                        mAdView.setCallToActionView(mAdView.findViewById(R.id.ad_call_to_action));
                        mAdView.setIconView(mAdView.findViewById(R.id.ad_app_icon));
                        mAdView.setPriceView(mAdView.findViewById(R.id.ad_price));
                        mAdView.setStarRatingView(mAdView.findViewById(R.id.ad_stars));
                        mAdView.setStoreView(mAdView.findViewById(R.id.ad_store));
                        mAdView.setAdvertiserView(mAdView.findViewById(R.id.ad_advertiser));

                        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
                        ((TextView) mAdView.getHeadlineView()).setText(getString(R.string.label_ad) + ": " + nativeAd.getHeadline());
                        mAdView.getMediaView().setMediaContent(nativeAd.getMediaContent());

                        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
                        // check before trying to display them.
                        if (nativeAd.getBody() == null) {

                            mAdView.getBodyView().setVisibility(View.INVISIBLE);

                        } else {

                            mAdView.getBodyView().setVisibility(View.VISIBLE);
                            ((TextView) mAdView.getBodyView()).setText(nativeAd.getBody());
                        }

                        if (nativeAd.getCallToAction() == null) {

                            mAdView.getCallToActionView().setVisibility(View.INVISIBLE);

                        } else {

                            mAdView.getCallToActionView().setVisibility(View.VISIBLE);
                            ((Button) mAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
                        }

                        if (nativeAd.getIcon() == null) {

                            mAdView.getIconView().setVisibility(View.GONE);

                        } else {

                            ((ImageView) mAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
                            mAdView.getIconView().setVisibility(View.VISIBLE);
                        }

                        if (nativeAd.getPrice() == null) {

                            mAdView.getPriceView().setVisibility(View.INVISIBLE);

                        } else {

                            mAdView.getPriceView().setVisibility(View.VISIBLE);
                            ((TextView) mAdView.getPriceView()).setText(nativeAd.getPrice());
                        }

                        if (nativeAd.getStore() == null) {

                            mAdView.getStoreView().setVisibility(View.INVISIBLE);

                        } else {

                            mAdView.getStoreView().setVisibility(View.VISIBLE);
                            ((TextView) mAdView.getStoreView()).setText(nativeAd.getStore());
                        }

                        if (nativeAd.getStarRating() == null) {

                            mAdView.getStarRatingView().setVisibility(View.INVISIBLE);

                        } else {

                            ((RatingBar) mAdView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
                            mAdView.getStarRatingView().setVisibility(View.VISIBLE);
                        }

                        if (nativeAd.getAdvertiser() == null) {

                            mAdView.getAdvertiserView().setVisibility(View.INVISIBLE);

                        } else {

                            ((TextView) mAdView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                            mAdView.getAdvertiserView().setVisibility(View.VISIBLE);
                        }

                        // This method tells the Google Mobile Ads SDK that you have finished populating your
                        // native ad view with this native ad.
                        mAdView.setNativeAd(nativeAd);

                        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
                        // have a video asset.
                        VideoController vc = nativeAd.getMediaContent().getVideoController();

                        // Updates the UI to say whether or not this ad has a video asset.
                        if (vc.hasVideoContent()) {

                            Log.e("admob", "Video status: Ad contains a %.2f:1 video asset.");

                            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
                            // VideoController will call methods on this object when events occur in the video
                            // lifecycle.

                            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                                @Override
                                public void onVideoEnd() {
                                    // Publishers should allow native ads to complete video playback before
                                    // refreshing or replacing them with another ad in the same UI location.

                                    Log.e("admob", "Video status: Video playback has ended.");
                                    super.onVideoEnd();
                                }
                            });

                        } else {

                            Log.e("admob", "Video status: Ad does not contain a video asset.");
                        }
                    });

            VideoOptions videoOptions =
                    new VideoOptions.Builder().setStartMuted(true).build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {

                    String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                    Log.e("admob","Failed to load native ad with error " + error);

                    mAdView.setVisibility(View.GONE);
                    mAdProgressBar.setVisibility(View.GONE);

                    AdView mAdBannerView = new AdView(getActivity());
                    mAdBannerView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                    mAdBannerView.setAdUnitId(App.getInstance().getAdmobSettings().getBannerAdUnitId());
                    mAdBannerContainer.addView(mAdBannerView);

                    try {

                        AdRequest adRequest = new AdRequest.Builder().build();
                        mAdBannerView.loadAd(adRequest);

                    } catch (Exception e) {

                        mAdBannerContainer.removeAllViews();
                        mAdCard.setVisibility(View.GONE);

                        Log.e("Dimon3", e.toString());
                    }
                }

                @Override
                public void onAdLoaded() {

                    Log.e("admob","Ad loaded");
                    Log.e("admob", App.getInstance().getAdmobSettings().getBannerNativeAdUnitId());

                    mAdView.setVisibility(View.VISIBLE);
                    mAdProgressBar.setVisibility(View.GONE);
                }

            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
            mAdCard.setVisibility(View.VISIBLE);

        } else {

            mAdCard.setVisibility(View.GONE);
        }
        
    }

    @Override
    public void onRefresh() {

        if (App.getInstance().isConnected()) {

            itemId = 0;

            getItems();

        } else {

            mItemsContainer.setRefreshing(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
        outState.putLong("itemId", itemId);
        outState.putParcelableArrayList(STATE_LIST, itemsList);
        outState.putParcelableArrayList(STATE_LIST_2, spotlightList);
    }

    public void getItems() {

        mItemsContainer.setRefreshing(true);
        mModeSwitch.setEnabled(false);

        String url = METHOD_GALLERY_FEED;

        if (App.getInstance().getFeedMode() != 1) {

            url = METHOD_GALLERY_STREAM;
        }

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "MainFragment Not Added to Activity");

                            return;
                        }

                        if (!loadingMore) {

                            itemsList.clear();
                        }

                        try {

                            arrayLengthStream = 0;

                            if (!response.getBoolean("error")) {

                                itemId = response.getInt("itemId");

                                if (response.has("items")) {

                                    JSONArray itemsArray = response.getJSONArray("items");

                                    arrayLengthStream = itemsArray.length();

                                    if (arrayLengthStream > 0) {

                                        for (int i = 0; i < itemsArray.length(); i++) {

                                            JSONObject itemObj = (JSONObject) itemsArray.get(i);

                                            Image item = new Image(itemObj);

                                            itemsList.add(item);
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            if (spotlightAdapter.getItemCount() == 0) {

                                getSpotlightItems();
                            }

                            updateView();

                            loadingComplete();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "MainFragment Not Added to Activity");

                    return;
                }

                loadingComplete();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("itemId", Long.toString(itemId));
                params.put("language", App.getInstance().getLanguage());

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(VOLLEY_REQUEST_SECONDS), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonReq.setRetryPolicy(policy);

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void getSpotlightItems() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_SPOTLIGHT_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "MainFragment Not Added to Activity");

                            return;
                        }

                        try {

                            arrayLength = 0;

                            if (!response.getBoolean("error")) {

                                if (response.has("items")) {

                                    JSONArray usersArray = response.getJSONArray("items");

                                    arrayLength = usersArray.length();

                                    if (arrayLength > 0) {

                                        for (int i = 0; i < usersArray.length(); i++) {

                                            JSONObject userObj = (JSONObject) usersArray.get(i);

                                            Profile profile = new Profile(userObj);

                                            spotlightList.add(profile);
                                        }
                                    }
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            spotlightAdapter.notifyDataSetChanged();

                            loadingComplete();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "MainFragment Not Added to Activity");

                    return;
                }

                loadingComplete();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("itemId", Long.toString(0));
                params.put("language", "en");

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(VOLLEY_REQUEST_SECONDS), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonReq.setRetryPolicy(policy);

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete() {

        mModeSwitch.setEnabled(true);

        viewMore = arrayLengthStream == LIST_ITEMS;

        //viewMore = true;

        hideMessage();

        itemsAdapter.notifyDataSetChanged();
        spotlightAdapter.notifyDataSetChanged();

        mSpotlightCard.setVisibility(View.VISIBLE);
        mFeedCard.setVisibility(View.VISIBLE);

        if (spotlightAdapter.getItemCount() == 0) {

            mSpotlightRecyclerView.setVisibility(View.GONE);

        } else {

            mSpotlightRecyclerView.setVisibility(View.VISIBLE);
        }

        loadingMore = false;
        mItemsContainer.setRefreshing(false);
    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);

        mSplash.setVisibility(View.VISIBLE);

        mSpotlightCard.setVisibility(View.GONE);
        mFeedCard.setVisibility(View.GONE);
    }

    public void hideMessage() {

        mMessage.setVisibility(View.GONE);

        mSplash.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_filters);
        item.setVisible(false);

        item = menu.findItem(R.id.action_remove_all);
        item.setVisible(false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            // String message = intent.getStringExtra("message");

            updateView();
        }
    };

    @Override
    public void onResume() {

        super.onResume();

        getActivity().registerReceiver(mMessageReceiver, new IntentFilter(TAG_UPDATE_BADGES));
    }

    @Override
    public void onPause() {

        super.onPause();

        getActivity().unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}