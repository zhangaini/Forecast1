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
import com.forecast.forecast.stepUtils.bean.StepEntity;
import com.forecast.forecast.stepUtils.db.StepDataDao;
import com.forecast.forecast.stepUtils.utils.HourStepsUtils;
import com.forecast.forecast.stepUtils.utils.TimeUtil;
import com.forecast.forecast.utils.SecurePreferences;
import com.forecast.forecast.view.progress.Heartrate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    ArrayList <String>xAxisValue =new ArrayList<>();
    ArrayList <String>xAxisValue2 =new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#.##");
    private List<StepEntity> stepEntityList = new ArrayList<>();
    private StepDataDao stepDataDao;
    private String TAG="RNG";
    private String curSelDate;
    private BarChart barchart;
    private BarChart barchart2;
    private LineChart mLineHeartChart;
    List<Entry> LineHeartentries = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_main);

        initView();
        HourStepsUtils.setHourSteps(TimeUtil.getCurHour(),getHourSteps(),this);
        InitBarChartHeart();
        InitBarChartSteps();
        //存储当前时间的步数
        //折线图
        InitLineChartHeart();
    }

    private void InitLineChartHeart() {
        mLineHeartChart = (LineChart) findViewById(R.id.line_heart_chart);
        mLineHeartChart.getAxisRight().setEnabled(false);
        mLineHeartChart.getDescription().setEnabled(false);
        XAxis xAxis = mLineHeartChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//1.设置x轴和y轴的点
//
//        for (int i = 0; i < LineHeartentries.size(); i++)
//            LineHeartentries.add(new Entry(i, new Random().nextInt(300)));
        LineDataSet dataSet = new LineDataSet(LineHeartentries, "心率/次"); // add entries to dataset
//3.chart设置数据
        LineData lineData = new LineData(dataSet);
        mLineHeartChart.setData(lineData);
        mLineHeartChart.invalidate(); // refresh

    }

    private void InitBarChartSteps() {

        barchart2 = (BarChart) findViewById(R.id.stepschart);
        barchart2.clear();
        barchart2.setDrawBarShadow(false);//true绘画的Bar有阴影。
        barchart2.setDrawValueAboveBar(true);//true文字绘画在bar上
        barchart2.getDescription().setEnabled(false);
        barchart2.setMaxVisibleValueCount(60);
        barchart2.setPinchZoom(false);//false只能单轴缩放
        barchart2.setDrawGridBackground(false);

        //zlh  自定义x坐标轴
        // TODO: 2019/6/1 0001 emm取出最近一周的日期
        xAxisValue2.clear();
        List<Integer> aWeekDay =TimeUtil.getCurrentweekDay();
        for (int i=0;i<24;i++) {
//            List<String> aWeekDay=  TimeUtil.getBeforeDateListByNow();
//            //一周日期其中一天
//            StepEntity stepEntity = stepDataDao.getCurDataByDate(aWeekDay.get(i));

            //将一周的号数填入
            xAxisValue2.add(""+(i+1));

        }
        XAxis xAxis = barchart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setGranularity(0.5f);//底部X轴值的分割间距
        xAxis.setLabelCount(xAxisValue2.size());

        xAxis.setCenterAxisLabels(false);//设置底部X标签居中

//        XAxis xAxis = barchart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValue2));

        //设置Y轴
        barchart2.getAxisRight().setEnabled(false);//隐藏右边的坐标轴
        YAxis leftAxis = barchart2.getAxisLeft();
        leftAxis.setLabelCount(24, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinimum(0f);
        //设置Y轴的最大坐标
        leftAxis.setAxisMaximum(150f);
        //设置Y轴的间隔精度
        leftAxis.setGranularity(50f);
        Legend l = barchart2.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(6f);
        l.setXEntrySpace(4f);
        setstepsData(24, 50);
    }

    private void setstepsData(int count, float range){
        float start = 0f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = (int) start; i < start + count; i++) {
            float mult = (range + 1);

            //todo 获取真数据
            //获取一周的日期
            List<String> aWeekDay=  TimeUtil.getBeforeDateListByNow();
            //一周日期其中一天
            //没东西 会报空指针 换一种写法
            //获取当前的时间 来存取今日心率
            float val=0;
                //todo 步数
                //获取现在时间的步数

                val = HourStepsUtils.getHourSteps(this,i+"");


            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "步数统计图/次");
        set1.setDrawIcons(false);
        set1.setColor(ColorTemplate.rgb("#FFFFFF"));
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        barchart2.setData(data);
        barchart2.getBarData().setBarWidth(0.3f);//设置柱状图的宽度
        barchart2.setScaleXEnabled(false);//禁止X轴缩放

//        barchart.getXAxis().setAxisMinimum(0);
//        barchart.animateY(1000, Easing.Linear);
//        barchart.animateX(1000, Easing.Linear);
    }
    private void InitBarChartHeart() {

        barchart = (BarChart) findViewById(R.id.heartchart);
        barchart.clear();
        barchart.setDrawBarShadow(false);//true绘画的Bar有阴影。
        barchart.setDrawValueAboveBar(true);//true文字绘画在bar上
        barchart.getDescription().setEnabled(false);
        barchart.setMaxVisibleValueCount(60);
        barchart.setPinchZoom(false);//false只能单轴缩放
        barchart.setDrawGridBackground(false);

        //zlh  自定义x坐标轴
        // TODO: 2019/6/1 0001 emm取出最近一周的日期
        xAxisValue.clear();
        List<Integer> aWeekDay =TimeUtil.getCurrentweekDay();
        for (int i=0;i<24;i++) {
//            List<String> aWeekDay=  TimeUtil.getBeforeDateListByNow();
//            //一周日期其中一天
//            StepEntity stepEntity = stepDataDao.getCurDataByDate(aWeekDay.get(i));

            //将一周的号数填入
            xAxisValue.add(""+(i+1));

        }
        XAxis xAxis = barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setGranularity(0.5f);//底部X轴值的分割间距
        xAxis.setLabelCount(xAxisValue.size());

        xAxis.setCenterAxisLabels(false);//设置底部X标签居中

//        XAxis xAxis = barchart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValue));

        //设置Y轴
        barchart.getAxisRight().setEnabled(false);//隐藏右边的坐标轴
        YAxis leftAxis = barchart.getAxisLeft();
        leftAxis.setLabelCount(24, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinimum(0f);
        //设置Y轴的最大坐标
        leftAxis.setAxisMaximum(150f);
        //设置Y轴的间隔精度
        leftAxis.setGranularity(50f);
        Legend l = barchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(6f);
        l.setXEntrySpace(4f);
        setData(24, 50);
    }
    private void setData(int count, float range) {
        float start = 0f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = (int) start; i < start + count; i++) {
            float mult = (range + 1);

            //todo 获取真数据
            //获取一周的日期
            List<String> aWeekDay=  TimeUtil.getBeforeDateListByNow();
            //一周日期其中一天
            //没东西 会报空指针 换一种写法
           //获取当前的时间 来存取今日心率
            float val=0;
            if (Integer.parseInt(TimeUtil.getCurHour())>=i){

                val = (float) (Math.floor(Math.random()*40)+60);
                LineHeartentries.add(new Entry(i,val));
            }
            else {
                val = (float) 0;
            }
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "心率统计图/次");
        set1.setDrawIcons(false);
        set1.setColor(ColorTemplate.rgb("#FFFFFF"));
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        barchart.setData(data);
        barchart.getBarData().setBarWidth(0.3f);//设置柱状图的宽度
        barchart.setScaleXEnabled(false);//禁止X轴缩放

//        barchart.getXAxis().setAxisMinimum(0);
//        barchart.animateY(1000, Easing.Linear);
//        barchart.animateX(1000, Easing.Linear);
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
    //返回当前时间的步数
    public int getHourSteps(){
        int mSteps=HourStepsUtils.getSteps(this);
        for (int i=1;i<Integer.parseInt(TimeUtil.getCurHour());i++) {
            mSteps=mSteps - HourStepsUtils.getHourSteps(this,i+"");
        }
        return mSteps;
    }
}
