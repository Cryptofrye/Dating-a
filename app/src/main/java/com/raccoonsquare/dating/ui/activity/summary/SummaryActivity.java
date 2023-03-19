package com.thinkin_service.provider.ui.activity.summary;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinkin_service.provider.MvpApplication;
import com.thinkin_service.provider.R;
import com.thinkin_service.provider.base.BaseActivity;
import com.thinkin_service.provider.common.Constants;
import com.thinkin_service.provider.common.Utilities;
import com.thinkin_service.provider.data.network.model.Summary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SummaryActivity extends BaseActivity implements SummaryIView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.card_layout)
    LinearLayout cardLayout;
    @BindView(R.id.rides_value)
    TextView ridesValue;
    @BindView(R.id.rides_card)
    CardView ridesCard;
    @BindView(R.id.completed_rides_value)
    TextView completedRidesValue;
    @BindView(R.id.completed_rides_card)
    CardView completedRides;
    @BindView(R.id.revenue_value)
    TextView revenueValue;
    @BindView(R.id.revenue_card)
    CardView revenueCard;
    @BindView(R.id.montly_gains_value)
    TextView montlyGainsValue;
    @BindView(R.id.montly_gains_card)
    CardView montlyGainsCard;
    @BindView(R.id.montly_pass_value)
    TextView montlyPassValue;
    @BindView(R.id.montly_pass_card)
    CardView montlyPassCard;

    private SummaryPresenter presenter = new SummaryPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_summary;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.summary));
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        presenter.getSummary(dateFormat.format(date));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(Summary summary) {

        Utilities.printV("onSuccess==>", summary.getMontlyPass() + "");
        // summary.setRevenue(210);
        Animation slideUp = AnimationUtils.loadAnimation(activity(), R.anim.slide_up_slow);
        cardLayout.startAnimation(slideUp);
        cardLayout.setVisibility(View.VISIBLE);

        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Un Used
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    animateTextView(0, summary.getRides(), ridesValue);
                    animateTextView(0, summary.getCompletedRides(), completedRidesValue);
                    animateTextView(0, summary.getCancelRides(), montlyPassValue);
                    animateTextViewFloat(0.0f, summary.getMontlyGains(), montlyGainsValue);
                    animateTextViewFloat(0.0f, summary.getRevenue(), revenueValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Un Used
            }
        });

    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @SuppressLint("SetTextI18n")
    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(700);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            if (textview.getId() == R.id.revenue_value)
                textview.setText(Constants.Currency +  valueAnimator1.getAnimatedValue().toString());
            else
                textview.setText(valueAnimator1.getAnimatedValue().toString());
        });
        valueAnimator.start();

    }


    @SuppressLint("SetTextI18n")
    public void animateTextViewFloat(float initialValue, float finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
        valueAnimator.setDuration(700);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            if (textview.getId() == R.id.revenue_value) {
                textview.setText(Constants.Currency + MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble
                        (valueAnimator1.getAnimatedValue().toString())));
            }else if (textview.getId() == R.id.montly_gains_value) {
                textview.setText(Constants.Currency + MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble
                        (valueAnimator1.getAnimatedValue().toString())));
            }else {
                textview.setText(valueAnimator1.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();

    }

}
