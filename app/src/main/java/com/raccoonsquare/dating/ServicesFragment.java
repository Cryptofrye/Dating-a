package com.raccoonsquare.dating;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.util.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ServicesFragment extends Fragment implements Constants {

    CallbackManager callbackManager;

    LoginButton loginButton;

    private ProgressDialog pDialog;

    CardView mGoogleCard, mFacebookCard;
    Button mFacebookDisconnectBtn, mGoogleDisconnectBtn;
    TextView mFacebookPrompt, mGooglePrompt;

    SignInButton mGoogleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSigninActivityResultLauncher;

    String oauth_id = "";

    private Boolean loading = false;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (AccessToken.getCurrentAccessToken()!= null) LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        googleSigninActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {

                    // There are no request codes
                    Intent data = result.getData();

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                    try {

                        GoogleSignInAccount account = task.getResult(ApiException.class);

                        oauth_id = account.getId();

                        // Signed in successfully, show authenticated UI.

                        googleOauth("connect");

                    } catch (ApiException e) {

                        // The ApiException status code indicates the detailed failure reason.
                        // Please refer to the GoogleSignInStatusCodes class reference for more information.
                        Log.e("Google", "Google sign in failed", e);
                    }
                }
            }
        });

        //

        initpDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_services, container, false);

        if (loading) {

            showpDialog();
        }

        mGoogleCard = rootView.findViewById(R.id.google_card);
        mFacebookCard = rootView.findViewById(R.id.facebook_card);

        mGooglePrompt = (TextView) rootView.findViewById(R.id.google_sub_label);
        mFacebookPrompt = (TextView) rootView.findViewById(R.id.facebook_sub_label);

        mFacebookDisconnectBtn = rootView.findViewById(R.id.facebook_disconnect_button);
        mGoogleDisconnectBtn = (Button) rootView.findViewById(R.id.google_disconnect_button);

        mFacebookDisconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disconnectFromFacebook();
            }
        });

        mGoogleDisconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleOauth("disconnect");
            }
        });

        // Google Button

        mGoogleSignInButton = rootView.findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);

        setGooglePlusButtonText(mGoogleSignInButton, getString(R.string.action_connect_with_google));

        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                googleSigninActivityResultLauncher.launch(signInIntent);
            }
        });

        // Facebook Button

        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setPermissions("public_profile");
        loginButton.setFragment(this);

        // Registering CallbackManager with the LoginButton
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                // Retrieving access token using the LoginResult
                AccessToken accessToken = loginResult.getAccessToken();

                useLoginInformation(accessToken);
            }

            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {

            }
        });

        updateView();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void useLoginInformation(AccessToken accessToken) {

        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/

        showpDialog();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {

                    if (object.has("id")) {

                        oauth_id = object.getString("id");
                    }

                } catch (JSONException e) {

                    Log.e("Facebook Login", "Could not parse malformed JSON: \"" + object.toString() + "\"");

                } finally {

                    if (AccessToken.getCurrentAccessToken() != null) LoginManager.getInstance().logOut();

                    if (!oauth_id.equals("")) {

                        connectToFacebook();

                    } else {

                        hidepDialog();
                    }
                }
            }
        });

        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        // parameters.putString("fields", "id,name,email,picture.width(200)");
        parameters.putString("fields", "id, name");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    private void updateView() {

        if (FACEBOOK_AUTHORIZATION) {

            mFacebookCard.setVisibility(View.VISIBLE);

            if (App.getInstance().getFacebookId().length() > 4) {

                loginButton.setVisibility(View.GONE);
                mFacebookDisconnectBtn.setVisibility(View.VISIBLE);
                mFacebookPrompt.setText(getString(R.string.label_account_connected_to_facebook));

            } else {

                loginButton.setVisibility(View.VISIBLE);
                mFacebookDisconnectBtn.setVisibility(View.GONE);
                mFacebookPrompt.setText(getString(R.string.label_account_connect_to_facebook));
            }

        } else {

            mFacebookCard.setVisibility(View.GONE);
        }

        if (GOOGLE_AUTHORIZATION) {

            mGoogleCard.setVisibility(View.VISIBLE);

            if (App.getInstance().getGoogleFirebaseId().length() > 0) {

                mGoogleSignInButton.setVisibility(View.GONE);
                mGoogleDisconnectBtn.setVisibility(View.VISIBLE);
                mGooglePrompt.setText(getString(R.string.label_account_connected_to_google));

            } else {

                mGoogleSignInButton.setVisibility(View.VISIBLE);
                mGoogleDisconnectBtn.setVisibility(View.GONE);
                mGooglePrompt.setText(getString(R.string.label_account_connect_to_google));
            }

        } else {

            mGoogleCard.setVisibility(View.GONE);
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {

        for (int i = 0; i < signInButton.getChildCount(); i++) {

            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {

                TextView tv = (TextView) v;
                tv.setTextSize(15);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);

                return;
            }
        }
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
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
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void googleOauth(String action) {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_GOOGLE_AUTH, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.has("error")) {

                                if (!response.getBoolean("error")) {

                                    if (action.equals("connect")) {

                                        Toast.makeText(getActivity(), getString(R.string.msg_connect_to_google_success), Toast.LENGTH_SHORT).show();
                                        App.getInstance().setGoogleFirebaseId(oauth_id);
                                    }

                                    if (action.equals("disconnect")) {

                                        Toast.makeText(getActivity(), getString(R.string.msg_connect_to_google_removed), Toast.LENGTH_SHORT).show();
                                        App.getInstance().setGoogleFirebaseId("");
                                    }

                                } else {

                                    Toast.makeText(getActivity(), getString(R.string.msg_connect_to_google_error), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            oauth_id = "";

                            updateView();

                            loading = false;

                            hidepDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                oauth_id = "";

                Toast.makeText(getActivity(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("client_id", CLIENT_ID);

                params.put("access_token", App.getInstance().getAccessToken());
                params.put("account_id", Long.toString(App.getInstance().getId()));

                params.put("app_type", Integer.toString(APP_TYPE_ANDROID));
                params.put("action", action);
                params.put("uid", oauth_id);

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void disconnectFromFacebook() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_DISCONNECT_FROM_FACEBOOK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.has("error")) {

                                if (!response.getBoolean("error")) {

                                    App.getInstance().setFacebookId("");

                                    Toast.makeText(getActivity(), getString(R.string.msg_connect_to_facebook_removed), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            updateView();

                            loading = false;

                            hidepDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("clientId", CLIENT_ID);
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("accountId", Long.toString(App.getInstance().getId()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void connectToFacebook() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_CONNECT_TO_FACEBOOK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.has("error")) {

                                if (!response.getBoolean("error")) {

                                    if (response.has("error_code")) {

                                        if (response.getInt("error_code") == ERROR_FACEBOOK_ID_TAKEN) {

                                            Toast.makeText(getActivity(), getString(R.string.msg_connect_to_facebook_error), Toast.LENGTH_SHORT).show();

                                        } else {

                                            App.getInstance().setFacebookId(oauth_id);

                                            Toast.makeText(getActivity(), getString(R.string.msg_connect_to_facebook_success), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            oauth_id = "";

                            updateView();

                            loading = false;

                            hidepDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                oauth_id = "";

                Toast.makeText(getActivity(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", oauth_id);
                params.put("clientId", CLIENT_ID);
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("accountId", Long.toString(App.getInstance().getId()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
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