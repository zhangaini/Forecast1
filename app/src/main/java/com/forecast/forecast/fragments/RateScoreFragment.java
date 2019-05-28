package com.forecast.forecast.fragments;


import android.view.View;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.RateClam;
import com.forecast.forecast.models.RateHigh;
import com.forecast.forecast.models.RateUp;
import com.forecast.forecast.view.widget.NavBarBack;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：
 */
@EFragment(R.layout.fragment_rate_score)
public class RateScoreFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;

    @ViewById(R.id.tv_rate_clam)
    TextView tvRateClam;
    @ViewById(R.id.tv_rate_max)
    TextView tvRateMax;
    @ViewById(R.id.tv_rate_up)
    TextView tvRateUp;
    @ViewById(R.id.tv_score)
    TextView tvScore;



    private String objectId;
    private RateClam rateClam;
    private RateHigh rateHigh;
    private RateUp rateUp;


    @AfterViews
    void initView(){

        objectId = getArguments().getString("objectId");
        navBar.setBarTitle("综合评分");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });

        getRateClam();

    }

    /**
     * 获取静态心脉
     */
    public void getRateClam(){

        BmobQuery<RateClam> rateClamBmobQuery = new BmobQuery<>();
        rateClamBmobQuery.addWhereEqualTo("UserId",objectId);
        rateClamBmobQuery.order("-Time");
        rateClamBmobQuery.findObjects(getActivity(), new FindListener<RateClam>() {
            @Override
            public void onSuccess(List<RateClam> list) {

                if (list.size()>0){
                    rateClam = list.get(0);
                    tvRateClam.setText(rateClam.getScore()+"分");
                    getRateHigh();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }


    /**
     * 获取最大心脉
     */
    public void getRateHigh(){

        BmobQuery<RateHigh> rateClamBmobQuery = new BmobQuery<>();
        rateClamBmobQuery.addWhereEqualTo("UserId",objectId);
        rateClamBmobQuery.order("-Time");
        rateClamBmobQuery.findObjects(getActivity(), new FindListener<RateHigh>() {
            @Override
            public void onSuccess(List<RateHigh> list) {

                if (list.size()>0){
                    rateHigh = list.get(0);
                    tvRateMax.setText(rateHigh.getScore()+"分");
                    getRateUp();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }


    /**
     * 获取晨起心脉
     */
    public void getRateUp(){

        BmobQuery<RateUp> rateClamBmobQuery = new BmobQuery<>();
        rateClamBmobQuery.addWhereEqualTo("UserId",objectId);
        rateClamBmobQuery.order("-Time");
        rateClamBmobQuery.findObjects(getActivity(), new FindListener<RateUp>() {
            @Override
            public void onSuccess(List<RateUp> list) {
                if (list.size()>0){
                    rateUp = list.get(0);
                    tvRateUp.setText(rateUp.getScore()+"分");

                    tvScore.setText(( Integer.valueOf(rateClam.getScore())
                            +Integer.valueOf(rateHigh.getScore())+Integer.valueOf(rateUp.getScore()))/3 +"分");


                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

}
