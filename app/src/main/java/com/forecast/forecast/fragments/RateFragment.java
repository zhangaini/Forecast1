package com.forecast.forecast.fragments;

import android.Manifest;
import android.view.View;
import com.forecast.forecast.R;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.utils.PermissionUtili;
import com.forecast.forecast.view.widget.NavBar;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * 作者：
 * 创建时间
 * 功能描述：首页
 */
@EFragment(R.layout.fragment_rate)
public class RateFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBar navBar;


    @AfterViews
    void initView(){
        navBar.setMiddleTitle("测试");
        navBar.setOnMenuClickListener(new NavBar.OnMenuClickListener() {
            @Override
            public void onRightMenuClick(View view) {
                super.onRightMenuClick(view);
            }
        });

    }

    /**
     * 静态心脉
     */
    @Click(R.id.bt_rate_calm)
    void btRateCalm(){

        String[] permission = new String[]{Manifest.permission.CALL_PHONE};
        boolean check = PermissionUtili.checkPermission(getActivity(),permission,"需要设置手机权限","需要拨打电话权限");

        if (check){
            showFragment(getActivity(),UserRateClamFragment_.builder().build());

        }


    }


    /**
     * 最大心脉
     */
    @Click(R.id.bt_rate_high)
    void btRateHigh(){
        String[] permission = new String[]{Manifest.permission.CALL_PHONE};
        boolean check = PermissionUtili.checkPermission(getActivity(),permission,"需要设置手机权限","需要拨打电话权限");

        if (check){
            showFragment(getActivity(),UserRateHighFragment_.builder().build());

        }


    }


    /**
     * 晨起心脉
     */
    @Click(R.id.bt_rate_up)
    void btRateUp(){

        String[] permission = new String[]{Manifest.permission.CALL_PHONE};
        boolean check = PermissionUtili.checkPermission(getActivity(),permission,"需要设置手机权限","需要拨打电话权限");

        if (check){
            showFragment(getActivity(),UserRateUpFragment_.builder().build());

        }

    }


    /**
     * 综合测评
     */
    @Click(R.id.bt_rate_score)
    void btRateScore(){

        showFragment(getActivity(),RateScoreFragment_.builder().build());
    }

}
