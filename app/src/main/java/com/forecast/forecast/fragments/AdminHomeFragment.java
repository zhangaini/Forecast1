package com.forecast.forecast.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.forecast.forecast.R;
import com.forecast.forecast.adapters.AdminHomeAdapter;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.intefaces.AdapterListener;
import com.forecast.forecast.models.User;
import com.forecast.forecast.view.widget.NavBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：首页
 */
@EFragment(R.layout.fragment_admin_home)
public class AdminHomeFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBar navBar;
    @ViewById(R.id.recycler)
    RecyclerView recyclerView;

    private AdminHomeAdapter adminHomeAdapter;

    @AfterViews
    void initView(){
        navBar.setMiddleTitle("首页");
        navBar.setOnMenuClickListener(new NavBar.OnMenuClickListener() {
            @Override
            public void onRightMenuClick(View view) {
                super.onRightMenuClick(view);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adminHomeAdapter = new AdminHomeAdapter(getContext(),adapterClickListener));

        getData();

    }

    /**
     * 获取用户列表
     */
    public void getData(){

        BmobQuery<User> user = new BmobQuery<>();
        user.addWhereEqualTo("type",2);
        user.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {

                adminHomeAdapter.setData(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    AdapterListener adapterClickListener = new AdapterListener<User>() {
        @Override
        public void setItemClickListener(User user, int position) {

            SettingFragment fragment = SettingFragment_.builder().build();
            Bundle bundle = new Bundle();
            bundle.putString("objectId",user.getObjectId());
            fragment.setArguments(bundle);
            showFragment(getActivity(),fragment);
        }
    };





}
