package com.forecast.forecast.fragments;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;




/**
 * 作者：
 * 创建时间
 * 功能描述：
 */
@EFragment(R.layout.fragment_add_step)
public class AddStepFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;
    @ViewById(R.id.et_number)
    EditText etNumber;
    @ViewById(R.id.tv_time_name)
    TextView tvTimeName;
    @ViewById(R.id.et_distance)
    EditText etDistance;
    @ViewById(R.id.et_quantity)
    EditText etQuanity;



    private TimePickerView pickerView;

    private Date examDate;

    private String objectId;

    @AfterViews
    void initView(){
        navBar.setMiddleTitle("新增步数");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });

        objectId = getArguments().getString("objectId");

        pickerView = new TimePickerView(getActivity(),TimePickerView.Type.YEAR_MONTH_DAY);
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                examDate = date;
                String time = Tool.formatSimpleDate1(date);
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



        final Step step = new Step();
        step.setStepNumber(number);
        step.setUserId(objectId);
        step.setDistance(distance);
        step.setQuantity(quanity);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(examDate);
        endCalendar.add(Calendar.MONTH, -1);
        step.setTime(new BmobDate(endCalendar.getTime()));


        BmobQuery<Step> stepBmobQuery = new BmobQuery<>();
        stepBmobQuery.addWhereEqualTo("UserId",objectId);
        stepBmobQuery.findObjects(getContext(), new FindListener<Step>() {
            @Override
            public void onSuccess(List<Step> list) {

                Boolean addShow = true;
                for(Step step1:list){
                    if (step1.getTime().getDate().equals(step.getTime().getDate())){
                        addShow = false;
                        break;
                    }
                }

                if (addShow){
                    step.save(getContext(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            EventBus.getDefault().post(new String(Constant.EVEBUSSTEP));
                            ToastUtil.showToast("添加成功");
                            finishFragment();
                        }
                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }else{
                    ToastUtil.showToast("该天记录存在,只能修改");
                }


            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

}
