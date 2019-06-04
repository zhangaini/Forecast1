package com.forecast.forecast.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.forecast.forecast.R;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.models.Contact;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.view.progress.Heartrate;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 *
 * 新首页
 *
 */
public class NewMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Heartrate waterWaveProgress,waterWaveProgress2;
    private String objectId;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_main);

        initView();
    }


    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int rand = (int)(Math.random()*71) + 70;
                    if (count % 10 == 0) {
                        if (rand > 100) {
                            getContact();
                        }
                    } else {
                        if (rand > 100) {
                            rand = 93;
                        }
                    }
                    Heartrate.mCount = rand;
                    waterWaveProgress.setProgress(rand);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    void initView() {
        waterWaveProgress2 = (Heartrate) findViewById(R.id.step_number);
        waterWaveProgress2.setOnClickListener(this);

        waterWaveProgress = (Heartrate) findViewById(R.id.heart_rate);

        Timer timer = new Timer();
        TimerTask MyTask = new TimerTask() {
            @Override
            public void run() {
                count++;
                Message msg = new Message();
                msg.what = 1;
                myHandler.sendMessage(msg);
            }
        };

        timer.schedule(MyTask, 1000,4000);
    }

    @Override
    public void onClick(View view) {
        if (view == waterWaveProgress2) {
            startActivity(new Intent(NewMainActivity.this, MyStep.class));
        }

    }

    public void getContact(){

        objectId = SecurePreferences.getInstance().getString(Constant.USERID,"");

        BmobQuery<Contact> contactBmobQuery = new BmobQuery<>();
        contactBmobQuery.addWhereEqualTo("UserId",objectId);
        contactBmobQuery.findObjects(NewMainActivity.this, new FindListener<Contact>() {
            @Override
            public void onSuccess(final List<Contact> list) {
                if (list.size() > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewMainActivity.this);
                    builder.setMessage("心率危险，需要联系紧急联系人？");
                    builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+list.get(0).getPhone()));
                            try {
                                startActivity(intent);
                            } catch (SecurityException e) {
                                System.out.print("拨号权限异常：" + e);
                            }

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
