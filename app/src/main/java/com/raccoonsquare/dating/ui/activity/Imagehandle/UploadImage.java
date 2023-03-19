package com.thinkin_service.provider.ui.activity.Imagehandle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.thinkin_service.provider.R;
import com.thinkin_service.provider.base.BaseActivity;
import com.thinkin_service.provider.data.network.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.thinkin_service.provider.MvpApplication.DATUM;
import static com.thinkin_service.provider.MvpApplication.afterImage;
import static com.thinkin_service.provider.MvpApplication.beforeImage;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class UploadImage extends BaseActivity implements UploadIView, View.OnClickListener {
    Unbinder unbinder;

    @BindView(R.id.imgBeforeService)
    ImageView imgBeforeService;
    @BindView(R.id.imgAfterService)
    ImageView imgAfterService;
    @BindView(R.id.txtServiceComments)
    EditText comment;
    @BindView(R.id.backArrow)
    ImageView backArrow;
    private int CAM=104;
    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;


    String isPaid = "", paymentMode = "", strTag = "";
    File mFileBefore, mFileAfter;
    private static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath = "", strServiceType = "";

    UploadImageIIPresenter presenter =new UploadImageIIPresenter();




    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.backArrow:{

               // lnrServicePhoto.setVisibility(View.GONE);

                break;
            }
            case R.id.imgBeforeService:{

                strTag="save_before";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, this);
                } else {
                    takePhotoFromCamera();
                }
            }
            break;
            case R.id.imgAfterService:
                strTag="save_after";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, this);
                } else {
                    takePhotoFromCamera();
                }

                break;

        }
    }


    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAM);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            takePhotoFromCamera();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         ////////////////////////image picker/////////////////
        Uri imageUri;
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == CAM) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri( this,thumbnail);
            String imgPath= getRealPathFromUri(this,imageUri);

            mFileBefore= new File(imgPath);
            if (strTag.equalsIgnoreCase("save_before")) {
                imgBeforeService.setImageURI(imageUri);
                beforeImage=imageUri;

                presenter.uploadImageApi("before",comment.getText().toString(),mFileBefore,String.valueOf(DATUM.getId()));
            } else if (strTag.equalsIgnoreCase("save_after")){
                imgAfterService.setImageURI(imageUri);
                afterImage=imageUri;
                presenter.uploadImageApi("after",comment.getText().toString(),mFileBefore,String.valueOf(DATUM.getId()));
            }

            Log.e("########## imagepath",""+imgPath);
            //     Log.e("Image Path............", "" + imageFilesPath);
//                getImageUrl(getRealPathFromUri(this,contentURI));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onSuccess(JsonObject object) {

    }

    @Override
    public Activity activity() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_image;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Image");
         imgAfterService.setOnClickListener(this);
        imgBeforeService.setOnClickListener(this);

        if (beforeImage!=null){
            imgBeforeService.setImageURI(beforeImage);

        }if (afterImage!=null){
            imgAfterService.setImageURI(afterImage);

        }


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onErrorRefreshToken(Throwable throwable) {

    }

    @Override
    public void onSuccessRefreshToken(User user) {

    }

    @Override
    public void onSuccessLogout(Object object) {

    }

    @Override
    public void onError(Throwable e) {
        Log.e("hey", "onError: "+e);
        Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onWaitingTimeSuccess(JsonObject jsonObject) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

    }
}
