package com.thinkin_service.provider.ui.activity.email;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkin_service.provider.BuildConfig;
import com.thinkin_service.provider.R;
import com.thinkin_service.provider.base.BaseActivity;
import com.thinkin_service.provider.common.Constants;
import com.thinkin_service.provider.common.SharedHelper;
import com.thinkin_service.provider.ui.activity.password.PasswordActivity;
import com.thinkin_service.provider.ui.activity.regsiter.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class EmailActivity extends BaseActivity implements EmailIView {

    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.next)
    FloatingActionButton next;

    EmailIPresenter presenter = new EmailPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_email;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        if (BuildConfig.DEBUG) email.setText("partner@dragon.com");
    }

    @OnClick({R.id.back, R.id.sign_up, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                activity().onBackPressed();
                break;
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.next:
                if (email.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
                    return;
                }
                Intent i = new Intent(this, PasswordActivity.class);
                i.putExtra(Constants.SharedPref.EMAIL, email.getText().toString());
                SharedHelper.putKey(this, Constants.SharedPref.TXT_EMAIL, email.getText().toString());
                startActivity(i);
                break;
        }
    }
}
