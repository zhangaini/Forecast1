package com.forecast.forecast.fragments;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.forecast.forecast.R;
import com.forecast.forecast.activities.LoginActivity_;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.User;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.utils.ToastUtil;
import com.forecast.forecast.view.widget.NavBarBack;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：
 */
@EFragment(R.layout.fragment_modify_pwd)
public class ModifyPwdFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;
    @ViewById(R.id.et_oldpwd)
    EditText etOldPwd;
    @ViewById(R.id.et_pwd)
    EditText etPwd;
    @ViewById(R.id.et_repwd)
    EditText etRePwd;


    @AfterViews
    void initView(){
        navBar.setMiddleTitle("修改密码");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });


    }

    /**
     * 保存密码
     */
    @Click(R.id.bt_save)
    void btSave(){
        final String oldPwd = etOldPwd.getText().toString().trim();
        final String pwd = etPwd.getText().toString().trim();
        String rePwd = etRePwd.getText().toString().trim();

        if (TextUtils.isEmpty(oldPwd)){
            ToastUtil.showToast("请输入原密码");
            return;
        }

        if (TextUtils.isEmpty(pwd)){
            ToastUtil.showToast("请输入新密码");
            return;
        }

        if (TextUtils.isEmpty(rePwd)){
            ToastUtil.showToast("请输入再次密码");
            return;
        }

        if (pwd.length() <6){
            ToastUtil.showToast("密码长度不少于6位");
            return;
        }

        if (!pwd.equals(rePwd)){
            ToastUtil.showToast("两次密码不一致");
            return;
        }


        showProgress();
        String objectId = SecurePreferences.getInstance().getString(Constant.USERID,"");

        User user =new User();
        user.setObjectId(objectId);
        user.setType(SecurePreferences.getInstance().getInt(Constant.TYPE,1));
        user.setPassword(rePwd);
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(getContext(), objectId, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                closeProgress();
                if (!user.getPassword().equals(oldPwd)){
                    ToastUtil.showToast("原密码错误");
                }else{
                    user.update(getContext());
                    SecurePreferences.getInstance().edit().putInt(Constant.TYPE,0).commit();
                    ToastUtil.showToast("修改成功，请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity_.class));
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }


}
