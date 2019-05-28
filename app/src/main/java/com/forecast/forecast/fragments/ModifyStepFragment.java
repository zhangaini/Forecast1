package com.forecast.forecast.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.StartAndEndYear;
import com.forecast.forecast.models.Step;
import com.forecast.forecast.utils.ToastUtil;
import com.forecast.forecast.utils.Tool;
import com.forecast.forecast.view.TimePickerView;
import com.forecast.forecast.view.widget.NavBarBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：
 */
@EFragment(R.layout.fragment_add_step)
public class ModifyStepFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;
    @ViewById(R.id.et_number)
    EditText etNumber;
    @ViewById(R.id.rl_time)
    RelativeLayout rlTime;

    @ViewById(R.id.tv_time_name)
    TextView tvTimeName;
    @ViewById(R.id.et_distance)
    EditText etDistance;
    @ViewById(R.id.et_quantity)
    EditText etQuanity;





    private Step step;

    @AfterViews
    void initView(){
        navBar.setMiddleTitle("修改步数");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });

        step = (Step) getArguments().getSerializable("data");


        rlTime.setVisibility(View.GONE);
        etNumber.setText(step.getStepNumber());
        etDistance.setText(step.getDistance());
        etQuanity.setText(step.getQuantity());


    }


    /* 保存*/
    @Click(R.id.bt_save)
    void btSave(){

        String number  = etNumber.getText().toString().trim();
        String distance = etDistance.getText().toString().trim();
        String quanity = etQuanity.getText().toString().trim();
        if (TextUtils.isEmpty(number)){
            ToastUtil.showToast("请输入步数");
            return;
        }
        if (TextUtils.isEmpty(distance)){
            ToastUtil.showToast("请输入距离");
            return;
        }
        if (TextUtils.isEmpty(quanity)){
            ToastUtil.showToast("请输入热量");
            return;
        }


        step.setQuantity(quanity);
        step.setDistance(distance);
        step.setStepNumber(number);

        step.update(getContext(), new UpdateListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new String(Constant.EVEBUSSTEP));
                ToastUtil.showToast("修改成功");
                finishFragment();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

}
