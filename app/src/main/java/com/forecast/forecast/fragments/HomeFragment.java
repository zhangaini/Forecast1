package com.forecast.forecast.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.activities.MyStep;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.Step;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.utils.Tool;
import com.forecast.forecast.view.HistogramView;
import com.forecast.forecast.view.SportStepCountView;
import com.forecast.forecast.view.widget.NavBar;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：首页
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBar navBar;

    @ViewById(R.id.circleProgress)
    SportStepCountView sportStepCountView;

    @ViewById(R.id.tv_distance)
    TextView tvDistance;
    @ViewById(R.id.tv_quarity)
    TextView tvQuarity;
    @ViewById(R.id.histogram)
    HistogramView histogramView;
    @ViewById(R.id.tv_step)
    TextView tvStep;

    private String objectId="";
    private int[] data = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    private int[] text = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    private String[] weeks = new String[] { "", "", "", "", "", "", "" };

    @AfterViews
    void initView(){
        //zlh 绑定id


        navBar.setMiddleTitle("首页");
        navBar.setOnMenuClickListener(new NavBar.OnMenuClickListener() {
            @Override
            public void onRightMenuClick(View view) {
                super.onRightMenuClick(view);
            }
        });
        objectId = SecurePreferences.getInstance().getString(Constant.USERID,"");

        //zlh 点击进入界面二
//        tvStep.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent();
//                intent.setClass(getContext(),MyStep.class);
//                getActivity().startActivity(intent);
//            }
//        });

        getStepData();


    }


    /**
     * 获取步数
     */
    public void getStepData(){

        sportStepCountView.setValueDuringRefresh(0f,10000);

        BmobQuery<Step> stepBmobQuery = new BmobQuery<>();
        stepBmobQuery.addWhereEqualTo("UserId",objectId);
        stepBmobQuery.order("-Time");
        stepBmobQuery.findObjects(getActivity(), new FindListener<Step>() {
            @Override
            public void onSuccess(List<Step> list) {


                for (Step step:list){
                    String stepDate = step.getTime().getDate();

                    if (stepDate.contains(Tool.formatSimpleDateX(new Date()))){
                        nowStepData(step);

                    }
                }



                initCart(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    /**
     * 获取当天步数
     * @param step
     */
    public void nowStepData(Step step){

        if (Integer.valueOf(step.getStepNumber())>=50000){
            sportStepCountView.setValueDuringRefresh(Float.valueOf(step.getStepNumber()),100000);
        }else if(Integer.valueOf(step.getStepNumber())>=40000){
            sportStepCountView.setValueDuringRefresh(Float.valueOf(step.getStepNumber()),50000);
        }else if(Integer.valueOf(step.getStepNumber())>=30000){
            sportStepCountView.setValueDuringRefresh(Float.valueOf(step.getStepNumber()),40000);
        }else if(Integer.valueOf(step.getStepNumber())>=20000){
            sportStepCountView.setValueDuringRefresh(Float.valueOf(step.getStepNumber()),30000);
        }else if(Integer.valueOf(step.getStepNumber())>=10000){
            sportStepCountView.setValueDuringRefresh(Float.valueOf(step.getStepNumber()),20000);
        } else{
            sportStepCountView.setValueDuringRefresh(Float.valueOf(step.getStepNumber()),10000);
        }

        tvDistance.setText(step.getDistance()+"公里");
        tvQuarity.setText(step.getQuantity()+"千卡");

    }


    /**
     * 更多步数
     */
    @Click(R.id.tv_more)
    public void tvMore(){

        UserStepFragment fragment = UserStepFragment_.builder().build();
        Bundle bundle = new Bundle();
        bundle.putString("objectId",objectId);
        fragment.setArguments(bundle);
        showFragment(getActivity(),fragment);
    }
    @Click(R.id.tv_step)
    public void tvStep(){
                Intent intent =new Intent();
                intent.setClass(getContext(),MyStep.class);
                getActivity().startActivity(intent);
    }


    public void initCart(List<Step> steps){

        for (int i=0;i<steps.size();i++){
            data[data.length -(i+1)] = Integer.valueOf(steps.get(i).getStepNumber());
            weeks[data.length-(i+1)] = steps.get(i).getTime().getDate().substring(5,7)+"/"+
                    steps.get(i).getTime().getDate().substring(8,10);

        }
        histogramView.setWeek(weeks);
        histogramView.setProgress(data);


        histogramView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int step = (v.getWidth() - 30) / 8;
                int x = (int) event.getX();
                for (int i = 0; i < 7; i++) {
                    if (x > (30 + step * (i + 1) - 30)
                            && x < (30 + step * (i + 1) + 30)) {
                        text[i] = 1;
                        for (int j = 0; j < 7; j++) {
                            if (i != j) {
                                text[j] = 0;
                            }
                        }
                        histogramView.setText(text);
                    }
                }

                return false;
            }
        });

    }



}
