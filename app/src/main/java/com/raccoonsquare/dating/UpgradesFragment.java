package com.raccoonsquare.dating;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.raccoonsquare.dating.app.App;
import com.raccoonsquare.dating.constants.Constants;
import com.raccoonsquare.dating.util.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpgradesFragment extends Fragment implements Constants {

    private ProgressDialog pDialog;

    Button mGetCreditsButton, mGhostModeButton, mVerifiedBadgeButton, mDisableAdsButton, mProModeButton, mMessagePackageButton;
    TextView mLabelCredits, mLabelGhostModeStatus, mLabelVerifiedBadgeStatus, mLabelDisableAdsStatus, mLabelProModeStatus, mLabelProModeTitle, mLabelMessagePackageStatus;
    LinearLayout mMessagePackageContainer;

    private Boolean loading = false;

    public UpgradesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initpDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_upgrades, container, false);

        if (loading) {

            showpDialog();
        }

        mLabelCredits = rootView.findViewById(R.id.labelCredits);

        mMessagePackageContainer = rootView.findViewById(R.id.messagePackageContainer);

        mLabelGhostModeStatus = rootView.findViewById(R.id.labelGhostModeStatus);
        mLabelVerifiedBadgeStatus = rootView.findViewById(R.id.labelVerifiedBadgeStatus);
        mLabelDisableAdsStatus = rootView.findViewById(R.id.labelDisableAdsStatus);
        mLabelProModeStatus = rootView.findViewById(R.id.labelProModeStatus);
        mLabelProModeTitle = rootView.findViewById(R.id.labelProMode);
        mLabelMessagePackageStatus = rootView.findViewById(R.id.labelMessagePackageStatus);

        mGhostModeButton = rootView.findViewById(R.id.ghostModeBtn);
        mVerifiedBadgeButton = rootView.findViewById(R.id.verifiedBadgeBtn);
        mDisableAdsButton = rootView.findViewById(R.id.disableAdsBtn);
        mProModeButton = rootView.findViewById(R.id.proModeBtn);
        mMessagePackageButton = rootView.findViewById(R.id.messagePackageBtn);

        mGetCreditsButton = rootView.findViewById(R.id.getCreditsBtn);

        mGetCreditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), BalanceActivity.class);
                startActivityForResult(i, 1945);
            }
        });

        mGhostModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= App.getInstance().getSettings().getGhostModeCost()) {

                    upgrade(PA_BUY_GHOST_MODE, App.getInstance().getSettings().getGhostModeCost());

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mVerifiedBadgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= App.getInstance().getSettings().getVerifiedBadgeCost()) {

                    upgrade(PA_BUY_VERIFIED_BADGE, App.getInstance().getSettings().getVerifiedBadgeCost());

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mProModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= App.getInstance().getSettings().getProModeCost()) {

                    upgrade(PA_BUY_PRO_MODE, App.getInstance().getSettings().getProModeCost());

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDisableAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= App.getInstance().getSettings().getDisableAdsCost()) {

                    upgrade(PA_BUY_DISABLE_ADS, App.getInstance().getSettings().getDisableAdsCost());

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMessagePackageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= App.getInstance().getSettings().getMessagePackageCost()) {

                    upgrade(PA_BUY_MESSAGE_PACKAGE, App.getInstance().getSettings().getMessagePackageCost());

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        update();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1945 && resultCode == getActivity().RESULT_OK && null != data) {

            update();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }

    @Override
    public void onStart() {

        super.onStart();

        update();
    }

    public void update() {

        mLabelCredits.setText(getString(R.string.label_credits) + " (" + App.getInstance().getBalance() + ")");

        mGhostModeButton.setText(getString(R.string.action_enable) + " (" + App.getInstance().getSettings().getGhostModeCost() + ")" + " " + getString(R.string.label_month_time));
        mVerifiedBadgeButton.setText(getString(R.string.action_enable) + " (" + App.getInstance().getSettings().getVerifiedBadgeCost() + ")" + " " + getString(R.string.label_life_time));
        mProModeButton.setText(getString(R.string.action_enable) + " (" + App.getInstance().getSettings().getProModeCost() + ")" + " " + getString(R.string.label_life_time));
        mDisableAdsButton.setText(getString(R.string.action_enable) + " (" + App.getInstance().getSettings().getDisableAdsCost() + ")" + " " + getString(R.string.label_month_time));
        mMessagePackageButton.setText(getString(R.string.action_purchase) + " (" + App.getInstance().getSettings().getMessagePackageCost() + ")");

        if (App.getInstance().getGhost() == 0) {

            mLabelGhostModeStatus.setVisibility(View.GONE);
            mGhostModeButton.setEnabled(true);
            mGhostModeButton.setVisibility(View.VISIBLE);

        } else {

            mLabelGhostModeStatus.setVisibility(View.VISIBLE);
            mGhostModeButton.setEnabled(false);
            mGhostModeButton.setVisibility(View.GONE);
        }

        if (App.getInstance().getVerify() == 0) {

            mLabelVerifiedBadgeStatus.setVisibility(View.GONE);
            mVerifiedBadgeButton.setEnabled(true);
            mVerifiedBadgeButton.setVisibility(View.VISIBLE);

        } else {

            mLabelVerifiedBadgeStatus.setVisibility(View.VISIBLE);
            mVerifiedBadgeButton.setEnabled(false);
            mVerifiedBadgeButton.setVisibility(View.GONE);
        }

        if (App.getInstance().getPro() == 0) {

            mLabelProModeStatus.setVisibility(View.GONE);
            mProModeButton.setEnabled(true);
            mProModeButton.setVisibility(View.VISIBLE);

            mLabelProModeTitle.setText(getActivity().getString(R.string.label_upgrades_pro_mode));

            mLabelMessagePackageStatus.setText(String.format(Locale.getDefault(), getString(R.string.free_messages_of), App.getInstance().getFreeMessagesCount()));

        } else {

            mLabelProModeStatus.setVisibility(View.VISIBLE);
            mProModeButton.setEnabled(false);
            mProModeButton.setVisibility(View.GONE);

            mLabelProModeTitle.setText(getActivity().getString(R.string.label_upgrades_pro_mode));

            mMessagePackageContainer.setVisibility(View.GONE);
        }

        if (App.getInstance().getAdmob() == ADMOB_DISABLED) {

            mLabelDisableAdsStatus.setVisibility(View.GONE);
            mDisableAdsButton.setEnabled(true);
            mDisableAdsButton.setVisibility(View.VISIBLE);

        } else {

            mLabelDisableAdsStatus.setVisibility(View.VISIBLE);
            mDisableAdsButton.setEnabled(false);
            mDisableAdsButton.setVisibility(View.GONE);
        }
    }

    public void upgrade(final int upgradeType, final int credits) {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_UPGRADE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (!response.getBoolean("error")) {

                                switch (upgradeType) {

                                    case PA_BUY_VERIFIED_BADGE: {

                                        App.getInstance().setBalance(App.getInstance().getBalance() - credits);
                                        App.getInstance().setVerify(1);

                                        Toast.makeText(getActivity(), getString(R.string.msg_success_verified_badge), Toast.LENGTH_SHORT).show();

                                        break;
                                    }

                                    case PA_BUY_GHOST_MODE: {

                                        App.getInstance().setBalance(App.getInstance().getBalance() - credits);
                                        App.getInstance().setGhost(1);

                                        Toast.makeText(getActivity(), getString(R.string.msg_success_ghost_mode), Toast.LENGTH_SHORT).show();

                                        break;
                                    }

                                    case PA_BUY_DISABLE_ADS: {

                                        App.getInstance().setBalance(App.getInstance().getBalance() - credits);
                                        App.getInstance().setAdmob(ADMOB_ENABLED);

                                        Toast.makeText(getActivity(), getString(R.string.msg_success_disable_ads), Toast.LENGTH_SHORT).show();

                                        break;
                                    }

                                    case PA_BUY_PRO_MODE: {

                                        App.getInstance().setBalance(App.getInstance().getBalance() - credits);
                                        App.getInstance().setPro(1);

                                        Toast.makeText(getActivity(), getString(R.string.msg_success_pro_mode), Toast.LENGTH_SHORT).show();

                                        break;
                                    }

                                    case PA_BUY_MESSAGE_PACKAGE: {

                                        App.getInstance().setBalance(App.getInstance().getBalance() - credits);
                                        App.getInstance().setFreeMessagesCount(App.getInstance().getFreeMessagesCount() + 100);

                                        Toast.makeText(getActivity(), getString(R.string.msg_success_buy_message_package), Toast.LENGTH_SHORT).show();

                                        break;
                                    }

                                    default: {

                                        break;
                                    }
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            update();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading = false;

                update();

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("upgradeType", Integer.toString(upgradeType));
                params.put("credits", Integer.toString(credits));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
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
}