package com.raccoonsquare.dating.util;

import android.Manifest;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.raccoonsquare.dating.R;
import com.raccoonsquare.dating.app.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper extends Application {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    private AppCompatActivity activity;
    private Context context;

    public Helper(Context current){

        this.context = current;
    }

    public Helper(AppCompatActivity activity) {

        this.activity = activity;
    }

    public Helper(FragmentActivity activity) {


    }

    public boolean checkPermission(String permission) {

        if (ContextCompat.checkSelfPermission(App.getInstance().getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) {

            return true;
        }

        return false;
    }

    public boolean checkStoragePermission() {

        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {

            permission = Manifest.permission.READ_MEDIA_IMAGES;
        }

        if (ContextCompat.checkSelfPermission(App.getInstance().getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) {

            return true;
        }

        return false;
    }

    public boolean checkVideoPermission() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(App.getInstance().getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {

                return true;
            }

        } else {

            return true;
        }

        return false;
    }

    private Bitmap resizeImg(Uri filename) throws IOException {

        int maxWidth = 1200;
        int maxHeight = 1200;

        // create the options
        BitmapFactory.Options opts = new BitmapFactory.Options();

        BitmapFactory.decodeFile(getRealPath(filename), opts);

        //get the original size
        int orignalHeight = opts.outHeight;
        int orignalWidth = opts.outWidth;

        //opts = new BitmapFactory.Options();

        //just decode the file
        opts.inJustDecodeBounds = true;

        //initialization of the scale
        int resizeScale = 1;

        Log.e("qascript orignalWidth", Integer.toString(orignalWidth));
        Log.e("qascript orignalHeight", Integer.toString(orignalHeight));

        //get the good scale
        if (orignalWidth > maxWidth || orignalHeight > maxHeight) {

            resizeScale = 2;
        }

        //put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;

        //get the future size of the bitmap
        int bmSize = 6000;

        //check if it's possible to store into the vm java the picture
        if (Runtime.getRuntime().freeMemory() > bmSize) {

            //decode the file

            InputStream is = this.context.getContentResolver().openInputStream(filename);
            Bitmap bp = BitmapFactory.decodeStream(is, new Rect(0, 0, 512, 512), opts);
            is.close();

            return bp;

        } else {

            Log.e("qascript", "not resize image");

            return null;
        }
    }

    public void saveImg(Uri filename, String newFilename) {

        String mimeType = "image/jpeg";
        String directory = Environment.DIRECTORY_PICTURES;
        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        try {

            Bitmap bmp = this.resizeImg(filename);

            if (bmp == null) {

                try {

                    bmp = MediaStore.Images.Media.getBitmap(App.getInstance().getApplicationContext().getContentResolver(), filename);

                }  catch (Exception e) {

                    //handle exception

                    Log.e("qascript", "MediaStore error");
                }
            }

            int orientation = 1;

            OutputStream imageOutStream;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, newFilename);
                values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                values.put(MediaStore.Images.Media.RELATIVE_PATH, directory);

                ContentResolver contentResolver = this.context.getContentResolver();

                imageOutStream = contentResolver.openOutputStream(contentResolver.insert(mediaContentUri, values));

                try (InputStream inputStream = context.getContentResolver().openInputStream(filename)) {

                    ExifInterface exif = new ExifInterface(inputStream);

                    orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                } catch (IOException e) {

                    e.printStackTrace();
                }

            } else {

                // File file = new File(Environment.DIRECTORY_PICTURES, inFile);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), newFilename);
                imageOutStream = new FileOutputStream(file);

                ExifInterface exif = new ExifInterface(getRealPath(filename));
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            }

            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:

                    bmp = rotateImage(bmp, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:

                    bmp = rotateImage(bmp, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:

                    bmp = rotateImage(bmp, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:

                default:

                    bmp = bmp;
            }

            bmp.compress(Bitmap.CompressFormat.JPEG, 90, imageOutStream);
            imageOutStream.flush();
            imageOutStream.close();

        } catch (Exception ex) {

            Log.e("qascript saveImg()", ex.getMessage());
        }
    }

    public void saveBmp(Bitmap bmp, String newFilename) {

        String mimeType = "image/jpeg";
        String directory = Environment.DIRECTORY_PICTURES;
        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        try {

            OutputStream imageOutStream;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, newFilename);
                values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                values.put(MediaStore.Images.Media.RELATIVE_PATH, directory);

                ContentResolver contentResolver = this.context.getContentResolver();

                imageOutStream = contentResolver.openOutputStream(contentResolver.insert(mediaContentUri, values));

            } else {

                // File file = new File(Environment.DIRECTORY_PICTURES, inFile);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), newFilename);
                imageOutStream = new FileOutputStream(file);
            }

            bmp.compress(Bitmap.CompressFormat.JPEG, 90, imageOutStream);
            imageOutStream.flush();
            imageOutStream.close();

        } catch (Exception ex) {

            Log.e("qascript saveBmp()", ex.getMessage());

        } finally {

            Log.e("qascript saveBmp()", "success");
        }
    }

    public static String getRealPath(Uri uri) {

        String docId = DocumentsContract.getDocumentId(uri);
        String[] split = docId.split(":");
        String type = split[0];
        Uri contentUri;

        switch (type) {

            case "image":

                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                break;

            case "video":

                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                break;

            case "audio":

                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                break;

            default:

                contentUri = MediaStore.Files.getContentUri("external");
        }

        String selection = "_id=?";
        String[] selectionArgs = new String[]{split[1]};

        return getDataColumn(App.getInstance().getApplicationContext(), contentUri, selection, selectionArgs);
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        String column = "_data";
        String[] projection = {column};

        try {

            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {

                int column_index = cursor.getColumnIndexOrThrow(column);
                String value = cursor.getString(column_index);

                if (value.startsWith("content://") || !value.startsWith("/") && !value.startsWith("file://")) {

                    return null;
                }

                return value;
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (cursor != null) {

                cursor.close();
            }
        }

        return null;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {

        Matrix matrix = new Matrix();

        matrix.postRotate(angle);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void deleteFile(final Context context, final File file) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {

            final String where = MediaStore.MediaColumns.DATA + "=?";

            final String[] selectionArgs = new String[] {
                    file.getAbsolutePath()
            };

            final ContentResolver contentResolver = context.getContentResolver();
            final Uri filesUri = MediaStore.Files.getContentUri("external");

            contentResolver.delete(filesUri, where, selectionArgs);

            if (file.exists()) {

                contentResolver.delete(filesUri, where, selectionArgs);
            }

        } else {

            if (file.exists()) {

                file.delete();
            }
        }
    }

    public static String getGenderTitle(Context ctx, int gender) {

        switch (gender) {

            case 0: {

                return ctx.getString(R.string.label_male);
            }

            case 1: {

                return ctx.getString(R.string.label_female);
            }

            case 2: {

                return ctx.getString(R.string.label_secret);
            }

            default: {

                return ctx.getString(R.string.label_select_gender);
            }
        }
    }

    public static String getSexOrientationTitle(Context ctx, int sex_orientation) {

        switch (sex_orientation) {

            case 1: {

                return ctx.getString(R.string.sex_orientation_1);
            }

            case 2: {

                return ctx.getString(R.string.sex_orientation_2);

            }

            case 3: {

                return ctx.getString(R.string.sex_orientation_3);
            }

            case 4: {

                return ctx.getString(R.string.sex_orientation_4);
            }

            default: {

                return ctx.getString(R.string.label_select_sex_orientation);
            }
        }
    }

    public static int getGalleryGridCount(FragmentActivity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.gallery_item_size);

        return Math.round(screenWidth / cellWidth);
    }

    public static int dpToPx(Context c, int dp) {

        Resources r = c.getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static int getGridSpanCount(FragmentActivity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public static int getStickersGridSpanCount(FragmentActivity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.sticker_item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public static String randomString(int len) {

        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++)

            sb.append(AB.charAt(rnd.nextInt(AB.length())));

        return sb.toString();
    }

    public boolean isValidEmail(String email) {

    	if (TextUtils.isEmpty(email)) {

    		return false;

    	} else {

    		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    	}
    }
    
    public boolean isValidLogin(String login) {

        String regExpn = "^([a-zA-Z]{4,24})?([a-zA-Z][a-zA-Z0-9_]{4,24})$";
        CharSequence inputStr = login;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public boolean isValidSearchQuery(String query) {

        String regExpn = "^([a-zA-Z]{1,24})?([a-zA-Z][a-zA-Z0-9_]{1,24})$";
        CharSequence inputStr = query;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }
    
    public boolean isValidPassword(String password) {

        String regExpn = "^[a-z0-9_]{6,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public static String md5(final String s) {

        final String MD5 = "MD5";

        try {

            // Create MD5 Hash

            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());

            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();

            for (byte aMessageDigest : messageDigest) {

                String h = Integer.toHexString(0xFF & aMessageDigest);

                while (h.length() < 2) h = "0" + h;

                hexString.append(h);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        return "";
    }

    public String getRelationshipStatus(int mRelationship) {

        switch (mRelationship) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getResources().getString(R.string.relationship_status_1);
            }

            case 2: {

                return context.getResources().getString(R.string.relationship_status_2);
            }

            case 3: {

                return context.getResources().getString(R.string.relationship_status_3);
            }

            case 4: {

                return context.getResources().getString(R.string.relationship_status_4);
            }

            case 5: {

                return context.getResources().getString(R.string.relationship_status_5);
            }

            case 6: {

                return context.getResources().getString(R.string.relationship_status_6);
            }

            case 7: {

                return context.getResources().getString(R.string.relationship_status_7);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getPoliticalViews(int mPolitical) {

        switch (mPolitical) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getResources().getString(R.string.political_views_1);
            }

            case 2: {

                return context.getResources().getString(R.string.political_views_2);
            }

            case 3: {

                return context.getResources().getString(R.string.political_views_3);
            }

            case 4: {

                return context.getResources().getString(R.string.political_views_4);
            }

            case 5: {

                return context.getResources().getString(R.string.political_views_5);
            }

            case 6: {

                return context.getResources().getString(R.string.political_views_6);
            }

            case 7: {

                return context.getResources().getString(R.string.political_views_7);
            }

            case 8: {

                return context.getResources().getString(R.string.political_views_8);
            }

            case 9: {

                return context.getResources().getString(R.string.political_views_9);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getWorldView(int mWorld) {

        switch (mWorld) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getResources().getString(R.string.world_view_1);
            }

            case 2: {

                return context.getResources().getString(R.string.world_view_2);
            }

            case 3: {

                return context.getResources().getString(R.string.world_view_3);
            }

            case 4: {

                return context.getResources().getString(R.string.world_view_4);
            }

            case 5: {

                return context.getResources().getString(R.string.world_view_5);
            }

            case 6: {

                return context.getString(R.string.world_view_6);
            }

            case 7: {

                return context.getString(R.string.world_view_7);
            }

            case 8: {

                return context.getString(R.string.world_view_8);
            }

            case 9: {

                return context.getString(R.string.world_view_9);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getPersonalPriority(int mPriority) {

        switch (mPriority) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.personal_priority_1);
            }

            case 2: {

                return context.getString(R.string.personal_priority_2);
            }

            case 3: {

                return context.getString(R.string.personal_priority_3);
            }

            case 4: {

                return context.getString(R.string.personal_priority_4);
            }

            case 5: {

                return context.getString(R.string.personal_priority_5);
            }

            case 6: {

                return context.getString(R.string.personal_priority_6);
            }

            case 7: {

                return context.getString(R.string.personal_priority_7);
            }

            case 8: {

                return context.getString(R.string.personal_priority_8);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getImportantInOthers(int mImportant) {

        switch (mImportant) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.important_in_others_1);
            }

            case 2: {

                return context.getString(R.string.important_in_others_2);
            }

            case 3: {

                return context.getString(R.string.important_in_others_3);
            }

            case 4: {

                return context.getString(R.string.important_in_others_4);
            }

            case 5: {

                return context.getString(R.string.important_in_others_5);
            }

            case 6: {

                return context.getString(R.string.important_in_others_6);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getSmokingViews(int mSmoking) {

        switch (mSmoking) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.smoking_views_1);
            }

            case 2: {

                return context.getString(R.string.smoking_views_2);
            }

            case 3: {

                return context.getString(R.string.smoking_views_3);
            }

            case 4: {

                return context.getString(R.string.smoking_views_4);
            }

            case 5: {

                return context.getString(R.string.smoking_views_5);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getAlcoholViews(int mAlcohol) {

        switch (mAlcohol) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.alcohol_views_1);
            }

            case 2: {

                return context.getString(R.string.alcohol_views_2);
            }

            case 3: {

                return context.getString(R.string.alcohol_views_3);
            }

            case 4: {

                return context.getString(R.string.alcohol_views_4);
            }

            case 5: {

                return context.getString(R.string.alcohol_views_5);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getLooking(int mLooking) {

        switch (mLooking) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.you_looking_1);
            }

            case 2: {

                return context.getString(R.string.you_looking_2);
            }

            case 3: {

                return context.getString(R.string.you_looking_3);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getGenderLike(int mLike) {

        switch (mLike) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.profile_like_1);
            }

            case 2: {

                return context.getString(R.string.profile_like_2);
            }

            default: {

                break;
            }
        }

        return "-";
    }
}
