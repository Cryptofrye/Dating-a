package com.thinkin_service.provider.ui.fragment.status_flow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chaos.view.PinView;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.theartofdev.edmodo.cropper.CropImage;
import com.thinkin_service.provider.BuildConfig;
import com.thinkin_service.provider.MvpApplication;
import com.thinkin_service.provider.R;
import com.thinkin_service.provider.base.BaseFragment;
import com.thinkin_service.provider.common.Constants;
import com.thinkin_service.provider.common.SharedHelper;
import com.thinkin_service.provider.common.Utilities;
import com.thinkin_service.provider.common.chat.ChatActivity;
import com.thinkin_service.provider.data.network.model.Request_;
import com.thinkin_service.provider.data.network.model.TimerResponse;
import com.thinkin_service.provider.data.network.model.TripResponse;
import com.thinkin_service.provider.ui.activity.Imagehandle.UploadImage;
import com.thinkin_service.provider.ui.activity.main.MainActivity;
import com.thinkin_service.provider.ui.bottomsheetdialog.cancel.CancelDialogFragment;
import com.yalantis.ucrop.UCrop;

import android.support.v4.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.thinkin_service.provider.MvpApplication.DATUM;
import static com.thinkin_service.provider.MvpApplication.getInstance;
import static com.thinkin_service.provider.MvpApplication.mLastKnownLocation;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class StatusFlowFragment extends BaseFragment implements StatusFlowIView, View.OnClickListener {

    String isPaid = "", paymentMode = "", strTag = "";
    File mFileBefore, mFileAfter;
    private static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath = "", strServiceType = "";


    ImageView beforeImg,afterImg;

    protected boolean waitingTimeStatus;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_rating)
    AppCompatRatingBar userRating;
    @BindView(R.id.imgCall)
    ImageView imgCall;
    @BindView(R.id.imgGotoPhoto)
    ImageView imgGotoPhoto;
    @BindView(R.id.imgBeforeService)
    ImageView imgBeforeService;
    @BindView(R.id.imgAfterService)
    ImageView imgAfterService;
    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.lnrServicePhoto)
    LinearLayout lnrServicePhoto;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnStatus)
    Button btnStatus;
    @BindView(R.id.status_arrived_img)
    ImageView statusArrivedImg;
    @BindView(R.id.status_picked_up_img)
    ImageView statusPickedUpImg;
    @BindView(R.id.status_finished_img)
    ImageView statusFinishedImg;
    @BindView(R.id.user_img)
    CircleImageView userImg;
    @BindView(R.id.imgMsg)
    ImageView imgMsg;
    @BindView(R.id.arrived_view)
    View arrivedView;
    @BindView(R.id.btWaitingTime)
    Button btWaitingTime;
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    @BindView(R.id.llWaitingTimeContainer)
    LinearLayout llWaitingTimeContainer;
    Unbinder unbinder;
    private StatusFlowPresenter presenter = new StatusFlowPresenter();
    private Context thisContext;
    private AlertDialog addTollDialog, otpDialog;
    private Double tollAmount;
    private Request_ data = null;
    private TripResponse tripResponse = null;
    private String STATUS = "";
    public int PERMISSIONS_REQUEST_PHONE = 4;
    private final int REQUEST_STORAGE_PERMISSION_CODE = 3;
    private final int REQUEST_LOCATION_PERMISSION_CODE = 4;


    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int CAMERA_REQUEST = 0x78;

    private String imgPath;

    public static final int CAPTURE_IMAGE_REQ_CODE = 200;

    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;


    private Handler customHandler = new Handler();
    private long timerInHandler = 0L;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timerInHandler++;
            secondSplitUp(timerInHandler, tvTimer);
            customHandler.postDelayed(this, 1000);
        }
    };
    private int CAM=4;
    private Dialog dialogFloating;

    @Override
    public Fragment fragmentInstance() {
        return StatusFlowFragment.this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_status_flow;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        this.thisContext = getContext();
        init();
        return view;
    }

    private void init() {
        data = DATUM;

        tripResponse = MvpApplication.tripResponse;
        imgGotoPhoto.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        imgAfterService.setOnClickListener(this);
        imgBeforeService.setOnClickListener(this);
        if (data != null && data.getStatus() != null) {
            Utilities.printV("data===>", data.getStatus());
            LatLng currentLocation;
            changeFlow(data.getStatus());
            LatLng origin = new LatLng(data.getSLatitude(), data.getSLongitude());
            LatLng destination = new LatLng(data.getDLatitude(), data.getDLongitude());
            if (tripResponse != null && tripResponse.getProviderDetails() != null)
                currentLocation = new LatLng(tripResponse.getProviderDetails().getLatitude(),
                        tripResponse.getProviderDetails().getLongitude());
            else
                currentLocation = new LatLng(Double.parseDouble(SharedHelper.getKey(getContext(), "latitude")),
                        Double.parseDouble(SharedHelper.getKey(getContext(), "longitude")));
            if (data.getStatus().equalsIgnoreCase(Constants.checkStatus.ACCEPTED) ||
                    data.getStatus().equalsIgnoreCase(Constants.checkStatus.STARTED))
                ((MainActivity) getContext()).drawRoute(currentLocation, origin);
            else ((MainActivity) getContext()).drawRoute(origin, destination);


        }

    }

    @OnClick({R.id.imgCall, R.id.sos, R.id.btnCancel, R.id.btnStatus, R.id.imgMsg, R.id.btWaitingTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCall:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getUser().getMobile()));
                startActivity(intent);
                break;
            case R.id.sos:
                sos();
                break;
            case R.id.btnCancel:
                SharedHelper.putKey(thisContext, Constants.SharedPref.CANCEL_ID, String.valueOf(data.getId()));
                cancelRequestPopup();
                break;
            case R.id.btnStatus:
                if (STATUS.equalsIgnoreCase(Constants.checkStatus.PICKEDUP) && Constants.showOTP)
                    showOTP();
                else if (STATUS.equalsIgnoreCase(Constants.checkStatus.DROPPED)&& Constants.showTOLL)
                    showAddTollDialog();
                else statusUpdateCall(STATUS);
                break;
            case R.id.btWaitingTime:
                waitingTimeStatus = !waitingTimeStatus;
                presenter.waitingTime(waitingTimeStatus ? "1" : "0", String.valueOf(data.getId()));
                break;
            case R.id.imgMsg:
                if (DATUM != null) {
                    Intent i = new Intent(thisContext, ChatActivity.class);
                    i.putExtra(Constants.SharedPref.REQUEST_ID, String.valueOf(DATUM.getId()));
                    startActivity(i);
                }
                break;
        }

    }
    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            //   dispatchTakePictureIntent();
        }
    }

   /* private void takePhoto() {
        imgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + SystemClock.currentThreadTimeMillis() + ".jpeg";
        File file = new File(imgPath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = FileProvider.getUriForFile(
                getContext(), getContext().getPackageName() + ".provider", file);
        //intent.setDataAndType(imageUri,MediaStore.EXTRA_OUTPUT);
        List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getActivity().grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setClipData(ClipData.newRawUri(null, imageUri));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_REQ_CODE);
    }*/


    protected boolean verifyStoragePermissions() {
        return verifyPermission(getActivity(), READ_EXTERNAL_STORAGE)
                && verifyPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
    }
    public static boolean verifyPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(getInstance(), permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void onEditProfilePictureClick() {
        if (!verifyStoragePermissions()) {
            initRuntimePermissions();
        } else {
            initImageSelector();
        }
    }


    private void initRuntimePermissions() {
        requestStoragePermissions();
    }
    private void initImageSelector() {
        CropImage.activity().setAspectRatio(1, 1)
                .start((MainActivity)mcontext);
    }



    Context mcontext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext=context;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File file=new File(resultUri.toString());
                imgAfterService.setImageURI(resultUri);
                // if (file!=null)
                //  saveImageApiData(resultUri.getPath());


                // setImageInImageView(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String imgPath= getRealPathFromUri(getActivity(),resultUri);
                File file=new File(resultUri.toString());
                if (strTag.equalsIgnoreCase("save_before")) {
                    beforeImg.setImageURI(resultUri);
                    SharedHelper.beforeimage=resultUri;
                    mFileBefore= new File(imgPath);
                    if (mFileBefore!=null)
                        presenter.uploadImageApi("",mFileBefore,String.valueOf(DATUM.getId()));

                } else if (strTag.equalsIgnoreCase("save_after")){
                    afterImg.setImageURI(resultUri);
                    mFileAfter= new File(imgPath);
                    SharedHelper.afterImage=resultUri;
                }
                //   setImageInImageView(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
*/

    public void requestStoragePermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
        }, REQUEST_STORAGE_PERMISSION_CODE);
    }



    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAM);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAM);
        }
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        lnrServicePhoto.setVisibility(View.VISIBLE);
        ////////////////////////image picker/////////////////
        Uri imageUri;
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == CAM) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(getActivity(), thumbnail);
           String imgPath= getRealPathFromUri(getActivity(),imageUri);
            if (strTag.equalsIgnoreCase("save_before")) {
                beforeImg.setImageURI(imageUri);
                SharedHelper.beforeimage=imageUri;
                mFileBefore= new File(imgPath);
                if (mFileBefore!=null)
                    presenter.uploadImageApi("",mFileBefore,String.valueOf(DATUM.getId()));

            } else if (strTag.equalsIgnoreCase("save_after")){
                afterImg.setImageURI(imageUri);
                mFileAfter= new File(imgPath);
                SharedHelper.afterImage=imageUri;
            }
            Log.e("########## imagepath",""+imgPath);
            //     Log.e("Image Path............", "" + imageFilesPath);
