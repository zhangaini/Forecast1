package com.forecast.forecast.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.RateClam;
import com.forecast.forecast.models.Step;
import com.forecast.forecast.utils.ToastUtil;
import com.forecast.forecast.view.widget.NavBarBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.listener.UpdateListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：
 */
@EFragment(R.layout.fragment_add_rateclam)
public class ModifyRateClamFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;
    @ViewById(R.id.et_number)
    EditText etNumber;
    @ViewById(R.id.rl_time)
    RelativeLayout rlTime;

    @ViewById(R.id.tv_time_name)
    TextView tvTimeName;
    @ViewById(R.id.et_score)
    EditText etScore;





    private RateClam rateClam;

    @AfterViews
    void initView(){
        navBar.setMiddleTitle("修改静脉心率");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });

        rateClam = (RateClam) getArguments().getSerializable("data");

        rlTime.setVisibility(View.GONE);
        etNumber.setText(rateClam.getNumber());
        etScore.setText(rateClam.getScore());



    }


    /* 保存*/
    @Click(R.id.bt_save)
    void btSave(){

        String number  = etNumber.getText().toString().trim();
        String score = etScore.getText().toString().trim();
        if (TextUtils.isEmpty(number)){
            ToastUtil.showToast("请输入静脉心率");
            return;
        }

        if (TextUtils.isEmpty(score)){
            ToastUtil.showToast("请输入评分");
            return;
        }

        rateClam.setNumber(number);
        rateClam.setScore(score);

        rateClam.update(getContext(), new UpdateListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new String(Constant.EVEBUSRATECLAM));
                ToastUtil.showToast("修改成功");
                finishFragment();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

}
