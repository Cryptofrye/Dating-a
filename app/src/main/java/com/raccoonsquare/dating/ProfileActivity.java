package com.raccoonsquare.dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raccoonsquare.dating.common.ActivityBase;
import com.raccoonsquare.dating.dialogs.ProfileBlockDialog;
import com.raccoonsquare.dating.dialogs.ProfileReportDialog;


public class ProfileActivity extends ActivityBase implements ProfileReportDialog.AlertPositiveListener, ProfileBlockDialog.AlertPositiveListener {

    Toolbar mToolbar;

    FloatingActionButton mFabButton;

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFabButton = findViewById(R.id.fabButton);

        if (savedInstanceState != null) {

            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

        } else {

            fragment = new ProfileFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {

        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProfileReport(int position) {

        ProfileFragment p = (ProfileFragment) fragment;
        p.onProfileReport(position);
    }

    @Override
    public void onProfileBlock() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.onProfileBlock();
    }

    @Override
    public void onBackPressed(){

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

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
