package com.raccoonsquare.dating;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.raccoonsquare.dating.adapter.AdvancedPeopleListAdapter;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.model.Profile;
import com.raccoonsquare.dating.util.CustomRequest;
import com.raccoonsquare.dating.util.Helper;
import com.raccoonsquare.dating.view.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SearchFragment extends Fragment implements Constants, SwipeRefreshLayout.OnRefreshListener {

    private static final String STATE_LIST = "State Adapter Data";

    Menu MainMenu;

    RecyclerView mRecyclerView;
    TextView mMessage;
    ImageView mSplash;

    SwipeRefreshLayout mItemsContainer;

    private ArrayList<Profile> itemsList;
    private AdvancedPeopleListAdapter itemsAdapter;

    public String queryText = "";

    private int sex_orientation = 0, gender = 3, online = 0, photo = 0, pro_mode = 0, age_from = 18, age_to = 105, distance = SEARCH_DISTANCE_DEFAULT;

    private int itemId = 0;
    private int arrayLength = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean restore = false;

    int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        if (savedInstanceState != null) {

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            itemsAdapter = new AdvancedPeopleListAdapter(getActivity(), itemsList);

            viewMore = savedInstanceState.getBoolean("viewMore");

            restore = savedInstanceState.getBoolean("restore");
            itemId = savedInstanceState.getInt("itemId");

            gender = savedInstanceState.getInt("gender");
            sex_orientation = savedInstanceState.getInt("sex_orientation");
            online = savedInstanceState.getInt("online");
            photo = savedInstanceState.getInt("photo");
            pro_mode = savedInstanceState.getInt("pro_mode");
            age_from = savedInstanceState.getInt("age_from");
            age_to = savedInstanceState.getInt("age_to");
            distance = savedInstanceState.getInt("distance");

        } else {

            itemsList = new ArrayList<Profile>();
            itemsAdapter = new AdvancedPeopleListAdapter(getActivity(), itemsList);

            restore = false;
            itemId = 0;
            distance = SEARCH_DISTANCE_DEFAULT;

            readData();
        }

        mItemsContainer = rootView.findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);

        mMessage = rootView.findViewById(R.id.message);
        mSplash = rootView.findViewById(R.id.splash);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Helper.getGridSpanCount(getActivity()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(itemsAdapter);

        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //check for scroll down

                if (dy > 0) {

                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loadingMore) {

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && (viewMore) && !(mItemsContainer.isRefreshing())) {

                            loadingMore = true;

                            search();
                        }
                    }
                }
            }
        });

        itemsAdapter.setOnItemClickListener(new AdvancedPeopleListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Profile item, int position) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", item.getId());
                startActivity(intent);
            }
        });

        if (itemsAdapter.getItemCount() == 0) {

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            hideMessage();
        }

        if (!restore) {

            showMessage(getText(R.string.msg_loading_2).toString());

            searchStart();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onRefresh() {

        if (App.getInstance().isConnected()) {

            itemId = 0;
            search();

        } else {

            mItemsContainer.setRefreshing(false);
        }
    }

    public void searchStart() {

        if (App.getInstance().isConnected()) {

            itemId = 0;
            search();

        } else {

            Toast.makeText(getActivity(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("viewMore", viewMore);
        outState.putString("queryText", queryText);
        outState.putBoolean("restore", true);
        outState.putInt("itemId", itemId);

        outState.putInt("distance", distance);
        outState.putInt("gender", gender);
        outState.putInt("sex_orientation", sex_orientation);
        outState.putInt("online", online);
        outState.putInt("photo", photo);
        outState.putInt("pro_mode", pro_mode);
        outState.putInt("age_from", age_from);
        outState.putInt("age_to", age_to);

        outState.putParcelableArrayList(STATE_LIST, itemsList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();

        inflater.inflate(R.menu.menu_nearby, menu);

        MainMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_nearby_settings: {

                getSearchSettings();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void search() {

        mItemsContainer.setRefreshing(true);

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_SEARCH_PEOPLE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "SearchFragment Not Added to Activity");

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

                                    JSONArray usersArray = response.getJSONArray("items");

                                    arrayLength = usersArray.length();

                                    if (arrayLength > 0) {

                                        for (int i = 0; i < usersArray.length(); i++) {

                                            JSONObject profileObj = (JSONObject) usersArray.get(i);

                                            Profile u = new Profile(profileObj);

                                            itemsList.add(u);
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loadingComplete();

                            Log.e("response", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "SearchFragment Not Added to Activity");

                    return;
                }

                loadingComplete();

                Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("query", queryText);
                params.put("itemId", Integer.toString(itemId));
                params.put("gender", Integer.toString(gender));
                params.put("online", Integer.toString(online));
                params.put("photo", Integer.toString(photo));
                params.put("pro", Integer.toString(pro_mode));
                params.put("ageFrom", Integer.toString(age_from));
                params.put("ageTo", Integer.toString(age_to));
                params.put("sex_orientation", Integer.toString(sex_orientation));
                params.put("distance", Integer.toString(distance));
                params.put("lat", Double.toString(App.getInstance().getLat()));
                params.put("lng", Double.toString(App.getInstance().getLng()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete() {

        restore = true;

        viewMore = arrayLength == LIST_ITEMS;

        itemsAdapter.notifyDataSetChanged();

        loadingMore = false;

        mItemsContainer.setRefreshing(false);

        if (mRecyclerView.getAdapter().getItemCount() == 0) {

            if (isAdded()) {

                showMessage(getString(R.string.label_search_results_error));
            }

        } else {

            hideMessage();
        }
    }

    public void showMessage(String message) {

        if (isAdded()) {

            mMessage.setText(message);
            mMessage.setVisibility(View.VISIBLE);

            mSplash.setVisibility(View.VISIBLE);
        }
    }

    public void hideMessage() {

        if (isAdded()) {

            mMessage.setVisibility(View.GONE);

            mSplash.setVisibility(View.GONE);
        }
    }

    public void getSearchSettings() {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(getText(R.string.label_search_filters));

        LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_search_settings, null);

        b.setView(view);

        final EditText mSearchBox = view.findViewById(R.id.search_box);

        final RadioButton mAnyGenderRadio = view.findViewById(R.id.radio_gender_any);
        final RadioButton mMaleGenderRadio = view.findViewById(R.id.radio_gender_male);
        final RadioButton mFemaleGenderRadio = view.findViewById(R.id.radio_gender_female);
        final RadioButton mSecretGenderRadio = view.findViewById(R.id.radio_gender_secret);

        final RadioButton mAnySexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_any);
        final RadioButton mHeterosexualSexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_heterosexual);
        final RadioButton mGaySexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_gay);
        final RadioButton mLesbianSexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_lesbian);
        final RadioButton mBisexualSexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_bisexual);

        final CheckBox mOnlineCheckBox = view.findViewById(R.id.checkbox_online);
        final CheckBox mPhotoCheckBox = view.findViewById(R.id.checkbox_photo);
        final CheckBox mProCheckBox = view.findViewById(R.id.checkbox_pro);

        final RangeSeekBar<Integer> mAgeSeekBar = view.findViewById(R.id.age_seekbar);

        final TextView mDistanceLabel = view.findViewById(R.id.distance_label);
        final AppCompatSeekBar mDistanceSeekBar = view.findViewById(R.id.choice_distance);
        mDistanceSeekBar.setMax(SEARCH_DISTANCE_DEFAULT + 30);

        // Search

        mSearchBox.setText(queryText);

        // Gender

        switch (gender) {

            case 0: {

                mMaleGenderRadio.setChecked(true);

                break;
            }

            case 1: {

                mFemaleGenderRadio.setChecked(true);

                break;
            }

            case 2: {

                mSecretGenderRadio.setChecked(true);

                break;
            }

            default: {

                mAnyGenderRadio.setChecked(true);

                break;
            }
        }

        switch (sex_orientation) {

            case 0: {

                mAnySexOrientationRadio.setChecked(true);

                break;
            }

            case 1: {

                mHeterosexualSexOrientationRadio.setChecked(true);

                break;
            }

            case 2: {

                mGaySexOrientationRadio.setChecked(true);

                break;
            }

            case 3: {

                mLesbianSexOrientationRadio.setChecked(true);

                break;
            }

            default: {

                mBisexualSexOrientationRadio.setChecked(true);

                break;
            }
        }

        mOnlineCheckBox.setChecked(false);
        mPhotoCheckBox.setChecked(false);
        mProCheckBox.setChecked(false);

        if (online > 0) {

            mOnlineCheckBox.setChecked(true);
        }

        if (photo > 0) {

            mPhotoCheckBox.setChecked(true);
        }

        if (pro_mode > 0) {

            mProCheckBox.setChecked(true);
        }

        mAgeSeekBar.setSelectedMinValue(age_from);
        mAgeSeekBar.setSelectedMaxValue(age_to);

        mDistanceSeekBar.setProgress(distance);
        mDistanceLabel.setText(String.format(Locale.getDefault(), getString(R.string.label_distance), distance + 30));

        mDistanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mDistanceLabel.setText(String.format(Locale.getDefault(), getString(R.string.label_distance), progress + 30));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        b.setPositiveButton(getText(R.string.action_ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                // Search text

                queryText = mSearchBox.getText().toString().trim();

                // get distance

                distance = mDistanceSeekBar.getProgress();

                // age

                age_from = mAgeSeekBar.getSelectedMinValue();
                age_to = mAgeSeekBar.getSelectedMaxValue();

                // Gender

                if (mAnyGenderRadio.isChecked()) {

                    gender = 3;
                }

                if (mMaleGenderRadio.isChecked()) {

                    gender = 0;
                }

                if (mFemaleGenderRadio.isChecked()) {

                    gender = 1;
                }

                if (mSecretGenderRadio.isChecked()) {

                    gender = 2;
                }

                // Sex orientation

                if (mAnySexOrientationRadio.isChecked()) {

                    sex_orientation = 0;
                }

                if (mHeterosexualSexOrientationRadio.isChecked()) {

                    sex_orientation = 1;
                }

                if (mGaySexOrientationRadio.isChecked()) {

                    sex_orientation = 2;
                }

                if (mLesbianSexOrientationRadio.isChecked()) {

                    sex_orientation = 3;
                }

                if (mBisexualSexOrientationRadio.isChecked()) {

                    sex_orientation = 4;
                }

                //

                if (mOnlineCheckBox.isChecked()) {

                    online = 1;

                } else {

                    online = 0;
                }

                if (mPhotoCheckBox.isChecked()) {

                    photo = 1;

                } else {

                    photo = 0;
                }

                if (mProCheckBox.isChecked()) {

                    pro_mode = 1;

                } else {

                    pro_mode = 0;
                }

                // Save filters settings

                saveData();

                // Reload items list

                //String q = getCurrentQuery();

                itemId = 0;
                searchStart();
            }
        });

        b.setNegativeButton(getText(R.string.action_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog d = b.create();

        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.show();
    }

    private void readData() {

        gender = App.getInstance().getSharedPref().getInt(getString(R.string.settings_search_gender), 3); // 3 = all
        sex_orientation = App.getInstance().getSharedPref().getInt(getString(R.string.settings_search_sex_orientation), 0); // 0 = all
        online = App.getInstance().getSharedPref().getInt(getString(R.string.settings_search_online), 0);
        photo = App.getInstance().getSharedPref().getInt(getString(R.string.settings_search_photo), 0);
        pro_mode = App.getInstance().getSharedPref().getInt(getString(R.string.settings_search_pro), 0);
        age_from = App.getInstance().getSharedPref().getInt(getString(R.string.settings_search_age_from), 18);
        age_to = App.getInstance().getSharedPref().getInt(getString(R.string.settings_search_age_to), 105);
        distance = App.getInstance().getSharedPref().getInt(getString(R.string.settings_search_distance), SEARCH_DISTANCE_DEFAULT);
    }

    public void saveData() {

        App.getInstance().getSharedPref().edit().putInt(getString(R.string.settings_search_gender), gender).apply();
        App.getInstance().getSharedPref().edit().putInt(getString(R.string.settings_search_sex_orientation), sex_orientation).apply();
        App.getInstance().getSharedPref().edit().putInt(getString(R.string.settings_search_online), online).apply();
        App.getInstance().getSharedPref().edit().putInt(getString(R.string.settings_search_photo), photo).apply();
        App.getInstance().getSharedPref().edit().putInt(getString(R.string.settings_search_pro), pro_mode).apply();
        App.getInstance().getSharedPref().edit().putInt(getString(R.string.settings_search_age_from), age_from).apply();
        App.getInstance().getSharedPref().edit().putInt(getString(R.string.settings_search_age_to), age_to).apply();
        App.getInstance().getSharedPref().edit().putInt(getString(R.string.settings_search_distance), distance).apply();
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