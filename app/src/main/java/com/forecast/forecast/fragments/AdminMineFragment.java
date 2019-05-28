package com.forecast.forecast.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import com.forecast.forecast.R;
import com.forecast.forecast.activities.LoginActivity_;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.imagePacker.GlideImageLoader;
import com.forecast.forecast.imagePacker.ImageGridActivity;
import com.forecast.forecast.imagePacker.ImageItem;
import com.forecast.forecast.imagePacker.ImagePicker;
import com.forecast.forecast.models.User;
import com.forecast.forecast.utils.ImageLoader;
import com.forecast.forecast.utils.PermissionUtili;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.utils.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.io.File;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 创建时间：
 * 作者：
 * 功能描述
 */
@EFragment(R.layout.fragment_admin_mine)
public class AdminMineFragment extends BaseFragment {


    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.iv_header)
    RoundedImageView ivHeader;

    @AfterViews
    void initView() {
        getData();
    }

    /**
     * 获取数据
     */
    public void getData(){

        String objectId = SecurePreferences.getInstance().getString(Constant.USERID,"");

        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(getActivity(), objectId, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                tvName.setText(user.getNickName());
                if (user.getImg() == null){
                    ivHeader.setBackgroundResource(R.drawable.timg);
                }else{
                    ImageLoader.loadCircleImage(user.getImg().getUrl(),ivHeader,R.drawable.timg);
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 修改昵称
     */
    @Click(R.id.tv_name)
    void tvName(){
        final EditText inputServer = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修改昵称").setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

               String name =  inputServer.getText().toString();
               if (!TextUtils.isEmpty(name)){
                   String objectId = SecurePreferences.getInstance().getString(Constant.USERID,"");
                   User user = new User();
                   user.setObjectId(objectId);
                   user.setNickName(name);
                   user.setType(SecurePreferences.getInstance().getInt(Constant.TYPE,1));
                   user.update(getContext());
                   tvName.setText(name);
                   ToastUtil.showToast("修改成功");
               }else{
                   ToastUtil.showToast("修改失败");
               }
            }
        });
        builder.show();
    }

    /**
     * 头像
     */
    @Click(R.id.iv_header)
    void ivHeader(){

        String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        boolean check = PermissionUtili.checkPermission(getActivity(),permission,"需要设置手机权限","需要拍照，存储全新");

        if (check){
            ImagePicker picker = ImagePicker.getInstance();
            picker.setMultiMode(false);
            picker.setCrop(false);
            picker.setImageLoader(new GlideImageLoader());
            Intent intent = new Intent(getActivity(),ImageGridActivity.class);
            startActivityForResult(intent,200);

        }

    }


    //修改密码
    @Click(R.id.rl_pwd)
    void rlPwd(){

        showFragment(getActivity(),ModifyPwdFragment_.builder().build());

    }


    //退出登录
    @Click(R.id.bt_exit)
    void btExit(){
        SecurePreferences.getInstance().edit().putInt(Constant.TYPE,0).commit();
        startActivity(new Intent(getActivity(), LoginActivity_.class));
        getActivity().finish();
    }

    /**
     * 上传图片
     * @param path
     */
    public void uploadImage(String path){

        final BmobFile file = new BmobFile(new File(path));
        file.upload(getActivity(), new UploadFileListener() {
            @Override
            public void onSuccess() {
                String objectId = SecurePreferences.getInstance().getString(Constant.USERID,"");
                User user = new User();
                user.setObjectId(objectId);
                user.setImg(file);
                user.setType(SecurePreferences.getInstance().getInt(Constant.TYPE,1));
                user.update(getContext());
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200){
            List<ImageItem> imageItems = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            ImageLoader.loadCircleImage(imageItems.get(0).path,ivHeader,R.drawable.timg);
            uploadImage(imageItems.get(0).path);
        }
    }
}
