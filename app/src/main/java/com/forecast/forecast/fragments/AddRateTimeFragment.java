package com.forecast.forecast.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.RateClam;
import com.forecast.forecast.models.RateTime;
import com.forecast.forecast.models.StartAndEndYear;
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

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：
 */
@EFragment(R.layout.fragment_add_rateclam)
public class AddRateTimeFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;
    @ViewById(R.id.et_number)
    EditText etNumber;
    @ViewById(R.id.tv_time_name)
    TextView tvTimeName;
    @ViewById(R.id.et_score)
    EditText etScore;



    private TimePickerView pickerView;

    private Date examDate;

    private String objectId;

    @AfterViews
    void initView(){
        navBar.setMiddleTitle("新增实时心脉");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });

        objectId = getArguments().getString("objectId");

        pickerView = new TimePickerView(getActivity(),TimePickerView.Type.ALL);
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                examDate = date;
                String time = Tool.formatSimpleDate2(date);
                tvTimeName.setText(time);

            }

            @Override
            public void onTimeSelectStartEndYear(StartAndEndYear startAndEndYear) {

            }
        });
    }

    @Click(R.id.rl_time)
    void rlTime(){
        Tool.hideKeyboard(getActivity());
        pickerView.setTime(new Date());
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);
        pickerView.show();

    }




    /* 保存*/
    @Click(R.id.bt_save)
    void btSave(){

        String number  = etNumber.getText().toString().trim();
        String score = etScore.getText().toString().trim();
        if (TextUtils.isEmpty(number)){
            ToastUtil.showToast("请输入心脉");
            return;
        }

        if (examDate ==null){
            ToastUtil.showToast("请选择时间");
            return;
        }

        if (TextUtils.isEmpty(score)){
            ToastUtil.showToast("请输入评分");
            return;
        }


        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(examDate);
        endCalendar.add(Calendar.MONTH, -1);

        RateTime rateTime = new RateTime();
        rateTime.setNumber(number);
        rateTime.setUserId(objectId);
        rateTime.setScore(score);
        rateTime.setTime(new BmobDate(endCalendar.getTime()));


        rateTime.save(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new String(Constant.EVEBUSRATETIME));
                ToastUtil.showToast("添加成功");
                finishFragment();
            }
            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

}
