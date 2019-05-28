package com.forecast.forecast.fragments;

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
import com.forecast.forecast.models.RateUp;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.view.RategramView;
import com.forecast.forecast.view.widget.NavBarBack;

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
 * 功能描述：
 */
@EFragment(R.layout.fragment_user_rate_clam)
public class UserRateUpFragment extends BaseFragment {

    @ViewById(R.id.mNavBar)
    NavBarBack navBar;

    @ViewById(R.id.tv_rate_value)
    TextView tvRateValue;
    @ViewById(R.id.tv_rate_time)
    TextView tvRateTime;
    @ViewById(R.id.tv_state)
    TextView tvState;
    @ViewById(R.id.rategram)
    RategramView rategramView;

    private String objectId;

    private int[] data = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    private int[] text = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    private String[] weeks = new String[] { "", "", "", "", "", "", "" };

    @AfterViews
    void initView(){


        objectId = SecurePreferences.getInstance().getString(Constant.USERID,"");

        navBar.setMiddleTitle("静态心率");
        navBar.setOnMenuClickListener(new NavBarBack.OnMenuClickListener() {
            @Override
            public void onLeftMenuClick(View view) {
                super.onLeftMenuClick(view);
                finishFragment();
            }
        });
        getData();
    }


    /**
     * 更多
     */
    @Click(R.id.tv_more)
    void tvMore(){

        showFragment(getActivity(),RateUpMoreFragment_.builder().build());
    }



    /**
     * 获取数据
     */
    public void getData(){

        BmobQuery<RateUp> rateClamBmobQuery = new BmobQuery<>();
        rateClamBmobQuery.addWhereEqualTo("UserId",objectId);
        rateClamBmobQuery.order("-Time");
        rateClamBmobQuery.findObjects(getActivity(), new FindListener<RateUp>() {
            @Override
            public void onSuccess(List<RateUp> list) {

                if (list.size()>0){
                    tvRateValue.setText("心率值:" + list.get(0).getNumber());
                    tvRateTime.setText("测试时间:"+ list.get(0).getTime().getDate());

                    if (Integer.valueOf(list.get(0).getNumber()) > 150 ||Integer.valueOf(list.get(0).getNumber()) <40 ){
                        tvState.setText("状态:危险");

                        getContact();
                    }else{
                        tvState.setText("状态:正常");
                    }

                    initCart(list);

                }



            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 获取联系人
     */
    public void getContact(){

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


    public void initCart(List<RateUp> rateClams){

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
