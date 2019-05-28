package com.forecast.forecast.fragments.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forecast.forecast.R;
import com.forecast.forecast.constants.Permission;
import com.forecast.forecast.utils.PermissionUtili;
import com.forecast.forecast.utils.Tool;
import com.forecast.forecast.view.widget.ProgressDialog;
import com.forecast.forecast.view.widget.ProgressDialog_;

import org.androidannotations.annotations.EFragment;

import java.util.List;

/**
 * @author ruhui
 * @time 2017/2/22 15:34
 **/

@EFragment
public abstract class BaseFragment extends Fragment {

    private static final int REQUEST_CODE = 0; // 请求码
    private String mLastStackName = "";

    //以下为城市选择相关功能


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onResume() {
        super.onResume();

    }


    public void onPause() {
        super.onPause();
    }

    public boolean onBackPressed() {
        return false;
    }


    ProgressDialog mProgressDialog;

    protected ProgressDialog showProgress() {
        return showProgress((ViewGroup) getView());
    }


    protected ProgressDialog showProgress(ViewGroup viewParent) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog_.build(getActivity());
            mProgressDialog.addToView(viewParent);
        }
        mProgressDialog.show();
        return mProgressDialog;
    }


    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    protected void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.removeFromView();
            mProgressDialog = null;
        }
    }


    public void showProgress(@StringRes int msgRes) {
        ProgressDialog dialog = showProgress();
        dialog.setTipMsg(msgRes);
    }

    public void showProgress(String msg) {
        ProgressDialog dialog = showProgress();
        dialog.setTipMsg(msg);
    }

    public String getRightTile() {
        return null;
    }

    public void rightOnClick() {
    }

    public void startActivity(Class cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    public String getmLastStackName() {
        return mLastStackName;
    }


    public void setmLastStackName(String mLastStackName) {
        this.mLastStackName = mLastStackName;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setClickable(true);
    }

    @Override
    public void onDestroyView() {
        closeProgress();
        Tool.hideKeyboard(getActivity());
        super.onDestroyView();

    }

    public void showFragment(Context actovoty, BaseFragment fragment) {
        FragmentTransaction transaction = ((AppCompatActivity) actovoty).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(0, 0, 0, 0);
        transaction.add(R.id.container, fragment, fragment.getClass().getName());
        fragment.mLastStackName = "" + System.currentTimeMillis() + hashCode();
        transaction.addToBackStack(fragment.mLastStackName);
        transaction.commitAllowingStateLoss();
    }

    public void showFragmentReplace(Context actovoty, BaseFragment fragment) {
        FragmentTransaction transaction = ((AppCompatActivity) actovoty).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(0, 0, 0, 0);
        transaction.replace(R.id.container, fragment, fragment.getClass().getName());
        fragment.mLastStackName = "" + System.currentTimeMillis() + hashCode();
        transaction.addToBackStack(fragment.mLastStackName);
        transaction.commitAllowingStateLoss();
    }

    public void showFragment(Context actovoty, BaseFragment fragment, int fragmentlayoutid) {
        FragmentTransaction transaction = ((AppCompatActivity) actovoty).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(0, 0, 0, 0);
        transaction.add(fragmentlayoutid, fragment, fragment.getClass().getName());
        fragment.mLastStackName = "" + System.currentTimeMillis() + hashCode();
        transaction.addToBackStack(fragment.mLastStackName);
        transaction.commitAllowingStateLoss();
    }

    public void finishFragment() {
        getFragmentManager().popBackStackImmediate(mLastStackName,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }





    public interface CityListener {
        /**
         * 是否已经完成了地区选择
         *
         * @param city
         */
        void isChooseCity(String city);
    }

    /**
     * 检查相机权限
     *
     * @return
     */
    public boolean checkCameraPerms() {
        String[] permission = new String[]{Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE};
        boolean checked = PermissionUtili.checkPermission(getActivity(), permission, "需要设置手机权限", "需要使用相机和读取相册权限，请到设置中设置应用权限。");
        return checked;
    }

    /**
     * 检查文件权限
     *
     * @return
     */
    public boolean checkStoragePerms() {
        String[] permission = new String[]{Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE};
        boolean checked = PermissionUtili.checkPermission(getActivity(), permission, "需要设置手机权限", "需要使用读取和写入文件权限，请到设置中设置应用权限。");
        return checked;
    }


}
