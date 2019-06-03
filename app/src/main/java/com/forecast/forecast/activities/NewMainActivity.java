package com.forecast.forecast.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.forecast.forecast.R;
import com.forecast.forecast.view.progress.Heartrate;
import com.forecast.forecast.view.progress.WaterWaveProgress;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * 新首页
 *
 */
public class NewMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Heartrate waterWaveProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_main);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void initView() {

        waterWaveProgress = (Heartrate) findViewById(R.id.heart_rate);

//        Timer timer = new Timer();
//        TimerTask MyTask = new TimerTask() {
//            @Override
//            public void run() {
                waterWaveProgress = (Heartrate) findViewById(R.id.heart_rate);
                int rand = (int)(Math.random()*71) + 70;
                Heartrate.mCount = rand;
                waterWaveProgress.setProgress(rand);
//            }
//        };
//
//        timer.schedule(MyTask, 5000);
    }

    @Override
    public void onClick(View view) {

    }
}
