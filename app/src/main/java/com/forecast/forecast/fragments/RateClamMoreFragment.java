package com.forecast.forecast.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.forecast.forecast.R;
import com.forecast.forecast.adapters.UserRateClamAdapter;
import com.forecast.forecast.adapters.UserStepAdapter;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.intefaces.AdapterListener;
import com.forecast.forecast.models.RateClam;
import com.forecast.forecast.models.Step;
import com.forecast.forecast.view.widget.NavBarBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：
 */
@EFragment(R.layout.fragment_school)
public class RateClamMoreFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;
    @ViewById(R.id.recycler)
    RecyclerView recyclerView;


    private String objectId;
    private UserRateClamAdapter userRateClamAdapter;

    @AfterViews
    void initView(){


        objectId = getArguments().getString("objectId");
        navBar.setBarTitle("静态心率");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }

        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userRateClamAdapter = new UserRateClamAdapter(getActivity(),adapterListener));

        getData();
    }


    AdapterListener adapterListener = new AdapterListener<Step>() {
        @Override
        public void setItemClickListener(Step step, int position) {

        }
    };


    /**
     * 获取数据
     */
    public void getData(){

        BmobQuery<RateClam> stepBmobQuery = new BmobQuery<>();
        stepBmobQuery.addWhereEqualTo("UserId",objectId);
        stepBmobQuery.order("-Time");
        stepBmobQuery.findObjects(getContext(), new FindListener<RateClam>() {
            @Override
            public void onSuccess(List<RateClam> list) {

                userRateClamAdapter.setData(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }


}
