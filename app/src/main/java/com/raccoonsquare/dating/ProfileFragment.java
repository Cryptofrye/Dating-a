package com.raccoonsquare.dating;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.android.volley.toolbox.ImageLoader;
import com.balysv.materialripple.MaterialRippleLayout;
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
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.raccoonsquare.dating.adapter.FeelingsListAdapter;
import com.raccoonsquare.dating.adapter.FriendsSpotlightListAdapter;
import com.raccoonsquare.dating.adapter.GalleryListAdapter;
import com.raccoonsquare.dating.adapter.GiftsSelectListAdapter;
import com.raccoonsquare.dating.adapter.GiftsSpotlightListAdapter;
import com.raccoonsquare.dating.adapter.ProfilesSpotlightListAdapter;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.dialogs.ProfileBlockDialog;
import com.raccoonsquare.dating.dialogs.ProfileReportDialog;
import com.raccoonsquare.dating.model.BaseGift;
import com.raccoonsquare.dating.model.Feeling;
import com.raccoonsquare.dating.model.Friend;
import com.raccoonsquare.dating.model.Gift;
import com.raccoonsquare.dating.model.Image;
import com.raccoonsquare.dating.model.Profile;
import com.raccoonsquare.dating.util.Api;
import com.raccoonsquare.dating.util.CustomRequest;
import com.raccoonsquare.dating.util.Helper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment implements Constants, SwipeRefreshLayout.OnRefreshListener {

    private static final int PROFILE_NEW_GIFT = 30001;

    private static final String STATE_LIST = "State Adapter Data";
    private static final String STATE_LIKES_SPOTLIGHT_LIST = "State Adapter Data 2";
    private static final String STATE_FRIENDS_SPOTLIGHT_LIST = "State Adapter Data 3";
    private static final String STATE_GIFTS_SPOTLIGHT_LIST = "State Adapter Data 4";

    private ProgressDialog pDialog;

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private static final int SELECT_PHOTO = 1;
    private static final int SELECT_COVER = 2;
    private static final int PROFILE_EDIT = 3;
    private static final int PROFILE_NEW_POST = 4;
    private static final int CREATE_PHOTO = 5;
    private static final int CREATE_COVER = 6;
    private static final int PROFILE_CHAT = 7;
    private static final int PROFILE_FEELINGS = 8;

    public CardView mAdCard;
    public NativeAdView mAdView;
    public LinearLayout mAdBannerContainer;
    public ProgressBar mAdProgressBar;

    Button mFriendsSpotlightMoreButton, mGiftsSpotlightMoreButton, mLikesSpotlightMoreButton;
    TextView mFriendsSpotlightTitle, mGiftsSpotlightTitle, mLikesSpotlightTitle;
    CardView mFriendsSpotlight, mGiftsSpotlight, mLikesSpotlight;
    RecyclerView mFriendsSpotlightRecyclerView, mGiftsSpotlightRecyclerView, mLikesSpotlightRecyclerView;

    private ArrayList<Profile> likesSpotlightList;
    private ProfilesSpotlightListAdapter likesSpotlightAdapter;

    private ArrayList<Friend> friendsSpotlightList;
    private FriendsSpotlightListAdapter friendsSpotlightAdapter;

    private ArrayList<Gift> giftsSpotlightList;
    private GiftsSpotlightListAdapter giftsSpotlightAdapter;

    private int mAccountAction = 0; // 0 = choicePhoto, 1 = choiceCover

    RelativeLayout mProfileLoadingScreen, mProfileErrorScreen, mProfileDisabledScreen;

    LinearLayout mLocationContainer, mProfileInfoContainer, mProfileCountersContainer;

    LinearLayout mProfileSexOrientationContainer, mProfileAgeContainer, mProfileHeightContainer, mProfileWeightContainer, mProfileStatusContainer, mProfileJoinDateContainer, mProfileBirthDateContainer, mProfileGenderContainer, mProfileRelationshipStatusContainer, mProfilePoliticalViewsContainer, mProfileWorldViewContainer, mProfilePersonalPriorityContainer, mProfileImportantInOthersContainer, mProfileFacebookContainer, mProfileSiteContainer;
    LinearLayout mProfileSmokingViewsContainer, mProfileAlcoholViewsContainer, mProfileProfileLookingContainer, mProfileGenderLikeContainer;
    TextView mProfileSmokingViews, mProfileAlcoholViews, mProfileProfileLooking, mProfileGenderLike;
    TextView mProfileSexOrientation, mProfileAge, mProfileHeight, mProfileWeight, mProfileStatus, mProfileJoinDate, mProfileBirthDate, mProfileGender, mProfileRelationshipStatus, mProfilePoliticalViews, mProfileWorldView, mProfilePersonalPriority, mProfileImportantInOthers, mProfileFacebookUrl, mProfileSiteUrl;

    SwipeRefreshLayout mProfileRefreshLayout;
    NestedScrollView mNestedScrollView;

    CircularImageView mProfilePhoto, mProfileIcon, mProfileProIcon, mFeelingIcon;
    ImageView mProfileCover, mProfileOnlineIcon, mProfileSexOrientationImage;
    TextView mProfileLocation, mProfileFullname, mProfileUsername;
    RecyclerView mRecyclerView;
    TextView mProfileItemsCount, mProfileFriendsCount, mProfileLikesCount, mProfileGiftsCount;
    MaterialRippleLayout mProfileItemsBtn, mProfileFriendsBtn, mProfileLikesBtn, mProfileGiftsBtn;

    Button mProfileMessageBtn, mProfileActionBtn;

    Toolbar mToolbar;

    Profile profile;

    private ArrayList<Image> itemsList;

    private GalleryListAdapter itemsAdapter;

    private Uri selectedImage;

    private String selectedImagePath = "", newImageFileName = "";

    private Boolean loadingComplete = false;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;

    private String profile_mention;
    public long profile_id;
    int itemId = 0;
    int arrayLength = 0;
    int accessMode = 0;

    private Boolean loading = false;
    private Boolean restore = false;
    private Boolean preload = false;

    private Boolean isMainScreen = false;

    //

    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<String[]> storagePermissionLauncher;
    private ActivityResultLauncher<Intent> imgFromGalleryActivityResultLauncher;
    private ActivityResultLauncher<Intent> imgFromCameraActivityResultLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);

        initpDialog();

        Intent i = getActivity().getIntent();

        profile_id = i.getLongExtra("profileId", 0);
        profile_mention = i.getStringExtra("profileMention");

        if (profile_id == 0 && (profile_mention == null || profile_mention.length() == 0)) {

            profile_id = App.getInstance().getId();
            isMainScreen = true;
        }

        profile = new Profile();
        profile.setId(profile_id);

        itemsList = new ArrayList<>();
        itemsAdapter = new GalleryListAdapter(getActivity(), itemsList);

        likesSpotlightList = new ArrayList<Profile>();
        likesSpotlightAdapter = new ProfilesSpotlightListAdapter(getActivity(), likesSpotlightList);

        friendsSpotlightList = new ArrayList<Friend>();
        friendsSpotlightAdapter = new FriendsSpotlightListAdapter(getActivity(), friendsSpotlightList);

        giftsSpotlightList = new ArrayList<Gift>();
        giftsSpotlightAdapter = new GiftsSpotlightListAdapter(getActivity(), giftsSpotlightList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        if (savedInstanceState != null) {

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            itemsAdapter = new GalleryListAdapter(getActivity(), itemsList);

            likesSpotlightList = savedInstanceState.getParcelableArrayList(STATE_LIKES_SPOTLIGHT_LIST);
            likesSpotlightAdapter = new ProfilesSpotlightListAdapter(getActivity(), likesSpotlightList);

            friendsSpotlightList = savedInstanceState.getParcelableArrayList(STATE_FRIENDS_SPOTLIGHT_LIST);
            friendsSpotlightAdapter = new FriendsSpotlightListAdapter(getActivity(), friendsSpotlightList);

            giftsSpotlightList = savedInstanceState.getParcelableArrayList(STATE_GIFTS_SPOTLIGHT_LIST);
            giftsSpotlightAdapter = new GiftsSpotlightListAdapter(getActivity(), giftsSpotlightList);

            itemId = savedInstanceState.getInt("itemId");

            restore = savedInstanceState.getBoolean("restore");
            loading = savedInstanceState.getBoolean("loading");
            preload = savedInstanceState.getBoolean("preload");

            profile = savedInstanceState.getParcelable("profileObj");

        } else {

            itemsList = new ArrayList<>();
            itemsAdapter = new GalleryListAdapter(getActivity(), itemsList);

            likesSpotlightList = new ArrayList<Profile>();
            likesSpotlightAdapter = new ProfilesSpotlightListAdapter(getActivity(), likesSpotlightList);

            friendsSpotlightList = new ArrayList<Friend>();
            friendsSpotlightAdapter = new FriendsSpotlightListAdapter(getActivity(), friendsSpotlightList);

            giftsSpotlightList = new ArrayList<Gift>();
            giftsSpotlightAdapter = new GiftsSpotlightListAdapter(getActivity(), giftsSpotlightList);

            itemId = 0;

            restore = false;
            loading = false;
            preload = false;
        }

        if (loading) {


            showpDialog();
        }

        //

        imgFromGalleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {

                    // The document selected by the user won't be returned in the intent.
                    // Instead, a URI to that document will be contained in the return intent
                    // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

                    if (result.getData() != null) {

                        selectedImage = result.getData().getData();

                        newImageFileName = Helper.randomString(6) + ".jpg";

                        Helper helper = new Helper(App.getInstance().getApplicationContext());
                        helper.saveImg(selectedImage, newImageFileName);

                        selectedImagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + newImageFileName;

                        File f = new File(selectedImagePath);

                        uploadFile(f, mAccountAction);
                    }
                }
            }
        });

        imgFromCameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {

                    selectedImagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + newImageFileName;

                    File f = new File(selectedImagePath);

                    uploadFile(f, mAccountAction);
                }
            }
        });

        storagePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {

            boolean granted = false;
            String storage_permission = Manifest.permission.READ_EXTERNAL_STORAGE;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {

                storage_permission = Manifest.permission.READ_MEDIA_IMAGES;
            }

            for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {

                if (x.getKey().equals(storage_permission)) {

                    if (x.getValue()) {

                        granted = true;
                    }
                }
            }

            if (granted) {

                Log.e("Permissions", "granted");

                choiceImage();

            } else {

                Log.e("Permissions", "denied");

                Snackbar.make(getView(), getString(R.string.label_no_storage_permission) , Snackbar.LENGTH_LONG).setAction(getString(R.string.action_settings), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + App.getInstance().getPackageName()));
                        startActivity(appSettingsIntent);

                        Toast.makeText(getActivity(), getString(R.string.label_grant_storage_permission), Toast.LENGTH_SHORT).show();
                    }

                }).show();
            }

        });

        //

        cameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

            if (isGranted) {

                // Permission is granted
                Log.e("Permissions", "Permission is granted");

                choiceImage();

            } else {

                // Permission is denied

                Log.e("Permissions", "denied");

                Snackbar.make(getView(), getString(R.string.label_no_camera_permission) , Snackbar.LENGTH_LONG).setAction(getString(R.string.action_settings), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + App.getInstance().getPackageName()));
                        startActivity(appSettingsIntent);

                        Toast.makeText(getActivity(), getString(R.string.label_grant_camera_permission), Toast.LENGTH_SHORT).show();
                    }

                }).show();
            }
        });

        // Ad

        mAdCard = (CardView) rootView.findViewById(R.id.adCard);
        mAdView = (NativeAdView) rootView.findViewById(R.id.ad_native_view);
        mAdBannerContainer = rootView.findViewById(R.id.ad_banner_container);
        mAdProgressBar = (ProgressBar) rootView.findViewById(R.id.ad_progress_bar);

        mAdCard.setVisibility(View.GONE);

        //

        mProfileRefreshLayout = rootView.findViewById(R.id.profileRefreshLayout);
        mProfileRefreshLayout.setOnRefreshListener(this);

        mProfileLoadingScreen = rootView.findViewById(R.id.profileLoadingScreen);
        mProfileErrorScreen = rootView.findViewById(R.id.profileErrorScreen);
        mProfileDisabledScreen = rootView.findViewById(R.id.profileDisabledScreen);

        mProfileInfoContainer = rootView.findViewById(R.id.profileInfoContainer);
        mProfileCountersContainer = rootView.findViewById(R.id.profileCountersContainer);

        mProfileSexOrientationContainer = rootView.findViewById(R.id.profileSexOrientationContainer);
        mProfileAgeContainer = rootView.findViewById(R.id.profileAgeContainer);
        mProfileHeightContainer = rootView.findViewById(R.id.profileHeightContainer);
        mProfileWeightContainer = rootView.findViewById(R.id.profileWeightContainer);

        mProfileSexOrientation = rootView.findViewById(R.id.profileSexOrientation);
        mProfileAge = rootView.findViewById(R.id.profileAge);
        mProfileHeight = rootView.findViewById(R.id.profileHeight);
        mProfileWeight = rootView.findViewById(R.id.profileWeight);

        mProfileSexOrientationImage = rootView.findViewById(R.id.profileSexOrientationImage);

        mProfileStatusContainer = rootView.findViewById(R.id.profileStatusContainer);
        mProfileJoinDateContainer = rootView.findViewById(R.id.profileJoinDateContainer);
        mProfileBirthDateContainer = rootView.findViewById(R.id.profileBirthDateContainer);
        mProfileGenderContainer = rootView.findViewById(R.id.profileGenderContainer);
        mProfileRelationshipStatusContainer = rootView.findViewById(R.id.profileRelationshipStatusContainer);
        mProfilePoliticalViewsContainer = rootView.findViewById(R.id.profilePoliticalViewsContainer);
        mProfileWorldViewContainer = rootView.findViewById(R.id.profileWorldViewContainer);
        mProfilePersonalPriorityContainer = rootView.findViewById(R.id.profilePersonalPriorityContainer);
        mProfileImportantInOthersContainer = rootView.findViewById(R.id.profileImportantInOthersContainer);
        mProfileSmokingViewsContainer = rootView.findViewById(R.id.profileSmokingViewsContainer);
        mProfileAlcoholViewsContainer = rootView.findViewById(R.id.profileAlcoholViewsContainer);
        mProfileProfileLookingContainer = rootView.findViewById(R.id.profileProfileLookingContainer);
        mProfileGenderLikeContainer = rootView.findViewById(R.id.profileGenderLikeContainer);

        mProfileFacebookContainer = rootView.findViewById(R.id.profileFacebookContainer);
        mProfileSiteContainer = rootView.findViewById(R.id.profileSiteContainer);

        mProfileStatus = rootView.findViewById(R.id.profileStatus);
        mProfileJoinDate = rootView.findViewById(R.id.profileJoinDate);
        mProfileBirthDate = rootView.findViewById(R.id.profileBirthDate);
        mProfileGender = rootView.findViewById(R.id.profileGender);
        mProfileRelationshipStatus = rootView.findViewById(R.id.profileRelationshipStatus);
        mProfilePoliticalViews = rootView.findViewById(R.id.profilePoliticalViews);
        mProfileWorldView = rootView.findViewById(R.id.profileWorldView);
        mProfilePersonalPriority = rootView.findViewById(R.id.profilePersonalPriority);
        mProfileImportantInOthers = rootView.findViewById(R.id.profileImportantInOthers);
        mProfileSmokingViews = rootView.findViewById(R.id.profileSmokingViews);
        mProfileAlcoholViews = rootView.findViewById(R.id.profileAlcoholViews);
        mProfileProfileLooking = rootView.findViewById(R.id.profileProfileLooking);
        mProfileGenderLike = rootView.findViewById(R.id.profileGenderLike);

        mProfileFacebookUrl = rootView.findViewById(R.id.profileFacebookUrl);
        mProfileSiteUrl = rootView.findViewById(R.id.profileSiteUrl);

        ((ProfileActivity)getActivity()).mFabButton.hide();

        // Start prepare Friends Spotlight

        mFriendsSpotlightTitle = rootView.findViewById(R.id.friendsSpotlightTitle);
        mFriendsSpotlightMoreButton = rootView.findViewById(R.id.friendsSpotlightMoreBtn);
        mFriendsSpotlight = rootView.findViewById(R.id.friendsSpotlight);
        mFriendsSpotlightRecyclerView = rootView.findViewById(R.id.friendsSpotlightRecyclerView);

        mFriendsSpotlight.setVisibility(View.GONE);

        mFriendsSpotlightRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mFriendsSpotlightRecyclerView.setAdapter(friendsSpotlightAdapter);

        friendsSpotlightAdapter.setOnItemClickListener(new FriendsSpotlightListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Friend obj, int position) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", obj.getFriendUserId());
                startActivity(intent);
            }
        });

        mFriendsSpotlightMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showProfileFriends(profile.getId());
            }
        });

        // Start prepare Gifts Spotlight

        mGiftsSpotlightTitle = rootView.findViewById(R.id.giftsSpotlightTitle);
        mGiftsSpotlightMoreButton = rootView.findViewById(R.id.giftsSpotlightMoreBtn);
        mGiftsSpotlight = rootView.findViewById(R.id.giftsSpotlight);
        mGiftsSpotlightRecyclerView = rootView.findViewById(R.id.giftsSpotlightRecyclerView);

        mGiftsSpotlight.setVisibility(View.GONE);

        mGiftsSpotlightRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mGiftsSpotlightRecyclerView.setAdapter(giftsSpotlightAdapter);

        giftsSpotlightAdapter.setOnItemClickListener(new GiftsSpotlightListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Gift obj, int position) {

                showProfileGifts(profile.getId());
            }
        });

        mGiftsSpotlightMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showProfileGifts(profile.getId());
            }
        });

        // END Gifts Spotlight

        // Start prepare Likes Spotlight

        mLikesSpotlightTitle = rootView.findViewById(R.id.likesSpotlightTitle);
        mLikesSpotlightMoreButton = rootView.findViewById(R.id.likesSpotlightMoreBtn);
        mLikesSpotlight = rootView.findViewById(R.id.likesSpotlight);
        mLikesSpotlightRecyclerView = rootView.findViewById(R.id.likesSpotlightRecyclerView);

        mLikesSpotlight.setVisibility(View.GONE);

        mLikesSpotlightRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mLikesSpotlightRecyclerView.setAdapter(likesSpotlightAdapter);

        likesSpotlightAdapter.setOnItemClickListener(new ProfilesSpotlightListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Profile obj, int position) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", obj.getId());
                startActivity(intent);
            }
        });

        mLikesSpotlightMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showProfileLikes(profile.getId());
            }
        });

        // END Gifts Spotlight


        mNestedScrollView = rootView.findViewById(R.id.nestedScrollView);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Helper.getGalleryGridCount(getActivity()));
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setPadding(2, 2, 2, 2);

        mRecyclerView.setAdapter(itemsAdapter);

        mRecyclerView.setNestedScrollingEnabled(false);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (!loadingMore && (viewMore) && !(mProfileRefreshLayout.isRefreshing())) {

                        mProfileRefreshLayout.setRefreshing(true);

                        loadingMore = true;

                        getItems();
                    }
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new FriendsFragment.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Image img = itemsList.get(position);

                Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra("itemId", img.getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

        mProfileActionBtn = rootView.findViewById(R.id.profileActionBtn);
        mProfileMessageBtn = rootView.findViewById(R.id.profileMessageBtn);

        mProfileFullname = rootView.findViewById(R.id.profileFullname);
        mProfileUsername = rootView.findViewById(R.id.profileUsername);

        mProfileOnlineIcon = rootView.findViewById(R.id.profileOnlineIcon);

        mProfileItemsCount = rootView.findViewById(R.id.profileItemsCount);
        mProfileFriendsCount = rootView.findViewById(R.id.profileFriendsCount);
        mProfileLikesCount = rootView.findViewById(R.id.profileLikesCount);
        mProfileGiftsCount = rootView.findViewById(R.id.profileGiftsCount);

        mProfileItemsBtn = rootView.findViewById(R.id.profileItemsBtn);
        mProfileFriendsBtn = rootView.findViewById(R.id.profileFriendsBtn);
        mProfileLikesBtn = rootView.findViewById(R.id.profileLikesBtn);
        mProfileGiftsBtn = rootView.findViewById(R.id.profileGiftsBtn);

        mLocationContainer = rootView.findViewById(R.id.profileLocationContainer);
        mProfileLocation = rootView.findViewById(R.id.profileLocation);

        mProfileFacebookContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!profile.getFacebookPage().startsWith("https://") && !profile.getFacebookPage().startsWith("http://")){

                    profile.setFacebookPage("http://" + profile.getFacebookPage());
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(profile.getFacebookPage()));
                startActivity(i);
            }
        });

        mProfileSiteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!profile.getInstagramPage().startsWith("https://") && !profile.getInstagramPage().startsWith("http://")){

                    profile.setInstagramPage("http://" + profile.getInstagramPage());
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(profile.getInstagramPage()));
                startActivity(i);
            }
        });

        mProfileFriendsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showProfileFriends(profile.getId());
            }
        });

        mProfileLikesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showProfileLikes(profile.getId());
            }
        });

        mProfilePhoto = rootView.findViewById(R.id.profilePhoto);
        mProfileIcon = rootView.findViewById(R.id.profileIcon);
        mProfileProIcon = rootView.findViewById(R.id.profileProIcon);
        mFeelingIcon = rootView.findViewById(R.id.feelingIcon);
        mProfileCover = rootView.findViewById(R.id.profileCover);

        mProfileActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getId() == profile.getId()) {

                    getAccountSettings();

                } else {

                    if (profile.isFriend()) {

                        removeFromFriends();

                    } else {

                        friendsRequest();
                    }
                }
            }
        });

        ((ProfileActivity)getActivity()).mFabButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (profile.getId() == App.getInstance().getId()) {

                    Intent intent = new Intent(getActivity(), AddPhotoActivity.class);
                    startActivityForResult(intent, STREAM_NEW_POST);

                } else {

                    if (!profile.isInBlackList()) {

                        like(profile.getId());

                    } else {

                        Toast.makeText(getActivity(), getString(R.string.error_action), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mProfileMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().isPro() || App.getInstance().getFreeMessagesCount() > 0) {

                    if (profile.getAllowMessages() == 0 && !profile.isFriend()) {

                        Toast.makeText(getActivity(), getString(R.string.error_no_friend), Toast.LENGTH_SHORT).show();

                    } else {

                        if (!profile.isInBlackList()) {

                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("chatId", 0);
                            i.putExtra("profileId", profile.getId());
                            i.putExtra("withProfile", profile.getFullname());

                            i.putExtra("with_user_username", profile.getUsername());
                            i.putExtra("with_user_fullname", profile.getFullname());
                            i.putExtra("with_user_photo_url", profile.getNormalPhotoUrl());

                            i.putExtra("with_user_state", profile.getState());
                            i.putExtra("with_user_verified", profile.getVerify());

                            startActivityForResult(i, PROFILE_CHAT);

                        } else {

                            Toast.makeText(getActivity(), getString(R.string.error_action), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    Toast.makeText(getActivity(), getString(R.string.msg_pro_mode_alert), Toast.LENGTH_LONG).show();
                }
            }
        });

        mProfileGiftsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProfileGifts(profile.getId());
            }
        });


        mProfilePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (profile.getNormalPhotoUrl().length() > 0 &&
                        (App.getInstance().getSettings().isAllowShowNotModeratedProfilePhotos() || App.getInstance().getId() == profile.getId() || profile.getPhotoModerateAt() != 0)) {

                    Intent i = new Intent(getActivity(), PhotoViewActivity.class);
                    i.putExtra("imgUrl", profile.getNormalPhotoUrl());
                    startActivity(i);
                }
            }
        });

        if (profile.getFullname() == null || profile.getFullname().length() == 0) {

            if (App.getInstance().isConnected()) {

                showLoadingScreen();
                getData();

                Log.e("Profile", "OnReload");

            } else {

                showErrorScreen();
            }

        } else {

            if (App.getInstance().isConnected()) {

                if (profile.getState() == ACCOUNT_STATE_ENABLED) {

                    showContentScreen();

                    loadingComplete();
                    updateProfile();

                } else {

                    showDisabledScreen();
                }

            } else {

                showErrorScreen();
            }
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putInt("itemId", itemId);

        outState.putBoolean("restore", restore);
        outState.putBoolean("loading", loading);
        outState.putBoolean("preload", preload);

        outState.putParcelable("profileObj", profile);
        outState.putParcelableArrayList(STATE_LIST, itemsList);
        outState.putParcelableArrayList(STATE_LIKES_SPOTLIGHT_LIST, likesSpotlightList);
        outState.putParcelableArrayList(STATE_FRIENDS_SPOTLIGHT_LIST, friendsSpotlightList);
        outState.putParcelableArrayList(STATE_GIFTS_SPOTLIGHT_LIST, giftsSpotlightList);
    }

    private Bitmap resize(String path){

        int maxWidth = 512;
        int maxHeight = 512;

        // create the options
        BitmapFactory.Options opts = new BitmapFactory.Options();

        //just decode the file
        opts.inJustDecodeBounds = true;
        Bitmap bp = BitmapFactory.decodeFile(path, opts);

        //get the original size
        int orignalHeight = opts.outHeight;
        int orignalWidth = opts.outWidth;

        //initialization of the scale
        int resizeScale = 1;

        //get the good scale
        if (orignalWidth > maxWidth || orignalHeight > maxHeight) {

            final int heightRatio = Math.round((float) orignalHeight / (float) maxHeight);
            final int widthRatio = Math.round((float) orignalWidth / (float) maxWidth);
            resizeScale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        //put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;

        //get the futur size of the bitmap
        int bmSize = (orignalWidth / resizeScale) * (orignalHeight / resizeScale) * 4;

        //check if it's possible to store into the vm java the picture
        if (Runtime.getRuntime().freeMemory() > bmSize) {

            //decode the file
            bp = BitmapFactory.decodeFile(path, opts);

        } else {

            return null;
        }

        return bp;
    }

    public void save(String outFile, String inFile) {

        try {

            Bitmap bmp = resize(outFile);

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, inFile);
            FileOutputStream fOut = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception ex) {

            Log.e("Error", ex.getMessage());
        }
    }

    private static int exifToDegrees(int exifOrientation) {

        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {

            return 90;

        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {

            return 180;

        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {

            return 270;
        }

        return 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STREAM_NEW_POST && resultCode == getActivity().RESULT_OK && null != data) {

            profile.setPhotosCount(profile.getPhotosCount() + 1);

            itemId = 0;
            getItems();

        } else if (requestCode == PROFILE_EDIT && resultCode == getActivity().RESULT_OK) {

            profile.setFullname(data.getStringExtra("fullname"));
            profile.setLocation(data.getStringExtra("location"));
            profile.setFacebookPage(data.getStringExtra("facebookPage"));
            profile.setInstagramPage(data.getStringExtra("instagramPage"));
            profile.setBio(data.getStringExtra("bio"));

            profile.setSex(data.getIntExtra("sex", 0));

            profile.setSexOrientation(data.getIntExtra("sexOrientation", 0));
            profile.setAge(data.getIntExtra("age", 0));
            profile.setHeight(data.getIntExtra("height", 0));
            profile.setWeight(data.getIntExtra("weight", 0));

            profile.setYear(data.getIntExtra("year", 0));
            profile.setMonth(data.getIntExtra("month", 0));
            profile.setDay(data.getIntExtra("day", 0));

            profile.setRelationshipStatus(data.getIntExtra("relationshipStatus", 0));
            profile.setPoliticalViews(data.getIntExtra("politicalViews", 0));
            profile.setWorldView(data.getIntExtra("worldView", 0));
            profile.setPersonalPriority(data.getIntExtra("personalPriority", 0));
            profile.setImportantInOthers(data.getIntExtra("importantInOthers", 0));
            profile.setViewsOnSmoking(data.getIntExtra("viewsOnSmoking", 0));
            profile.setViewsOnAlcohol(data.getIntExtra("viewsOnAlcohol", 0));
            profile.setYouLooking(data.getIntExtra("youLooking", 0));
            profile.setYouLike(data.getIntExtra("youLike", 0));

            profile.setAllowShowMyBirthday(data.getIntExtra("allowShowMyBirthday", 0));

            updateProfile();

        } else if (requestCode == PROFILE_NEW_POST && resultCode == getActivity().RESULT_OK) {

            getData();

        }
    }

    public void choiceImage() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        arrayAdapter.add(getString(R.string.action_gallery));
        arrayAdapter.add(getString(R.string.action_camera));

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0: {

                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/jpeg");

                        imgFromGalleryActivityResultLauncher.launch(intent);

                        break;
                    }

                    default: {

                        Helper helper = new Helper(getActivity());

                        if (helper.checkPermission(Manifest.permission.CAMERA)) {

                            try {

                                newImageFileName = Helper.randomString(6) + ".jpg";

                                selectedImage = FileProvider.getUriForFile(App.getInstance().getApplicationContext(), App.getInstance().getPackageName() + ".provider", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), newImageFileName));

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, selectedImage);
                                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                imgFromCameraActivityResultLauncher.launch(cameraIntent);

                            } catch (Exception e) {

                                Toast.makeText(getActivity(), "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            requestCameraPermission();
                        }

                        break;
                    }
                }

            }
        });

        AlertDialog d = builderSingle.create();
        d.show();
    }

    @Override
    public void onRefresh() {

        if (App.getInstance().isConnected()) {

            getData();

        } else {

            mProfileRefreshLayout.setRefreshing(false);
        }
    }

    public void updateFeeling() {

        if (profile.getFeeling() > 0) {

            mFeelingIcon.setVisibility(View.VISIBLE);

            showFeeling(Constants.WEB_SITE + "feelings/" + profile.getFeeling() + ".png");

        } else {

            mFeelingIcon.setVisibility(View.GONE);
        }
    }

    public void updateProfile() {

        updateFullname();
        updateGiftsCount();
        updateFriendsCount();
        updateLikesCount();
        updateActionButton();
        updateFeeling();

        mProfileUsername.setText("@" + profile.getUsername());
        mProfileLocation.setText(profile.getLocation());

        mProfileItemsCount.setText(Integer.toString(profile.getPhotosCount()));
        mProfileFriendsCount.setText(Integer.toString(profile.getFriendsCount()));
        mProfileLikesCount.setText(Integer.toString(profile.getLikesCount()));
        mProfileGiftsCount.setText(Integer.toString(profile.getGiftsCount()));

        // Show settings button is your profile
        if (profile.getId() == App.getInstance().getId()) {

            ((ProfileActivity)getActivity()).mFabButton.show();
            ((ProfileActivity)getActivity()).mFabButton.setImageResource(R.drawable.ic_action_new);

            mProfileActionBtn.setText(R.string.action_profile_edit);

            mProfileActionBtn.setVisibility(View.VISIBLE);

            mProfileMessageBtn.setVisibility(View.GONE);

        } else {

            ((ProfileActivity)getActivity()).mFabButton.hide();

            if (!profile.isMyLike()) {

                ((ProfileActivity)getActivity()).mFabButton.setImageResource(R.drawable.ic_action_like);

                ((ProfileActivity)getActivity()).mFabButton.show();
            }

            mProfileMessageBtn.setText(R.string.action_message);

            mProfileMessageBtn.setVisibility(View.VISIBLE);

            if (!profile.isInBlackList()) {

                mProfileMessageBtn.setEnabled(true);

            } else {

                mProfileMessageBtn.setEnabled(false);
            }
        }

        if (profile.getLocation() != null && profile.getLocation().length() != 0) {

            mLocationContainer.setVisibility(View.VISIBLE);

        } else {

            mLocationContainer.setVisibility(View.GONE);
        }

        if (profile.getFacebookPage() != null && profile.getFacebookPage().length() != 0) {

            mProfileFacebookContainer.setVisibility(View.VISIBLE);
            mProfileFacebookUrl.setText(profile.getFacebookPage());

        } else {

            mProfileFacebookContainer.setVisibility(View.GONE);
        }

        if (profile.getInstagramPage() != null && profile.getInstagramPage().length() != 0) {

            mProfileSiteContainer.setVisibility(View.VISIBLE);
            mProfileSiteUrl.setText(profile.getInstagramPage());

        } else {

            mProfileSiteContainer.setVisibility(View.GONE);
        }

        if (profile.getBio() != null && profile.getBio().length() != 0) {

            mProfileStatusContainer.setVisibility(View.VISIBLE);
            mProfileStatus.setText(profile.getBio());

        } else {

            mProfileStatusContainer.setVisibility(View.GONE);
        }

        if (profile.getSex() == 0) {

            mProfileGender.setText(getString(R.string.label_sex) + ": " + getString(R.string.label_male));

        } else if (profile.getSex() == 1) {

            mProfileGender.setText(getString(R.string.label_sex) + ": " + getString(R.string.label_female));

        } else {

            mProfileGender.setText(getString(R.string.label_sex) + ": " + getString(R.string.label_secret));
        }

        switch (profile.getSexOrientation()) {

            case 1: {

                mProfileSexOrientation.setText(getString(R.string.label_sex_orientation) + ": " + getString(R.string.sex_orientation_1));
                mProfileSexOrientationImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_heterosexual));

                break;
            }

            case 2: {

                mProfileSexOrientation.setText(getString(R.string.label_sex_orientation) + ": " + getString(R.string.sex_orientation_2));
                mProfileSexOrientationImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_gay));

                break;
            }

            case 3: {

                mProfileSexOrientation.setText(getString(R.string.label_sex_orientation) + ": " + getString(R.string.sex_orientation_3));
                mProfileSexOrientationImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_lesbian));

                break;
            }

            case 4: {

                mProfileSexOrientation.setText(getString(R.string.label_sex_orientation) + ": " + getString(R.string.sex_orientation_4));
                mProfileSexOrientationImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_feature));

                break;
            }

            default: {

                mProfileSexOrientationImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_feature));

                break;
            }
        }

        mProfileAge.setText(getString(R.string.label_age) + ": " + profile.getAge());
        mProfileHeight.setText(getString(R.string.label_height) + ": " + profile.getHeight() + " (" + getString(R.string.label_cm) + ")");
        mProfileWeight.setText(getString(R.string.label_weight) + ": " + profile.getWeight() + " (" + getString(R.string.label_kg) + ")");

        Helper helper = new Helper(getContext());

        mProfileRelationshipStatus.setText(getString(R.string.account_relationship_status) + ": " + helper.getRelationshipStatus(profile.getRelationshipStatus()));
        mProfilePoliticalViews.setText(getString(R.string.account_political_views) + ": " + helper.getPoliticalViews(profile.getPoliticalViews()));
        mProfileWorldView.setText(getString(R.string.account_world_view) + ": " + helper.getWorldView(profile.getWorldView()));
        mProfilePersonalPriority.setText(getString(R.string.account_personal_priority) + ": " + helper.getPersonalPriority(profile.getPersonalPriority()));
        mProfileImportantInOthers.setText(getString(R.string.account_important_in_others) + ": " + helper.getImportantInOthers(profile.getImportantInOthers()));
        mProfileSmokingViews.setText(getString(R.string.account_smoking_views) + ": " + helper.getSmokingViews(profile.getViewsOnSmoking()));
        mProfileAlcoholViews.setText(getString(R.string.account_alcohol_views) + ": " + helper.getAlcoholViews(profile.getViewsOnAlcohol()));
        mProfileProfileLooking.setText(getString(R.string.account_profile_looking) + ": " + helper.getLooking(profile.getYouLooking()));
        mProfileGenderLike.setText(getString(R.string.account_profile_like) + ": " + helper.getGenderLike(profile.getYouLike()));

        mProfileJoinDate.setText(getString(R.string.label_profile_join) + ": " + profile.getCreateDate());

        if (profile.getAllowShowMyBirthday() == 1) {

            mProfileBirthDate.setVisibility(View.VISIBLE);
            mProfileBirthDateContainer.setVisibility(View.VISIBLE);
            mProfileBirthDate.setText(getString(R.string.label_profile_birth) + ": " + profile.getBirthDate());

        } else {

            mProfileBirthDate.setVisibility(View.GONE);
            mProfileBirthDateContainer.setVisibility(View.GONE);
        }

        if (profile.getSexOrientation() == 0) {

            mProfileSexOrientationContainer.setVisibility(View.GONE);


        } else {

            mProfileSexOrientationContainer.setVisibility(View.VISIBLE);
        }

        if (profile.getAge() == 0) {

            mProfileAgeContainer.setVisibility(View.GONE);

        } else {

            mProfileAgeContainer.setVisibility(View.VISIBLE);
        }

        if (profile.getHeight() == 0) {

            mProfileHeightContainer.setVisibility(View.GONE);

        } else {

            mProfileHeightContainer.setVisibility(View.VISIBLE);
        }

        if (profile.getWeight() == 0) {

            mProfileWeightContainer.setVisibility(View.GONE);

        } else {

            mProfileWeightContainer.setVisibility(View.VISIBLE);
        }

        if (profile.getRelationshipStatus() == 0) {

            mProfileRelationshipStatusContainer.setVisibility(View.GONE);
        }

        if (profile.getPoliticalViews() == 0) {

            mProfilePoliticalViewsContainer.setVisibility(View.GONE);
        }

        if (profile.getWorldView() == 0) {

            mProfileWorldViewContainer.setVisibility(View.GONE);
        }

        if (profile.getPersonalPriority() == 0) {

            mProfilePersonalPriorityContainer.setVisibility(View.GONE);
        }

        if (profile.getImportantInOthers() == 0) {

            mProfileImportantInOthersContainer.setVisibility(View.GONE);
        }

        if (profile.getViewsOnSmoking() == 0) {

            mProfileSmokingViewsContainer.setVisibility(View.GONE);
        }

        if (profile.getViewsOnAlcohol() == 0) {

            mProfileAlcoholViewsContainer.setVisibility(View.GONE);
        }

        if (profile.getYouLooking() == 0) {

            mProfileProfileLookingContainer.setVisibility(View.GONE);
        }

        if (profile.getYouLike() == 0) {

            mProfileGenderLikeContainer.setVisibility(View.GONE);
        }

        showPhoto(profile.getLowPhotoUrl());
        showCover(profile.getNormalCoverUrl());

        showContentScreen();

        if (profile.isOnline()) {

            // user Online

            mProfileOnlineIcon.setVisibility(View.VISIBLE);

        } else {

            mProfileOnlineIcon.setVisibility(View.GONE);
        }

        if (profile.isVerify()) {

            mProfileIcon.setVisibility(View.VISIBLE);

        } else {

            mProfileIcon.setVisibility(View.GONE);
        }

        if (profile.isProMode()) {

            mProfileProIcon.setVisibility(View.VISIBLE);

        } else {

            mProfileProIcon.setVisibility(View.GONE);
        }

        // Profile Info

        mProfileInfoContainer.setVisibility(View.GONE);

        if (profile.getAllowShowMyInfo() == 0 || App.getInstance().getId() == profile.getId()) {

            mProfileInfoContainer.setVisibility(View.VISIBLE);

        } else {

            if (profile.getAllowShowMyInfo() == 1 && profile.isFriend()) {

                mProfileInfoContainer.setVisibility(View.VISIBLE);
            }
        }

        if (profile.getAllowShowMyGallery() == 0 || App.getInstance().getId() == profile.getId()) {

            if (profile.getPhotosCount() > 0 && itemsAdapter.getItemCount() != 0) {

                mRecyclerView.setVisibility(View.VISIBLE);

            } else {

                mRecyclerView.setVisibility(View.GONE);
            }

        } else {

            if (profile.getAllowShowMyGallery() == 1 && profile.isFriend()) {

                if (profile.getPhotosCount() > 0 && itemsAdapter.getItemCount() != 0) {

                    mRecyclerView.setVisibility(View.VISIBLE);

                } else {

                    mRecyclerView.setVisibility(View.GONE);
                }

            } else {

                mRecyclerView.setVisibility(View.GONE);
            }
        }

        mProfileCountersContainer.setVisibility(View.VISIBLE);

        if (this.isVisible()) {

            try {

                getActivity().invalidateOptionsMenu();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private void updateFullname() {

        if (profile.getFullname() == null || profile.getFullname().length() == 0) {

            mProfileFullname.setText(profile.getUsername());
            if (!isMainScreen) getActivity().setTitle(profile.getUsername());

        } else {

            mProfileFullname.setText(profile.getFullname());
            if (!isMainScreen) getActivity().setTitle(profile.getFullname());
        }
    }

    public void getData() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PROFILE_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                profile = new Profile(response);

                                if (profile.getPhotosCount() > 0) {

                                    getItems();
                                }

                                if (profile.getState() == ACCOUNT_STATE_ENABLED) {

                                    showContentScreen();

                                    updateProfile();

                                } else {

                                    showDisabledScreen();
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            if (profile.getState() == ACCOUNT_STATE_ENABLED && profile.getFriendsCount() > 0) {

                                getFriendsSpotlight();
                            }

                            if (profile.getState() == ACCOUNT_STATE_ENABLED && profile.getGiftsCount() > 0) {

                                getGiftsSpotlight();
                            }

                            if (profile.getState() == ACCOUNT_STATE_ENABLED && profile.getLikesCount() > 0) {

                                getLikesSpotlight();
                            }

                            // Advanced Ad

                            updateView();

                            // Interstitial ad

                            if (App.getInstance().getInterstitialAdSettings().getInterstitialAdAfterProfileView() != 0 && profile.getId() != App.getInstance().getId() && App.getInstance().getAdmob() == ADMOB_DISABLED) {

                                App.getInstance().getInterstitialAdSettings().setCurrentInterstitialAdAfterProfileView(App.getInstance().getInterstitialAdSettings().getCurrentInterstitialAdAfterProfileView() + 1);

                                if (App.getInstance().getInterstitialAdSettings().getCurrentInterstitialAdAfterProfileView() >= App.getInstance().getInterstitialAdSettings().getInterstitialAdAfterProfileView()) {

                                    App.getInstance().getInterstitialAdSettings().setCurrentInterstitialAdAfterProfileView(0);

                                    App.getInstance().showInterstitialAd(null);
                                }

                                App.getInstance().saveData();
                            }

                            Log.e("Profile Success",  response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                Log.e("Profile Error",  error.toString() + error.getMessage() + error.getLocalizedMessage());
                showErrorScreen();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile_id));

                return params;
            }
        };

        jsonReq.setRetryPolicy(new RetryPolicy() {

            @Override
            public int getCurrentTimeout() {

                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {

                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void getFriendsSpotlight() {

        if (App.getInstance().getId() == profile.getId() || profile.getAllowShowMyFriends() == 0) {

            // All right. Load items

        } else {

            if (profile.getAllowShowMyFriends() == 1 && profile.isFriend()) {

                // All right. Load items

            } else {

                return;
            }
        }

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_FRIENDS_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        friendsSpotlightList.clear();

                        try {

                            if (!response.getBoolean("error")) {

                                if (response.has("items")) {

                                    JSONArray friendsArray = response.getJSONArray("items");

                                    arrayLength = friendsArray.length();

                                    if (arrayLength > 0) {

                                        for (int i = 0; i < friendsArray.length(); i++) {

                                            JSONObject userObj = (JSONObject) friendsArray.get(i);

                                            Friend item = new Friend(userObj);

                                            friendsSpotlightList.add(item);
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            Log.d("Friends", response.toString());

                            loadingComplete();

                            updateFriendsCount();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                Log.e("getFriendsSpotlight", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile_id));
                params.put("itemId", Integer.toString(0));
                params.put("language", "en");

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonReq.setRetryPolicy(policy);

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void getGiftsSpotlight() {

        if (App.getInstance().getId() == profile.getId() || profile.getAllowShowMyGifts() == 0) {

            // All right. Load items

        } else {

            if (profile.getAllowShowMyGifts() == 1 && profile.isFriend()) {

                // All right. Load items

            } else {

                return;
            }
        }

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_GIFTS_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        giftsSpotlightList.clear();

                        try {

                            if (!response.getBoolean("error")) {

                                if (response.has("items")) {

                                    JSONArray giftsArray = response.getJSONArray("items");

                                    arrayLength = giftsArray.length();

                                    if (arrayLength > 0) {

                                        for (int i = 0; i < giftsArray.length(); i++) {

                                            JSONObject giftObj = (JSONObject) giftsArray.get(i);

                                            Gift item = new Gift(giftObj);

                                            giftsSpotlightList.add(item);
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            Log.d("Gifts", response.toString());

                            loadingComplete();

                            updateGiftsCount();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                Log.e("getGiftsSpotlight", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile_id));
                params.put("itemId", Integer.toString(0));
                params.put("language", "en");

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonReq.setRetryPolicy(policy);

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void getLikesSpotlight() {

        if (App.getInstance().getId() == profile.getId() || profile.getAllowShowMyLikes() == 0) {

            // All right. Load items

        } else {

            if (profile.getAllowShowMyLikes() == 1 && profile.isFriend()) {

                // All right. Load items

            } else {

                return;
            }
        }

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PROFILE_FANS_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        likesSpotlightList.clear();

                        try {

                            if (!response.getBoolean("error")) {

                                if (response.has("items")) {

                                    JSONArray likesArray = response.getJSONArray("items");

                                    arrayLength = likesArray.length();

                                    if (arrayLength > 0) {

                                        for (int i = 0; i < likesArray.length(); i++) {

                                            JSONObject profileObj = (JSONObject) likesArray.get(i);

                                            Profile item = new Profile(profileObj);

                                            likesSpotlightList.add(item);
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            Log.d("Likes", response.toString());

                            loadingComplete();

                            updateLikesCount();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                Log.e("getLikesSpotlight", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile_id));
                params.put("itemId", Integer.toString(0));
                params.put("language", "en");

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonReq.setRetryPolicy(policy);

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void showFeeling(String imgUrl) {

        if (imgUrl != null && imgUrl.length() > 0) {

            ImageLoader imageLoader = App.getInstance().getImageLoader();

            imageLoader.get(imgUrl, ImageLoader.getImageListener(mFeelingIcon, R.drawable.mood, R.drawable.mood));
        }
    }

    public void showPhoto(String photoUrl) {

        if (photoUrl != null && photoUrl.length() > 0 &&
                (App.getInstance().getSettings().isAllowShowNotModeratedProfilePhotos() || App.getInstance().getId() == profile.getId() || profile.getPhotoModerateAt() != 0)) {

            ImageLoader imageLoader = App.getInstance().getImageLoader();

            imageLoader.get(photoUrl, ImageLoader.getImageListener(mProfilePhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));
        }
    }

    public void showCover(String coverUrl) {

        if (coverUrl != null && coverUrl.length() > 0 &&
                (App.getInstance().getSettings().isAllowShowNotModeratedProfilePhotos() || App.getInstance().getId() == profile.getId() || profile.getCoverModerateAt() != 0)) {

            ImageLoader imageLoader = App.getInstance().getImageLoader();

            imageLoader.get(coverUrl, ImageLoader.getImageListener(mProfileCover, R.drawable.profile_default_cover, R.drawable.profile_default_cover));

            mProfileCover.setImageAlpha(200);
        }
    }

    public void getItems() {

        if (App.getInstance().getId() == profile.getId() || profile.getAllowShowMyGallery() == 0) {

            // All right. Load items

        } else {

            if (profile.getAllowShowMyGallery() == 1 && profile.isFriend()) {

                // All right. Load items

            } else {

                return;
            }
        }

        if (loadingMore) {

            mProfileRefreshLayout.setRefreshing(true);

        } else{

            itemId = 0;
        }

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_GALLERY_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!loadingMore) {

                                itemsList.clear();
                            }

                            arrayLength = 0;

                            if (!response.getBoolean("error")) {

                                itemId = response.getInt("itemId");

                                if (response.has("items")) {

                                    JSONArray itemsArray = response.getJSONArray("items");

                                    arrayLength = itemsArray.length();

                                    if (arrayLength > 0) {

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

                            loadingComplete();
                            updateProfile();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loadingComplete();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile.getId()));
                params.put("itemId", Integer.toString(itemId));

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonReq.setRetryPolicy(policy);

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete() {

        viewMore = arrayLength == LIST_ITEMS;

        itemsAdapter.notifyDataSetChanged();
        friendsSpotlightAdapter.notifyDataSetChanged();
        giftsSpotlightAdapter.notifyDataSetChanged();
        likesSpotlightAdapter.notifyDataSetChanged();

        mProfileRefreshLayout.setRefreshing(false);

        loadingMore = false;

//        if (this.isVisible()) getActivity().invalidateOptionsMenu();
    }

    public void showLoadingScreen() {

        if (!isMainScreen) getActivity().setTitle(getText(R.string.title_activity_profile));

        mProfileRefreshLayout.setVisibility(View.GONE);
        mProfileErrorScreen.setVisibility(View.GONE);
        mProfileDisabledScreen.setVisibility(View.GONE);
//
        mProfileLoadingScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showErrorScreen() {

        if (!isMainScreen) getActivity().setTitle(getText(R.string.title_activity_profile));

        mProfileLoadingScreen.setVisibility(View.GONE);
        mProfileDisabledScreen.setVisibility(View.GONE);
        mProfileRefreshLayout.setVisibility(View.GONE);
//
        mProfileErrorScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showDisabledScreen() {

        if (profile.getState() != ACCOUNT_STATE_ENABLED) {

            //mProfileDisabledScreenMsg.setText(getText(R.string.msg_account_blocked));
        }

        getActivity().setTitle(getText(R.string.label_account_disabled));

        mProfileRefreshLayout.setVisibility(View.GONE);
        mProfileLoadingScreen.setVisibility(View.GONE);
        mProfileErrorScreen.setVisibility(View.GONE);
//
        mProfileDisabledScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showContentScreen() {

        if (!isMainScreen) {

            getActivity().setTitle(profile.getFullname());
        }

        mProfileDisabledScreen.setVisibility(View.GONE);
        mProfileLoadingScreen.setVisibility(View.GONE);
        mProfileErrorScreen.setVisibility(View.GONE);
//
        mProfileRefreshLayout.setVisibility(View.VISIBLE);
        mProfileRefreshLayout.setRefreshing(false);

        loadingComplete = true;
        restore = true;
    }

    public void action(int position) {

        final Image item = itemsList.get(position);
    }

    public void updateView() {

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
                        ((TextView) mAdView.getHeadlineView()).setText(nativeAd.getHeadline());
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
                    Log.e("admob", App.getInstance().getAdmobSettings().getBannerNativeAdUnitId());

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();

        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (loadingComplete) {

            if (profile.getState() != ACCOUNT_STATE_ENABLED) {

                //hide all menu items
                hideMenuItems(menu, false);
            }

            if (App.getInstance().getId() != profile.getId()) {

                MenuItem menuItem = menu.findItem(R.id.action_profile_block);

                if (profile.isBlocked()) {

                    menuItem.setTitle(getString(R.string.action_unblock));

                } else {

                    menuItem.setTitle(getString(R.string.action_block));
                }

                menu.removeItem(R.id.action_profile_edit_feeling);
                menu.removeItem(R.id.action_profile_edit_photo);
                menu.removeItem(R.id.action_profile_edit_cover);
                menu.removeItem(R.id.action_profile_settings);

            } else {

                // your profile

                menu.removeItem(R.id.action_new_gift);
                menu.removeItem(R.id.action_profile_report);
                menu.removeItem(R.id.action_profile_block);
            }


            // If site not available - hide items

            if (!WEB_SITE_AVAILABLE) {

                menu.removeItem(R.id.action_profile_copy_url);
                menu.removeItem(R.id.action_profile_open_url);
            }

            //show all menu items
            hideMenuItems(menu, true);

        } else {

            //hide all menu items
            hideMenuItems(menu, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_profile_copy_url: {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(profile.getUsername(), API_DOMAIN + profile.getUsername());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getActivity(), getText(R.string.msg_profile_link_copied), Toast.LENGTH_SHORT).show();

                return true;
            }

            case R.id.action_profile_open_url: {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(API_DOMAIN + profile.getUsername()));
                startActivity(i);

                return true;
            }

            case R.id.action_new_gift: {

                selectGift(profile.getId());

                return true;
            }

            case R.id.action_profile_refresh: {

                mProfileRefreshLayout.setRefreshing(true);
                onRefresh();

                return true;
            }

            case R.id.action_profile_report: {

                profileReport();

                return true;
            }

            case R.id.action_profile_block: {

                profileBlock();

                return true;
            }

            case R.id.action_profile_edit_feeling: {

                choiceFeelingDialog();

                return true;
            }

            case R.id.action_profile_edit_photo: {

                Helper helper = new Helper(getActivity());

                if (!helper.checkStoragePermission()) {

                    requestStoragePermission();

                } else {

                    mAccountAction = 0;

                    choiceImage();
                }

                return true;
            }

            case R.id.action_profile_edit_cover: {

                Helper helper = new Helper(getActivity());

                if (!helper.checkStoragePermission()) {

                    requestStoragePermission();

                } else {

                    mAccountAction = 1;

                    choiceImage();
                }

                return true;
            }

            case R.id.action_profile_settings: {

                getAccountSettings();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void hideMenuItems(Menu menu, boolean visible) {

        for (int i = 0; i < menu.size(); i++){

            menu.getItem(i).setVisible(visible);
        }
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void profileReport() {

        /** Getting the fragment manager */
        FragmentManager fm = getActivity().getSupportFragmentManager();

        /** Instantiating the DialogFragment class */
        ProfileReportDialog alert = new ProfileReportDialog();

        /** Creating a bundle object to store the selected item's index */
        Bundle b  = new Bundle();

        /** Storing the selected item's index in the bundle object */
        b.putInt("position", 0);

        /** Setting the bundle object to the dialog fragment object */
        alert.setArguments(b);

        /** Creating the dialog fragment object, which will in turn open the alert dialog window */

        alert.show(fm, "alert_dialog_profile_report");
    }

    public  void onProfileReport(final int position) {

        Api api = new Api(getActivity());

        api.profileReport(profile.getId(), position);
    }

    public void profileBlock() {

        if (!profile.isBlocked()) {

            /** Getting the fragment manager */
            FragmentManager fm = getActivity().getSupportFragmentManager();

            /** Instantiating the DialogFragment class */
            ProfileBlockDialog alert = new ProfileBlockDialog();

            /** Creating a bundle object to store the selected item's index */
            Bundle b  = new Bundle();

            /** Storing the selected item's index in the bundle object */
            b.putString("blockUsername", profile.getUsername());

            /** Setting the bundle object to the dialog fragment object */
            alert.setArguments(b);

            /** Creating the dialog fragment object, which will in turn open the alert dialog window */

            alert.show(fm, "alert_dialog_profile_block");

        } else {

            loading = true;

            showpDialog();

            CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_BLACKLIST_REMOVE, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (!isAdded() || getActivity() == null) {

                                Log.e("ERROR", "ProfileFragment Not Added to Activity");

                                return;
                            }

                            try {

                                if (!response.getBoolean("error")) {

                                    profile.setBlocked(false);

                                    Toast.makeText(getActivity(), getString(R.string.msg_profile_removed_from_blacklist), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();

                            } finally {

                                loading = false;

                                hidepDialog();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (!isAdded() || getActivity() == null) {

                        Log.e("ERROR", "ProfileFragment Not Added to Activity");

                        return;
                    }

                    loading = false;

                    hidepDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", Long.toString(App.getInstance().getId()));
                    params.put("accessToken", App.getInstance().getAccessToken());
                    params.put("profileId", Long.toString(profile.getId()));

                    return params;
                }
            };

            App.getInstance().addToRequestQueue(jsonReq);
        }
    }

    public  void onProfileBlock() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_BLACKLIST_ADD, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                profile.setBlocked(true);

                                Toast.makeText(getActivity(), getString(R.string.msg_profile_added_to_blacklist), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile.getId()));
                params.put("reason", "example");

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public Boolean uploadFile(File file, final int type) {

        loading = true;

        showpDialog();

        final OkHttpClient client = new OkHttpClient();

        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));

        try {

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("uploaded_file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                    .addFormDataPart("accountId", Long.toString(App.getInstance().getId()))
                    .addFormDataPart("accessToken", App.getInstance().getAccessToken())
                    .addFormDataPart("imgType", Integer.toString(type))
                    .build();

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(METHOD_ACCOUNT_UPLOAD_IMAGE)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(com.squareup.okhttp.Request request, IOException e) {

                    loading = false;

                    hidepDialog();

                    Log.e("failure", request.toString());
                }

                @Override
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {

                    String jsonData = response.body().string();

                    Log.e("response", jsonData);

                    try {

                        JSONObject result = new JSONObject(jsonData);

                        if (!result.getBoolean("error")) {

                            switch (type) {

                                case 0: {

                                    profile.setLowPhotoUrl(result.getString("lowPhotoUrl"));
                                    profile.setBigPhotoUrl(result.getString("bigPhotoUrl"));
                                    profile.setNormalPhotoUrl(result.getString("normalPhotoUrl"));

                                    App.getInstance().setPhotoUrl(result.getString("lowPhotoUrl"));

                                    break;
                                }

                                default: {

                                    profile.setNormalCoverUrl(result.getString("normalCoverUrl"));

                                    App.getInstance().setCoverUrl(result.getString("normalCoverUrl"));

                                    break;
                                }
                            }
                        }

                        Log.d("My App", response.toString());

                    } catch (Throwable t) {

                        Log.e("My App", "Could not parse malformed JSON: \"" + response.body().string() + "\"");

                    } finally {

                        loading = false;

                        hidepDialog();

                        getData();
                    }

                }
            });

            return true;

        } catch (Exception ex) {
            // Handle the error

            loading = false;

            hidepDialog();
        }

        return false;
    }

    public void removeFromFriends() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_FRIENDS_REMOVE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                profile.setFriend(false);
                                profile.setFriendsCount(profile.getFriendsCount() - 1);

                                updateProfile();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("friendId", Long.toString(profile.getId()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void friendsRequest() {

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_FRIENDS_REQUEST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                profile.setFollow(response.getBoolean("follow"));
                                profile.setFollowersCount(response.getInt("followersCount"));

                                updateProfile();

                                changeAccessMode();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            hidepDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile_id));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void changeAccessMode() {

        if (App.getInstance().getId() == profile.getId() || profile.isFollow()) {

            accessMode = 1;

        } else {

            accessMode = 0;
        }
    }

    private void updateFriendsCount() {

        mProfileFriendsCount.setText(Integer.toString(profile.getFriendsCount()));
        mFriendsSpotlightTitle.setText(getString(R.string.label_friends) + " (" + profile.getFriendsCount() + ")");

        if (profile.getAllowShowMyFriends() == 0 || App.getInstance().getId() == profile.getId()) {

            if (profile.getFriendsCount() > 0 && friendsSpotlightAdapter.getItemCount() != 0) {

                mFriendsSpotlight.setVisibility(View.VISIBLE);

            } else {

                mFriendsSpotlight.setVisibility(View.GONE);
            }

        } else {

            mFriendsSpotlight.setVisibility(View.GONE);

            if (profile.getAllowShowMyFriends() == 1 && profile.isFriend()) {

                if (profile.getFriendsCount() > 0 && friendsSpotlightAdapter.getItemCount() != 0) {

                    mFriendsSpotlight.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void updateGiftsCount() {

        mProfileGiftsCount.setText(Integer.toString(profile.getGiftsCount()));
        mGiftsSpotlightTitle.setText(getString(R.string.label_gifts) + " (" + profile.getGiftsCount() + ")");

        if (profile.getAllowShowMyGifts() == 0 || App.getInstance().getId() == profile.getId()) {

            if (profile.getGiftsCount() > 0 && giftsSpotlightAdapter.getItemCount() != 0) {

                mGiftsSpotlight.setVisibility(View.VISIBLE);

            } else {

                mGiftsSpotlight.setVisibility(View.GONE);
            }

        } else {

            mGiftsSpotlight.setVisibility(View.GONE);

            if (profile.getAllowShowMyGifts() == 1 && profile.isFriend()) {

                if (profile.getGiftsCount() > 0 && giftsSpotlightAdapter.getItemCount() != 0) {

                    mGiftsSpotlight.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void updateLikesCount() {

        mProfileLikesCount.setText(Integer.toString(profile.getLikesCount()));
        mLikesSpotlightTitle.setText(getString(R.string.label_likes) + " (" + profile.getLikesCount() + ")");

        if (profile.getAllowShowMyLikes() == 0 || App.getInstance().getId() == profile.getId()) {

            if (profile.getLikesCount() > 0 && likesSpotlightAdapter.getItemCount() != 0) {

                mLikesSpotlight.setVisibility(View.VISIBLE);

            } else {

                mLikesSpotlight.setVisibility(View.GONE);
            }

        } else {

            mLikesSpotlight.setVisibility(View.GONE);

            if (profile.getAllowShowMyLikes() == 1 && profile.isFriend()) {

                if (profile.getLikesCount() > 0 && likesSpotlightAdapter.getItemCount() != 0) {

                    mLikesSpotlight.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void updateActionButton() {

        if (profile.getId() == App.getInstance().getId()) {

            mProfileActionBtn.setText(R.string.action_profile_edit);
            mProfileActionBtn.setEnabled(true);

        } else {

            if (profile.isFriend()) {

                mProfileActionBtn.setText(R.string.action_remove_from_friends);
                mProfileActionBtn.setEnabled(true);

            } else {

                if (profile.isFollow()) {

                    mProfileActionBtn.setText(R.string.action_cancel_friends_request);
                    mProfileActionBtn.setEnabled(true);

                } else {

                    mProfileActionBtn.setText(R.string.action_add_to_friends);
                    mProfileActionBtn.setEnabled(true);
                }
            }
        }
    }

    public void showProfileGifts(long profileId) {

        if (profile.getAllowShowMyGifts() == 0 || App.getInstance().getId() == profile.getId()) {

            Intent intent = new Intent(getActivity(), GiftsActivity.class);
            intent.putExtra("profileId", profileId);
            startActivity(intent);

        } else {

            if (profile.getAllowShowMyGifts() == 1 && profile.isFriend()) {

                Intent intent = new Intent(getActivity(), GiftsActivity.class);
                intent.putExtra("profileId", profileId);
                startActivity(intent);

            }
        }
    }

    public void showProfileLikes(long profileId) {

        if (profile.getAllowShowMyLikes() == 0 || App.getInstance().getId() == profile.getId()) {

            Intent intent = new Intent(getActivity(), LikesActivity.class);
            intent.putExtra("profileId", profileId);
            startActivity(intent);

        } else {

            if (profile.getAllowShowMyLikes() == 1 && profile.isFriend()) {

                Intent intent = new Intent(getActivity(), LikesActivity.class);
                intent.putExtra("profileId", profileId);
                startActivity(intent);

            }
        }
    }

    public void showProfileFriends(long profileId) {

        if (profile.getAllowShowMyFriends() == 0 || App.getInstance().getId() == profile.getId()) {

            Intent intent = new Intent(getActivity(), FriendsActivity.class);
            intent.putExtra("profileId", profileId);
            startActivity(intent);

        } else {

            if (profile.getAllowShowMyFriends() == 1 && profile.isFriend()) {

                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                intent.putExtra("profileId", profileId);
                startActivity(intent);

            }
        }
    }

    public void selectGift(long profileId) {

        if (!profile.isInBlackList()) {

            choiceGiftDialog();

        } else {

            Toast.makeText(getActivity(), getString(R.string.error_action), Toast.LENGTH_SHORT).show();
        }
    }

    public void like(final long profileId) {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PROFILE_LIKE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                if (response.has("likesCount")) {

                                    profile.setLikesCount(response.getInt("likesCount"));

                                    updateLikesCount();
                                }

                                if (response.has("myLike")) {

                                    profile.setMyLike(response.getBoolean("myLike"));
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            ((ProfileActivity)getActivity()).mFabButton.hide();

                            // Interstitial ad

                            if (App.getInstance().getInterstitialAdSettings().getInterstitialAdAfterNewProfileLike() != 0 && App.getInstance().getAdmob() == ADMOB_DISABLED) {

                                App.getInstance().getInterstitialAdSettings().setCurrentInterstitialAdAfterNewProfileLike(App.getInstance().getInterstitialAdSettings().getCurrentInterstitialAdAfterNewProfileLike() + 1);

                                if (App.getInstance().getInterstitialAdSettings().getCurrentInterstitialAdAfterNewProfileLike() >= App.getInstance().getInterstitialAdSettings().getInterstitialAdAfterNewProfileLike()) {

                                    App.getInstance().getInterstitialAdSettings().setCurrentInterstitialAdAfterNewProfileLike(0);

                                    App.getInstance().showInterstitialAd(null);
                                }

                                App.getInstance().saveData();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profileId));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    private void choiceGiftDialog() {

        final GiftsSelectListAdapter giftsAdapter;

        giftsAdapter = new GiftsSelectListAdapter(getActivity(), App.getInstance().getGiftsList());

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_gifts);
        dialog.setCancelable(true);

        final ProgressBar mProgressBar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        TextView mDlgTitle = (TextView) dialog.findViewById(R.id.title_label);
        mDlgTitle.setText(R.string.dlg_choice_gift_title);

        TextView mDlgSubtitle = (TextView) dialog.findViewById(R.id.subtitle_label);
        mDlgSubtitle.setText(String.format(Locale.getDefault(), getString(R.string.account_balance_label), App.getInstance().getBalance()));

        AppCompatButton mDlgBalanceButton = (AppCompatButton) dialog.findViewById(R.id.balance_button);
        mDlgBalanceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), BalanceActivity.class);
                startActivityForResult(i, 1945);

                dialog.dismiss();
            }
        });

        AppCompatButton mDlgCancelButton = (AppCompatButton) dialog.findViewById(R.id.cancel_button);
        mDlgCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        NestedScrollView mDlgNestedView = (NestedScrollView) dialog.findViewById(R.id.nested_view);
        final RecyclerView mDlgRecyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Helper.getStickersGridSpanCount(getActivity()));
        mDlgRecyclerView.setLayoutManager(mLayoutManager);
        mDlgRecyclerView.setHasFixedSize(true);
        mDlgRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDlgRecyclerView.setAdapter(giftsAdapter);

        mDlgRecyclerView.setNestedScrollingEnabled(true);

        giftsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {

                super.onChanged();

                if (App.getInstance().getGiftsList().size() != 0) {

                    mDlgRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        giftsAdapter.setOnItemClickListener(new GiftsSelectListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, BaseGift obj, int position) {

                if (App.getInstance().getBalance() >= obj.getCost()) {

                    Intent intent = new Intent(getActivity(), SendGiftActivity.class);
                    intent.putExtra("giftId", obj.getId());
                    intent.putExtra("giftTo", profile.getId());
                    intent.putExtra("giftCost", obj.getCost());
                    intent.putExtra("imgUrl", obj.getImgUrl());
                    startActivityForResult(intent, PROFILE_NEW_GIFT);

                    dialog.dismiss();

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (App.getInstance().getGiftsList().size() == 0) {

            mDlgRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

            Api api = new Api(getActivity());
            api.getGifts(giftsAdapter);
        }

        dialog.show();

        doKeepDialog(dialog);
    }

    private void choiceFeelingDialog() {

        final FeelingsListAdapter feelingsAdapter;

        feelingsAdapter = new FeelingsListAdapter(getActivity(), App.getInstance().getFeelingsList());

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_feelings);
        dialog.setCancelable(true);

        final ProgressBar mProgressBar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        TextView mDlgTitle = (TextView) dialog.findViewById(R.id.title_label);
        mDlgTitle.setText(R.string.dlg_choice_feeling_title);

        AppCompatButton mDlgCancelButton = (AppCompatButton) dialog.findViewById(R.id.cancel_button);
        mDlgCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        NestedScrollView mDlgNestedView = (NestedScrollView) dialog.findViewById(R.id.nested_view);
        final RecyclerView mDlgRecyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Helper.getStickersGridSpanCount(getActivity()));
        mDlgRecyclerView.setLayoutManager(mLayoutManager);
        mDlgRecyclerView.setHasFixedSize(true);
        mDlgRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDlgRecyclerView.setAdapter(feelingsAdapter);

        mDlgRecyclerView.setNestedScrollingEnabled(true);

        feelingsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {

                super.onChanged();

                if (App.getInstance().getFeelingsList().size() != 0) {

                    mDlgRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        feelingsAdapter.setOnItemClickListener(new FeelingsListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Feeling obj, int position) {

                Api api = new Api(getActivity());
                api.setFelling(position);

                profile.setFeeling(position);

                updateFeeling();

                dialog.dismiss();
            }
        });

        if (App.getInstance().getFeelingsList().size() == 0) {

            mDlgRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

            Api api = new Api(getActivity());
            api.getFeelings(feelingsAdapter);
        }

        dialog.show();

        doKeepDialog(dialog);
    }

    // Prevent dialog dismiss when orientation changes
    private static void doKeepDialog(Dialog dialog){

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
    }

    static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        public interface OnItemClickListener {

            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private FriendsFragment.RecyclerItemClickListener.OnItemClickListener mListener;

        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, FriendsFragment.RecyclerItemClickListener.OnItemClickListener listener) {

            mListener = listener;

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (childView != null && mListener != null) {

                        mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {

            View childView = view.findChildViewUnder(e.getX(), e.getY());

            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {

                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void getAccountSettings() {

        Intent i = new Intent(getActivity(), AccountSettingsActivity.class);
        i.putExtra("profileId", App.getInstance().getId());
        i.putExtra("sex", profile.getSex());
        i.putExtra("year", profile.getYear());
        i.putExtra("month", profile.getMonth());
        i.putExtra("day", profile.getDay());

        i.putExtra("sexOrientation", profile.getSexOrientation());
        i.putExtra("age", profile.getAge());
        i.putExtra("height", profile.getHeight());
        i.putExtra("weight", profile.getWeight());

        i.putExtra("relationshipStatus", profile.getRelationshipStatus());
        i.putExtra("politicalViews", profile.getPoliticalViews());
        i.putExtra("worldView", profile.getWorldView());
        i.putExtra("personalPriority", profile.getPersonalPriority());
        i.putExtra("importantInOthers", profile.getImportantInOthers());
        i.putExtra("viewsOnSmoking", profile.getViewsOnSmoking());
        i.putExtra("viewsOnAlcohol", profile.getViewsOnAlcohol());
        i.putExtra("youLooking", profile.getYouLooking());
        i.putExtra("youLike", profile.getYouLike());

        i.putExtra("allowShowMyBirthday", profile.getAllowShowMyBirthday());

        i.putExtra("fullname", profile.getFullname());
        i.putExtra("location", profile.getLocation());
        i.putExtra("facebookPage", profile.getFacebookPage());
        i.putExtra("instagramPage", profile.getInstagramPage());
        i.putExtra("bio", profile.getBio());
        startActivityForResult(i, PROFILE_EDIT);
    }

    private void requestStoragePermission() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {

            storagePermissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_IMAGES});

        } else {

            storagePermissionLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    private void requestCameraPermission() {

        cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }
}