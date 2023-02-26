package com.raccoonsquare.dating;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import com.raccoonsquare.dating.common.ActivityBase;


public class PrivacySettingsActivity extends ActivityBase {

    Toolbar mToolbar;

    PreferenceFragmentCompat fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null) {

            fragment = (PreferenceFragmentCompat) getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

        } else {

            fragment = new PrivacySettingsFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.settings_content, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.\

        switch (item.getItemId()) {

            case android.R.id.home: {

                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }
}
