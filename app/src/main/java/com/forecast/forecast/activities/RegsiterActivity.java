package com.forecast.forecast.activities;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.forecast.forecast.R;
import com.forecast.forecast.activities.base.BaseActivity;
import com.forecast.forecast.models.User;
import com.forecast.forecast.utils.ToastUtil;
import com.forecast.forecast.view.widget.NavBarBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


@EActivity(R.layout.activity_regsiter)
public class RegsiterActivity extends BaseActivity {

    @ViewById(R.id.mNavBar)
    NavBarBack mNavBar;
    @ViewById(R.id.et_login)
    EditText etLogin;
    @ViewById(R.id.et_pwd)
    EditText etPwd;
    @ViewById(R.id.et_repwd)
    EditText etRepwd;

    private User user ;

    @AfterViews
    void initView(){

        mNavBar.setBarTitle("注册");
        mNavBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finish();
            }
        });
    }


    /**
     * 注册
     */
    @Click(R.id.bt_register)
    void btReister(){

        String name = etLogin.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        String rePwd = etRepwd.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            ToastUtil.showToast("请输入手机号");
            return;
        }

        if (TextUtils.isEmpty(pwd)){
            ToastUtil.showToast("请输入密码");
            return;
        }

        if (TextUtils.isEmpty(rePwd)){
            ToastUtil.showToast("请再次输入密码");
            return;
        }

        if (pwd.length() < 6){
            ToastUtil.showToast("密码长度不能少于6位");
            return;
        }


        if (!pwd.equals(rePwd)){
            ToastUtil.showToast("两次密码不一样");
            return;
        }

        user = new User();
        user.setAccount(name);
        user.setNickName(name);
        user.setPassword(pwd);
        user.setType(2);

        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("account",name);
        showProgress();
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {


                if(list.size() > 0){
                    closeProgress();
                    Toast.makeText(getBaseContext(),"该账号已被用户注册",Toast.LENGTH_LONG).show();
                }else{
                    Register(user);
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    public void Register(User user){

        user.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                closeProgress();
                Toast.makeText(getBaseContext(),"注册成功",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getBaseContext(),"注册失败",Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}
