package com.thinkin_service.provider.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkin_service.provider.R;
import com.thinkin_service.provider.data.network.model.User;
import com.thinkin_service.provider.ui.bottomsheetdialog.invoice_flow.InvoiceDialogFragment;
import com.thinkin_service.provider.ui.fragment.incoming_request.IncomingRequestFragment;
import com.thinkin_service.provider.ui.fragment.status_flow.StatusFlowFragment;
import com.thinkin_service.provider.ui.fragment.upcoming.UpcomingTripFragment;
import com.thinkin_service.provider.ui.fragment.past.PastTripFragment;
import com.thinkin_service.provider.ui.fragment.offline.OfflineFragment;
import com.thinkin_service.provider.ui.fragment.dispute_status.DisputeStatusFragment;
import com.thinkin_service.provider.ui.bottomsheetdialog.rating.RatingDialogFragment;

public abstract class BaseFragment extends Fragment implements MvpView {

    protected View view;
    protected ProgressDialog progressDialog;

    private BaseActivity mActivity;


    public abstract int getLayoutId();

    public abstract View initView(View view);

    public abstract Fragment fragmentInstance();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            if (fragmentInstance() instanceof IncomingRequestFragment ||
                    fragmentInstance() instanceof StatusFlowFragment ||
                    fragmentInstance() instanceof UpcomingTripFragment ||
                    fragmentInstance() instanceof PastTripFragment ||
                    fragmentInstance() instanceof OfflineFragment ||
                    fragmentInstance() instanceof DisputeStatusFragment ||
                    fragmentInstance() instanceof RatingDialogFragment){
                initView(view);
            }
        }

        progressDialog = new ProgressDialog(activity());
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // atualiza os dados apos a view ter sido criada
        if (fragmentInstance() instanceof InvoiceDialogFragment){
            showLoading();
            // Delay de 5 segs até exibir o valor para pagamento no fragment
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    initView(view);
                }
            }, 5000);
        }
    }

    @Override
    public FragmentActivity activity() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    public void onErrorBase(Throwable e) {
        if (mActivity != null) {
            mActivity.onErrorBase(e);
        }
    }

    @Override
    public void onSuccessRefreshToken(User user) {
        if (mActivity != null) {
            mActivity.onSuccessRefreshToken(user);
        }
    }

    @Override
    public void onErrorRefreshToken(Throwable throwable) {
        if (mActivity != null) {
            mActivity.onErrorRefreshToken(throwable);
        }
    }

    @Override
    public void onSuccessLogout(Object object) {
        if (mActivity != null) {
            mActivity.onSuccessLogout(object);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (mActivity != null) {
            mActivity.onError(throwable);
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
}
