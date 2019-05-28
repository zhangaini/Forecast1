package com.forecast.forecast.fragments;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.Contact;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.view.widget.NavBarBack;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 作者：
 * 创建时间
 * 功能描述：
 */
@EFragment(R.layout.fragment_contact)
public class ContactFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;
    @ViewById(R.id.ll_info)
    LinearLayout llInfo;
    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.tv_phone)
    TextView tvPhone;
    @ViewById(R.id.bt_save)
    Button btSave;


    private String UserId;
    private int type = 1;

    @AfterViews
    void initView(){

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }


        navBar.setMiddleTitle("联系人");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });


        UserId = SecurePreferences.getInstance().getString(Constant.USERID,"");
        getData();
    }

    /**
     * 获取数据
     */
    public void getData(){

        BmobQuery<Contact> contactBmobQuery = new BmobQuery<>();
        contactBmobQuery.addWhereEqualTo("UserId",UserId);
        contactBmobQuery.findObjects(getActivity(), new FindListener<Contact>() {
            @Override
            public void onSuccess(List<Contact> list) {
                if (list.size() == 0){
                    type = 1;
                    llInfo.setVisibility(View.GONE);
                    btSave.setText("新增");
                }else{
                    tvName.setText("姓名:"+list.get(0).getName());
                    tvPhone.setText("电话:"+list.get(0).getPhone());

                    type = 2;
                    llInfo.setVisibility(View.VISIBLE);
                    btSave.setText("修改");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 保存数据
     */
    @Click(R.id.bt_save)
    void btSave(){

        AddContactFragment contactFragment = AddContactFragment_.builder().build();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        contactFragment.setArguments(bundle);
        showFragment(getActivity(),contactFragment);

    }


    @Subscribe
    public void refreshData(String str){
        if (str.equals(Constant.EVEBUSCONTACT)){
            getData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
