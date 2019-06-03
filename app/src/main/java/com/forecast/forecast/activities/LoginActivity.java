package com.forecast.forecast.activities;


import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.forecast.forecast.R;
import com.forecast.forecast.activities.base.BaseActivity;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.models.User;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.utils.ToastUtil;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * 创建时间： 2017/7/24.
 * 作者：
 * 功能描述：
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.et_login)
    EditText etLogin;
    @ViewById(R.id.et_pwd)
    EditText etPwd;


    /**
     * 登录
     */
    @Click(R.id.bt_login)
    void btLogin(){
        String login = etLogin.getText().toString().trim();
        final String pwd = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(login)){
            ToastUtil.showToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            ToastUtil.showToast("请输入密码");
            return;
        }

        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("account",login);
        showProgress();
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {

                closeProgress();
                if(list.size() < 1 ){
                    Toast.makeText(getBaseContext(),"用户不存在,请先注册",Toast.LENGTH_LONG).show();
                }else{

                    if (!list.get(0).getPassword().equals(pwd)){
                        Toast.makeText(getBaseContext(),"密码错误",Toast.LENGTH_LONG).show();
                    }else {
                        SecurePreferences.getInstance().edit().putString(Constant.USERID, list.get(0).getObjectId()).commit();
                        SecurePreferences.getInstance().edit().putInt(Constant.TYPE, list.get(0).getType()).commit();
                        LoginType(list.get(0).getType());
                    }
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    public void LoginType(int type ){
        if (type == 1){
            startActivity(new Intent(getActivity(),NewMainActivity.class));
        }else{
            startActivity(new Intent(getActivity(),NewMainActivity.class));
        }
    }


    /**
     * 注册
     */
    @Click(R.id.bt_register)
    void btRegister(){
        startActivity(new Intent(getActivity(),RegsiterActivity_.class));
    }


}