//                getImageUrl(getRealPathFromUri(this,contentURI));
        }
    }
*/

    private void cropImage(String path) {
        Uri uri = Uri.fromFile(new File(path));

        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        options.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        UCrop.of(uri, uri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(384, 384)
                .withOptions(options)
                .start(getActivity());
    }

    Bitmap getScaledBitmap(Bitmap mBitmap) {
        Bitmap scaled = null;
        try {
            int nh = (int) (mBitmap.getHeight() * (512.0 / mBitmap.getWidth()));
            scaled = Bitmap.createScaledBitmap(mBitmap, 512, nh, true);
            return scaled;
        } catch (Exception error) {
            error.printStackTrace();
        }
        return scaled;
    }


    @SuppressLint("SetTextI18n")
    public void changeFlow(String status) {

        btnCancel.setVisibility(View.GONE);
        userName.setText(data.getUser().getFirstName() + " " + data.getUser().getLastName());
        userRating.setRating(Float.parseFloat(data.getUser().getRating()));

        Glide.with(thisContext).
                load(BuildConfig.BASE_IMAGE_URL +
                        data.getUser().getPicture()).
                apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).
                        dontAnimate().error(R.drawable.ic_user_placeholder)).into(userImg);

        if (Constants.checkStatus.PICKEDUP.equalsIgnoreCase(status)) {
            imgMsg.setVisibility(View.GONE);
            llWaitingTimeContainer.setVisibility(View.VISIBLE);
        } else {
            imgMsg.setVisibility(View.VISIBLE);
            llWaitingTimeContainer.setVisibility(View.GONE);
        }

        switch (status) {
            case Constants.checkStatus.ACCEPTED:
                btnStatus.setText(getString(R.string.arrived));
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = Constants.checkStatus.STARTED;
                break;
            case Constants.checkStatus.STARTED:
                btnStatus.setText(getString(R.string.arrived));
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = Constants.checkStatus.ARRIVED;
                break;
            case Constants.checkStatus.ARRIVED:
                btnStatus.setText(getString(R.string.pick_up_invoice));
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = Constants.checkStatus.PICKEDUP;
                statusArrivedImg.setImageResource(R.drawable.ic_arrived_select);
                statusPickedUpImg.setImageResource(R.drawable.ic_pickup);
                statusFinishedImg.setImageResource(R.drawable.ic_finished);
                break;
            case Constants.checkStatus.PICKEDUP:
                waitingTimeStatus = !waitingTimeStatus;
                presenter.waitingTime(waitingTimeStatus ? "1" : "0", String.valueOf(data.getId()));

                arrivedView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryText));
                btnStatus.setBackgroundResource(R.drawable.button_round_primary);
                btnStatus.setText(getString(R.string.tap_when_dropped));
                STATUS = Constants.checkStatus.DROPPED;
                statusArrivedImg.setImageResource(R.drawable.ic_arrived_select);
                statusPickedUpImg.setImageResource(R.drawable.ic_pickup_select);
                statusFinishedImg.setImageResource(R.drawable.ic_finished);
                break;
            default:
                break;
        }
    }

    private void sos() {
        new AlertDialog.Builder(getContext())
                .setTitle(getContext().getResources().getString(R.string.sos_alert))
                .setMessage(R.string.are_sure_you_want_to_emergency_alert)
                .setCancelable(true)
                .setPositiveButton(getContext().getResources().getString(R.string.yes), (dialog, which) -> callPhoneNumber(Constants.userSos))
                .setNegativeButton(getContext().getResources().getString(R.string.no), (dialog, which) -> dialog.cancel())
                .show();
    }

    private void callPhoneNumber(String mobileNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobileNumber));
        startActivity(intent);
    }

    @Override
    public void onSuccess(Object object) {
        hideLoading();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .remove(StatusFlowFragment.this).commit();
        getContext().sendBroadcast(new Intent("INTENT_FILTER"));
    }

    @Override
    public void onWaitingTimeSuccess(TimerResponse object) {
        timerInHandler = object.getWaitingTime();
        secondSplitUp(timerInHandler, tvTimer);
        if (object.getWaitingStatus() == 1) customHandler.postDelayed(updateTimerThread, 1000);
        else customHandler.removeCallbacks(updateTimerThread);
    }

    public void secondSplitUp(long biggy, TextView tvTimer) {
        int hours = (int) biggy / 3600;
        int sec = (int) biggy - hours * 3600;
        int mins = sec / 60;
        sec = sec - mins * 60;
        tvTimer.setText(String.format("%02d:", hours)
                + String.format("%02d:", mins)
                + String.format("%02d", sec));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        try {
            if (e != null)
                onErrorBase(e);
        } catch (Exception throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onWaitingTimeSuccess(JsonObject jsonObject) {
        if (jsonObject!=null){

        }

    }

    void statusUpdateCall(String status) {
        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", status);
            map.put("_method", "PATCH");
            if (tollAmount != null)
                map.put("toll_price", tollAmount);
            if (mLastKnownLocation != null) {
                map.put("latitude", mLastKnownLocation.getLatitude());
                map.put("longitude", mLastKnownLocation.getLongitude());
            }
            showLoading();
            presenter.statusUpdate(map, datum.getId());
        }
    }

    void cancelRequestPopup() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisContext);
            alertDialogBuilder
                    .setMessage(thisContext.getResources().getString(R.string.cancel_request_title))
                    .setCancelable(false)
                    .setPositiveButton(thisContext.getResources().getString(R.string.yes), (dialog, id) -> {
                        try {
                            CancelDialogFragment cancelDialogFragment = new CancelDialogFragment();
                            cancelDialogFragment.show(getActivity().getSupportFragmentManager(), cancelDialogFragment.getTag());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).setNegativeButton(thisContext.getResources().getString(R.string.no), (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.otp_dialog, null);

        Button submitBtn = view.findViewById(R.id.submit_btn);
        final PinView pinView = view.findViewById(R.id.pinView);

        builder.setView(view);
        otpDialog = builder.create();
        otpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        submitBtn.setOnClickListener(view1 -> {

            if (data.getOtp().equalsIgnoreCase(pinView.getText().toString())) {
                try {
                    if (thisContext != null)
                        Toasty.success(thisContext, thisContext.getResources().getString(R.string.otp_verified), Toast.LENGTH_SHORT).show();
                    statusUpdateCall(STATUS);
                    otpDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else try {
                if (thisContext != null && isAdded())
                    Toasty.error(thisContext, thisContext.getResources().getString(R.string.otp_wrong), Toast.LENGTH_SHORT).show();
                else
                    Toasty.error(thisContext, thisContext.getResources().getString(R.string.otp_wrong), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        otpDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.checkWaitingTime(""+data.getId());
    }

    private void showAddTollDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_toll, null);

        Button submitBtn = view.findViewById(R.id.submit_btn);
        Button dismiss = view.findViewById(R.id.dismiss);
        final EditText toll_amount = view.findViewById(R.id.toll_amount);
        final TextView currency_type = view.findViewById(R.id.currency_type);

        currency_type.setText(Constants.Currency);

        builder.setView(view);
        addTollDialog = builder.create();
        Objects.requireNonNull(addTollDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        submitBtn.setOnClickListener(view1 -> {
            if (!toll_amount.getText().toString().trim().equalsIgnoreCase("")
                    && Double.parseDouble(toll_amount.getText().toString().trim()) > 0)
                tollAmount = Double.valueOf(toll_amount.getText().toString());
            else tollAmount = Double.valueOf("0");
            customHandler.removeCallbacks(updateTimerThread);
            statusUpdateCall(STATUS);
            addTollDialog.dismiss();
        });

        dismiss.setOnClickListener(v -> {
            tollAmount = null;
            addTollDialog.dismiss();
            statusUpdateCall(STATUS);
        });

        addTollDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customHandler.removeCallbacks(updateTimerThread);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnServiceStatus:
                if (mFileAfter!=null){

                    presenter.uploadImageApi("",mFileAfter,String.valueOf(DATUM.getId()));

                }else {
                    if (mFileBefore!=null)
                        presenter.uploadImageApi("",mFileBefore,String.valueOf(DATUM.getId()));

                }

                break;
            case R.id.imgGotoPhoto:{

                startActivity(new Intent(mcontext, UploadImage.class));
                // OPenDialogFloat(getContext());


                break;
            }
            case R.id.backArrow:{

                lnrServicePhoto.setVisibility(View.GONE);

                break;
            }


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



    public void OPenDialogFloat(Context context) {

        try {

            dialogFloating = new Dialog(context);
            dialogFloating.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogFloating.setCancelable(false);
            dialogFloating.setContentView(R.layout.activity_upload_image);
            dialogFloating.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogFloating.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialogFloating.getWindow().setGravity(Gravity.CENTER);
            dialogFloating.setTitle("");
            TextView cancelTextView, Apply;
            ImageView imageView;
            beforeImg = dialogFloating.findViewById(R.id.imgBeforeService);

            imageView = dialogFloating.findViewById(R.id.backArrow);

            afterImg = dialogFloating.findViewById(R.id.imgAfterService);
            Apply = dialogFloating.findViewById(R.id.btnServiceStatus);
            if (SharedHelper.beforeimage!=null){
                beforeImg.setImageURI(SharedHelper.beforeimage);
            }
            if (SharedHelper.afterImage!=null){
                afterImg.setImageURI(SharedHelper.afterImage);
            }


            beforeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strTag="save_before";
                    onEditProfilePictureClick();

                }
            });
            afterImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strTag="save_after";
                    onEditProfilePictureClick();

                }
            });



            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogFloating.dismiss();


                }
            });

            Apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });


            dialogFloating.show();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
    }

}