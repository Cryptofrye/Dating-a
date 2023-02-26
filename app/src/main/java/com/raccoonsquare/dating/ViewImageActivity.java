package com.raccoonsquare.dating;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.MenuItem;

import com.raccoonsquare.dating.common.ActivityBase;
import com.raccoonsquare.dating.dialogs.CommentActionDialog;
import com.raccoonsquare.dating.dialogs.CommentDeleteDialog;
import com.raccoonsquare.dating.dialogs.MixedCommentActionDialog;
import com.raccoonsquare.dating.dialogs.MyCommentActionDialog;
import com.raccoonsquare.dating.dialogs.MyPhotoActionDialog;
import com.raccoonsquare.dating.dialogs.PhotoActionDialog;
import com.raccoonsquare.dating.dialogs.PhotoDeleteDialog;
import com.raccoonsquare.dating.dialogs.PhotoReportDialog;


public class ViewImageActivity extends ActivityBase implements CommentDeleteDialog.AlertPositiveListener, CommentActionDialog.AlertPositiveListener, MyCommentActionDialog.AlertPositiveListener, PhotoDeleteDialog.AlertPositiveListener, PhotoReportDialog.AlertPositiveListener, MyPhotoActionDialog.AlertPositiveListener, PhotoActionDialog.AlertPositiveListener, MixedCommentActionDialog.AlertPositiveListener {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_image);

        if (savedInstanceState != null) {

            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

        } else {

            fragment = new ViewImageFragment();
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
    protected void onPause() {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.hideEmojiKeyboard();

        super.onPause();
    }

    @Override
    public void onPhotoDelete(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onPhotoDelete(position);
    }

    @Override
    public void onPhotoReport(int position, int reasonId) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onPhotoReport(position, reasonId);
    }

    @Override
    public void onPhotoRemoveDialog(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.remove(position);
    }

    @Override
    public void onPhotoReportDialog(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.report(position);
    }

    @Override
    public void onCommentRemove(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onCommentRemove(position);
    }

    @Override
    public void onCommentDelete(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onCommentDelete(position);
    }

    @Override
    public void onCommentReply(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onCommentReply(position);
    }

    @Override
    public void onBackPressed(){

        finish();
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
