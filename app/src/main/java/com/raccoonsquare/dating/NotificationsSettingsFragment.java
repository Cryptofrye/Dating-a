package com.raccoonsquare.dating;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;

public class NotificationsSettingsFragment extends PreferenceFragmentCompat implements Constants {

    private CheckBoxPreference mAllowLikesGCM, mAllowFollowersGCM, mAllowMessagesGCM, mAllowGiftsGCM, mAllowMatchesGCM, mAllowCommentsGCM;

    private ProgressDialog pDialog;

    int mAllowLikes, mAllowFollowers, mAllowMessages, mAllowGifts, mAllowMatches, mAllowComments;

    private Boolean loading = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.notifications_settings, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initpDialog();

        mAllowCommentsGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowCommentsGCM");

        mAllowCommentsGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowComments = 1;

                    } else {

                        mAllowComments = 0;
                    }

                    saveSettings();
                }

                return true;
            }
        });

        mAllowMatchesGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowMatchesGCM");

        mAllowMatchesGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowMatches = 1;

                    } else {

                        mAllowMatches = 0;
                    }

                    saveSettings();
                }

                return true;
            }
        });

        mAllowLikesGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowLikesGCM");

        mAllowLikesGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowLikes = 1;

                    } else {

                        mAllowLikes = 0;
                    }

                    saveSettings();
                }

                return true;
            }
        });

        mAllowFollowersGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowFollowersGCM");

        mAllowFollowersGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowFollowers = 1;

                    } else {

                        mAllowFollowers = 0;
                    }

                    saveSettings();
                }

                return true;
            }
        });

        mAllowMessagesGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowMessagesGCM");

        mAllowMessagesGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowMessages = 1;

                    } else {

                        mAllowMessages = 0;
                    }

                    saveSettings();
                }

                return true;
            }
        });

        mAllowGiftsGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowGiftsGCM");

        mAllowGiftsGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowGifts = 1;

                    } else {

                        mAllowGifts = 0;
                    }

                    saveSettings();
                }

                return true;
            }
        });

        checkAllowMatches(App.getInstance().getAllowMatchesGCM());
        checkAllowLikes(App.getInstance().getAllowLikesGCM());
        checkAllowFollowers(App.getInstance().getAllowFollowersGCM());
        checkAllowMessages(App.getInstance().getAllowMessagesGCM());
        checkAllowGifts(App.getInstance().getAllowGiftsGCM());
        checkAllowComments(App.getInstance().getAllowCommentsGCM());
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            loading = savedInstanceState.getBoolean("loading");

        } else {

            loading = false;
        }

        if (loading) {

            showpDialog();
        }
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("loading", loading);
    }

    public void checkAllowComments(int value) {

        if (value == 1) {

            mAllowCommentsGCM.setChecked(true);
            mAllowComments = 1;

        } else {

            mAllowCommentsGCM.setChecked(false);
            mAllowComments = 0;
        }
    }

    public void checkAllowMatches(int value) {

        if (value == 1) {

            mAllowMatchesGCM.setChecked(true);
            mAllowMatches = 1;

        } else {

            mAllowMatchesGCM.setChecked(false);
            mAllowMatches = 0;
        }
    }

    public void checkAllowLikes(int value) {

        if (value == 1) {

            mAllowLikesGCM.setChecked(true);
            mAllowLikes = 1;

        } else {

            mAllowLikesGCM.setChecked(false);
            mAllowLikes = 0;
        }
    }

    public void checkAllowFollowers(int value) {

        if (value == 1) {

            mAllowFollowersGCM.setChecked(true);
            mAllowFollowers = 1;

        } else {

            mAllowFollowersGCM.setChecked(false);
            mAllowFollowers = 0;
        }
    }

    public void checkAllowMessages(int value) {

        if (value == 1) {

            mAllowMessagesGCM.setChecked(true);
            mAllowMessages = 1;

        } else {

            mAllowMessagesGCM.setChecked(false);
            mAllowMessages = 0;
        }
    }

    public void checkAllowGifts(int value) {

        if (value == 1) {

            mAllowGiftsGCM.setChecked(true);
            mAllowGifts = 1;

        } else {

            mAllowGiftsGCM.setChecked(false);
            mAllowGifts = 0;
        }
    }

    public void saveSettings() {

        App.getInstance().setAllowLikesGCM(mAllowLikes);
        App.getInstance().setAllowGiftsGCM(mAllowGifts);
        App.getInstance().setAllowMessagesGCM(mAllowMessages);
        App.getInstance().setAllowCommentsGCM(mAllowComments);
        App.getInstance().setAllowFollowersGCM(mAllowFollowers);
        App.getInstance().setAllowMatchesGCM(mAllowMatches);

        App.getInstance().saveData();
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}