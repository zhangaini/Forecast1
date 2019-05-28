package com.forecast.forecast.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.fragments.base.BaseFragment;
import com.forecast.forecast.models.Contact;
import com.forecast.forecast.models.RateClam;
import com.forecast.forecast.models.RateTime;
import com.forecast.forecast.utils.PermissionUtili;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.view.RategramView;
import com.forecast.forecast.view.progress.WaterWaveProgress;
import com.forecast.forecast.view.widget.NavBar;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * 作者：
 * 创建时间
 * 功能描述：首页
 */
@EFragment(R.layout.fragment_rates)
public class UserRateTimeFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBar navBar;
    @ViewById(R.id.waterWaveProgress1)
    WaterWaveProgress waterWaveProgress;
    @ViewById(R.id.tv_rate)
    TextView tvRate;

    @ViewById(R.id.rategram)
    RategramView rategramView;

    private String objectId;

    private int[] data = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    private int[] text = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    private String[] weeks = new String[] { "", "", "", "", "", "", "" };

    @AfterViews
    void initView(){
        navBar.setMiddleTitle("心率");
        navBar.setOnMenuClickListener(new NavBar.OnMenuClickListener() {
            @Override
            public void onRightMenuClick(View view) {
                super.onRightMenuClick(view);
            }
        });
        objectId = SecurePreferences.getInstance().getString(Constant.USERID,"");

        getData();


    }

    public void getData(){
        BmobQuery<RateTime> rateTimeBmobQuery = new BmobQuery<>();
        rateTimeBmobQuery.addWhereEqualTo("UserId",objectId);
        rateTimeBmobQuery.order("-Time");
        rateTimeBmobQuery.findObjects(getActivity(), new FindListener<RateTime>() {
            @Override
            public void onSuccess(List<RateTime> list) {
                if (list.size()>0){
                    waterWaveProgress.setShowProgress(false);
                    waterWaveProgress.setProgress(Integer.valueOf(list.get(0).getNumber()));
                    tvRate.setText("心率值:"+list.get(0).getNumber());

                    if (Integer.valueOf(list.get(0).getNumber()) > 150 ||Integer.valueOf(list.get(0).getNumber()) <40 ){
                        getContact();
                    }
                }
                initCart(list);

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Click(R.id.tv_more)
    void tvMore(){

        showFragment(getActivity(),RateTimeMoreFragment_.builder().build());
    }


    /**
     * 获取联系人
     */
    public void getContact(){

        String[] permission = new String[]{Manifest.permission.CALL_PHONE};
        boolean check = PermissionUtili.checkPermission(getActivity(),permission,"需要设置手机权限","需要拨打电话权限");

        if (check){
            BmobQuery<Contact> contactBmobQuery = new BmobQuery<>();
            contactBmobQuery.addWhereEqualTo("UserId",objectId);
            contactBmobQuery.findObjects(getActivity(), new FindListener<Contact>() {
                @Override
                public void onSuccess(final List<Contact> list) {
                    if (list.size() > 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("心率危险，需要联系紧急联系人？");
                        builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+list.get(0).getPhone()));
                                startActivity(intent);

                            }
                        });
                        builder.setNegativeButton("取消",null);
                        builder.show();
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });

        }




    }



    public void initCart(List<RateTime> rateClams){

        for (int i=0;i<rateClams.size();i++){
            data[data.length -(i+1)] = Integer.valueOf(rateClams.get(i).getNumber());
            weeks[data.length-(i+1)] = rateClams.get(i).getTime().getDate().substring(11,13)+":"+
                    rateClams.get(i).getTime().getDate().substring(14,16);

        }
        rategramView.setWeek(weeks);
        rategramView.setProgress(data);


        rategramView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int step = (v.getWidth() - 30) / 8;
                int x = (int) event.getX();
                for (int i = 0; i < 7; i++) {
                    if (x > (30 + step * (i + 1) - 30)
                            && x < (30 + step * (i + 1) + 30)) {
                        text[i] = 1;
                        for (int j = 0; j < 7; j++) {
                            if (i != j) {
                                text[j] = 0;
                            }
                        }
                        rategramView.setText(text);
                    }
                }

                return false;
            }
        });

    }

}
