package com.forecast.forecast.fragments;

import android.view.View;
import android.widget.EditText;
import com.forecast.forecast.R;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.Contact;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.utils.ToastUtil;
import com.forecast.forecast.view.widget.NavBarBack;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：校区
 */
@EFragment(R.layout.fragment_add_contact)
public class AddContactFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;
    @ViewById(R.id.et_name)
    EditText etName;
    @ViewById(R.id.et_phone)
    EditText etPhone;

    private int type = 1;
    private String UserId="";
    private String ObjectId="";

    @AfterViews
    void initView(){

        type = getArguments().getInt("type",1);

        UserId = SecurePreferences.getInstance().getString(Constant.USERID,"");

        if (type == 1){
            navBar.setMiddleTitle("新增");
        }else{
            navBar.setMiddleTitle("编辑");
            getData();
        }


        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });

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
                etName.setText(list.get(0).getName());
                etPhone.setText(list.get(0).getPhone());
                ObjectId = list.get(0).getObjectId();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /* 保存*/
    @Click(R.id.bt_save)
    void btSave(){

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        Contact contact = new Contact();
        contact.setName(name);
        contact.setUserId(UserId);
        contact.setPhone(phone);

        if (type == 1){
            contact.save(getActivity(), new SaveListener() {
                @Override
                public void onSuccess() {
                    EventBus.getDefault().post(new String(Constant.EVEBUSCONTACT));
                    ToastUtil.showToast("保存成功");
                    finishFragment();
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }else{
            contact.setObjectId(ObjectId);
            contact.update(getContext(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    EventBus.getDefault().post(new String(Constant.EVEBUSCONTACT));
                    ToastUtil.showToast("保存成功");
                    finishFragment();
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }

    }

}
