package com.forecast.forecast.fragments;

import android.os.Bundle;
import android.view.View;
import com.forecast.forecast.R;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.view.widget.NavBarBack;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;



/**
 * 作者：
 * 创建时间
 * 功能描述：课程
 */
@EFragment(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;

    private String objectId;

    @AfterViews
    void initView(){
        navBar.setMiddleTitle("项目");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });

        objectId = getArguments().getString("objectId");
    }


    /**
     * 步数
     */
    @Click(R.id.bt_step)
    void btStep(){
        StepFragment stepFragment = StepFragment_.builder().build();
        Bundle bundle = new Bundle();
        bundle.putString("objectId",objectId);
        stepFragment.setArguments(bundle);
        showFragment(getActivity(),stepFragment);
    }


    /**
     * 实时心率
     */
    @Click(R.id.bt_rate_time)
    void btRateTime(){
        RateTimeFragment rateTimeFragment = RateTimeFragment_.builder().build();
        Bundle bundle = new Bundle();
        bundle.putString("objectId",objectId);
        rateTimeFragment.setArguments(bundle);
        showFragment(getActivity(),rateTimeFragment);
    }


    /**
     * 静态心脉
     */
    @Click(R.id.bt_rate_calm)
    void btRateCalm(){

        RateClamFragment stepFragment = RateClamFragment_.builder().build();
        Bundle bundle = new Bundle();
        bundle.putString("objectId",objectId);
        stepFragment.setArguments(bundle);
        showFragment(getActivity(),stepFragment);
    }


    /**
     * 最大心率
     */
    @Click(R.id.bt_rate_high)
    void btRateHigh(){
        RateHighFragment rateHighFragment = RateHighFragment_.builder().build();
        Bundle bundle = new Bundle();
        bundle.putString("objectId",objectId);
        rateHighFragment.setArguments(bundle);
        showFragment(getActivity(),rateHighFragment);
    }

    /**
     * 晨起心率
     */
    @Click(R.id.bt_rate_up)
    void btRateUp(){

        RateUpFragment rateUpFragment = RateUpFragment_.builder().build();
        Bundle bundle = new Bundle();
        bundle.putString("objectId",objectId);
        rateUpFragment.setArguments(bundle);
        showFragment(getActivity(),rateUpFragment);
    }

}
