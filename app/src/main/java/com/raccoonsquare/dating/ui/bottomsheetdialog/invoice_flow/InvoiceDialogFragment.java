package com.thinkin_service.provider.ui.bottomsheetdialog.invoice_flow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinkin_service.provider.MvpApplication;
import com.thinkin_service.provider.R;
import com.thinkin_service.provider.base.BaseFragment;
import com.thinkin_service.provider.common.Constants;
import com.thinkin_service.provider.data.network.model.Request_;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

import static com.thinkin_service.provider.MvpApplication.DATUM;

public class InvoiceDialogFragment extends BaseFragment implements InvoiceDialogIView {

    @BindView(R.id.promotion_amount)
    TextView promotionAmount;
    @BindView(R.id.wallet_amount)
    TextView walletAmount;
    @BindView(R.id.booking_id)
    TextView bookingId;
//    @BindView(R.id.total_amount)
//    TextView totalAmount;
    @BindView(R.id.payable_amount)
    TextView payableAmount;
    @BindView(R.id.payment_mode_img)
    ImageView paymentModeImg;
    @BindView(R.id.payment_mode_layout)
    LinearLayout paymentModeLayout;
    @BindView(R.id.llAmountToBePaid)
    LinearLayout llAmountToBePaid;
    Unbinder unbinder;
    @BindView(R.id.btnConfirmPayment)
    Button btnConfirmPayment;

    InvoiceDialogPresenter presenter;
    @BindView(R.id.lblPaymentType)
    TextView lblPaymentType;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invoice_dialog;
    }

    @Override
    public Fragment fragmentInstance() {
        return InvoiceDialogFragment.this;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter = new InvoiceDialogPresenter();
        presenter.attachView(this);
        // setCancelable(false);
        if (DATUM != null) {
            Request_ datum = DATUM;
            bookingId.setText(datum.getBookingId());

            //TODO ALLAN - Alterações débito na máquina e voucher
            if(datum.getPaymentMode().equals("CASH")){
                lblPaymentType.setText("CASH");
            }else if(datum.getPaymentMode().equals("DEBIT_MACHINE")){
                lblPaymentType.setText("DEBIT MACHINE");
                paymentModeImg.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_debit_machine));
            }else if(datum.getPaymentMode().equals("VOUCHER")) {
                lblPaymentType.setText("VOUCHER");
                paymentModeImg.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_voucher));
            }else{
                lblPaymentType.setText("CARD");
                paymentModeImg.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_card));
            }

            if (datum.getPayment() != null) {
                if (datum.getPayment().getPayable() > 0) {
                    llAmountToBePaid.setVisibility(View.VISIBLE);


                    // If it is payment, the card displays value minus fees
                    if(datum.getPaymentMode().equals("CARD")){
                        payableAmount.setText(Constants.Currency +
                                MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(datum.getPayment().getProviderPay() + "")));
                    }else{
                        payableAmount.setText(Constants.Currency +
                                MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(datum.getPayment().getPayable() + "")));
                    }
                } else llAmountToBePaid.setVisibility(View.GONE);
            }else{
                Toasty.info(this.getContext(), "Failed to display data! Please minimize the app and open again.", Toast.LENGTH_LONG).show();
                payableAmount.setText("Failure");
            }
        }
        return view;
    }

    @Override
    public void onSuccess(Object message) {
        hideLoading();
        activity().sendBroadcast(new Intent("INTENT_FILTER"));

    }


    @Override
    public void onError(Throwable e) {
        hideLoading();
        try {
            if (e != null)
                onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    @OnClick(R.id.btnConfirmPayment)
    public void onViewClicked() {

        if (DATUM != null) {


            // TODO ALLAN - Machine and voucher changes
            if(DATUM.getPaymentMode().equals("CARD")){
                Request_ datum = DATUM;
                HashMap<String, Object> map = new HashMap<>();
                map.put("status", Constants.checkStatus.COMPLETED);
                map.put("request_id", datum.getId());
                map.put("tips", 0.0);
                map.put("payment_type", Constants.PaymentMode.CARD);
                map.put("payment_mode", Constants.PaymentMode.CARD);
                map.put("_method", Constants.checkStatus.PATCH);
                showLoading();
                presenter.statusUpdate(map, datum.getId());
            }else if(DATUM.getPaymentMode().equals("DEBIT_MACHINE")){
                Request_ datum = DATUM;
                HashMap<String, Object> map = new HashMap<>();
                map.put("status", Constants.checkStatus.COMPLETED);
                map.put("payment_mode", "DEBIT_MACHINE");
                map.put("_method", Constants.checkStatus.PATCH);
                showLoading();
                presenter.statusUpdate(map, datum.getId());
                presenter.statusUpdate(map, datum.getId());
            }else if(DATUM.getPaymentMode().equals("VOUCHER")){
                Request_ datum = DATUM;
                HashMap<String, Object> map = new HashMap<>();
                map.put("status", Constants.checkStatus.COMPLETED);
                map.put("payment_mode", "VOUCHER");
                map.put("_method", Constants.checkStatus.PATCH);
                showLoading();
                presenter.statusUpdate(map, datum.getId());
            }else{
                Request_ datum = DATUM;
                HashMap<String, Object> map = new HashMap<>();
                map.put("status", Constants.checkStatus.COMPLETED);
                map.put("payment_mode", "CASH");
                map.put("_method", Constants.checkStatus.PATCH);
                showLoading();
                presenter.statusUpdate(map, datum.getId());
            }
        }
    }

    public void ShowPaymentPopUp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder
//                .setMessage(getString(R.string.log_out_title))
                .setMessage("ATTENTION! The passenger has facilitated credit card payment, wait for the passenger. By confirming you will be informing that you have received the cash payment.")
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog, id) -> {
                    Request_ datum = DATUM;
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("status", Constants.checkStatus.COMPLETED);
                    map.put("payment_mode", "CASH");
                    map.put("_method", Constants.checkStatus.PATCH);
                    showLoading();
                    presenter.statusUpdate(map, datum.getId());
                }).setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

   /* @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter = new InvoiceDialogPresenter();
        presenter.attachView(this);
        setCancelable(false);
        numberFormat = MvpApplication.getInstance().getNewNumberFormat();

        if (DATUM != null) {
            Request_ datum = DATUM;
            bookingId.setText(datum.getBookingId());
            if (datum.getPayment() != null)
                if (datum.getPayment().getTotal() != 0 ||
                        datum.getPayment().getPayable() != 0) {
                    totalAmount.setText(Constants.Currency + " " + numberFormat.format(Double.parseDouble(datum.getPayment().getTotal() + "")));
                    payableAmount.setText(Constants.Currency + " " + numberFormat.format(Double.parseDouble(datum.getPayment().getPayable() + "")));
                }
        }
    }

    *//*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "InvoiceDialogFragment", InvoiceDialogFragment.this);
    }*//*

    @OnClick(R.ID.btnConfirmPayment)
    public void onViewClicked() {

        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", Constants.checkStatus.COMPLETED);
            map.put("_method", Constants.checkStatus.PATCH);
            showLoading();
            presenter.statusUpdate(map, datum.getId());
        }
    }

    @Override
    public void onSuccess(Object object) {
        dismissAllowingStateLoss();
        hideLoading();
        activity().sendBroadcast(new Intent("INTENT_FILTER"));
    }


    @Override
    public void onError(Throwable e) {
        hideLoading();
    }*/
}
