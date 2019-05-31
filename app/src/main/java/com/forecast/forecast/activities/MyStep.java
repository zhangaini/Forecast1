package com.forecast.forecast.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.stepUtils.bean.StepEntity;
import com.forecast.forecast.stepUtils.calendar.BeforeOrAfterCalendarView;
import com.forecast.forecast.stepUtils.constant.Constant;
import com.forecast.forecast.stepUtils.db.StepDataDao;
import com.forecast.forecast.stepUtils.service.StepService;
import com.forecast.forecast.stepUtils.utils.StepCountCheckUtil;
import com.forecast.forecast.stepUtils.utils.TimeUtil;
import com.forecast.forecast.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//zlh 柱状图的界面
public class MyStep extends AppCompatActivity implements android.os.Handler.Callback {
    private LinearLayout movementCalenderLl;
    private TextView kmTimeTv;
    private TextView totalKmTv;
    private TextView mKaluli;
    private TextView stepsTimeTv;
    private TextView totalStepsTv;
    private TextView supportTv;

    private BarChart barchart;
    ArrayList <String>xAxisValue =new ArrayList<>();
    /**
     * 屏幕长度和宽度
     */
    public static int screenWidth, screenHeight;

    private BeforeOrAfterCalendarView calenderView;

    private String curSelDate;
    private DecimalFormat df = new DecimalFormat("#.##");
    private List<StepEntity> stepEntityList = new ArrayList<>();
    private StepDataDao stepDataDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystep);

        initView();
        initData();
        initListener();
        //zlh 获取柱状图 xml有相应的布局
        InitBarChart();
    }


    private void initView() {
        movementCalenderLl = (LinearLayout) findViewById(R.id.movement_records_calender_ll);
        kmTimeTv = (TextView) findViewById(R.id.movement_total_km_time_tv);
        totalKmTv = (TextView) findViewById(R.id.movement_total_km_tv);
        mKaluli=(TextView)findViewById(R.id.kaluli);
        stepsTimeTv = (TextView) findViewById(R.id.movement_total_steps_time_tv);
        totalStepsTv = (TextView) findViewById(R.id.movement_total_steps_tv);
        supportTv = (TextView) findViewById(R.id.is_support_tv);

        curSelDate = TimeUtil.getCurrentDate();
    }

    private void initData() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        //放到获取宽度之后
        calenderView = new BeforeOrAfterCalendarView(this);
        movementCalenderLl.addView(calenderView);
        /**
         * 这里判断当前设备是否支持计步
         */
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            getRecordList();
            supportTv.setVisibility(View.GONE);
            setDatas();
            setupService();
        } else {
            totalStepsTv.setText("0");
            supportTv.setVisibility(View.VISIBLE);
        }
    }


    private void initListener() {
        calenderView.setOnBoaCalenderClickListener(new BeforeOrAfterCalendarView.BoaCalenderClickListener() {
            @Override
            public void onClickToRefresh(int position, String curDate) {
                //获取当前选中的时间
                curSelDate = curDate;
                //根据日期去取数据
                setDatas();
            }
        });
    }


    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Messenger messenger;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;
    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        messenger = new Messenger(service);
                        Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * 设置记录数据
     *
     */
    private void setDatas() {
        StepEntity stepEntity = stepDataDao.getCurDataByDate(curSelDate);

        if (stepEntity != null) {
            int steps = Integer.parseInt(stepEntity.getSteps());

            //获取全局的步数
            totalStepsTv.setText(String.valueOf(steps));
            //计算总公里数
            totalKmTv.setText(countTotalKM(steps));
            //计算卡路里消耗
            mKaluli.setText((int)(Double.parseDouble(countTotalKM(steps))*117)+"");
        } else {
            //获取全局的步数
            totalStepsTv.setText("0");
            //计算总公里数
            totalKmTv.setText("0");
            mKaluli.setText("0");
        }

        //设置时间
        String time = TimeUtil.getWeekStr(curSelDate);
        kmTimeTv.setText(time);
        stepsTimeTv.setText(time);
    }

    /**
     * 简易计算公里数，假设一步大约有0.6米
     *
     * @param steps 用户当前步数
     * @return
     */
    private String countTotalKM(int steps) {
        double totalMeters = steps * 0.6;
        //保留两位有效数字
        return df.format(totalMeters / 1000);
    }


    /**
     * 获取全部运动历史纪录
     */
    private void getRecordList() {
        //获取数据库
        stepDataDao = new StepDataDao(this);
        stepEntityList.clear();
        stepEntityList.addAll(stepDataDao.getAllDatas());
        if (stepEntityList.size() >= 7) {
            // TODO: 2017/3/27 在这里获取历史记录条数，当条数达到7条或以上时，就开始删除第七天之前的数据,暂未实现

        }

    }

    private void getBarChart() {



    }





    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            //这里用来获取到Service发来的数据
            case Constant.MSG_FROM_SERVER:

                //如果是今天则更新数据
                if (curSelDate.equals(TimeUtil.getCurrentDate())) {
                    //记录运动步数
                    int steps = msg.getData().getInt("steps");
                    //设置的步数
                    totalStepsTv.setText(String.valueOf(steps));
                    //计算总公里数
                    totalKmTv.setText(countTotalKM(steps));
                }
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) this.unbindService(conn);
    }

    private void InitBarChart() {
        barchart = (BarChart) findViewById(R.id.barchart);
        barchart.setDrawBarShadow(false);//true绘画的Bar有阴影。
        barchart.setDrawValueAboveBar(true);//true文字绘画在bar上
        barchart.getDescription().setEnabled(false);
        barchart.setMaxVisibleValueCount(60);
        barchart.setPinchZoom(false);//false只能单轴缩放
        barchart.setDrawGridBackground(false);
        //x坐标轴设置

        xAxisValue.clear();
        xAxisValue.add("1月");
        xAxisValue.add("2月");
        xAxisValue.add("3月");
        xAxisValue.add("4月");
        xAxisValue.add("5月");
        xAxisValue.add("6月");
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
        leftAxis.setLabelCount(6, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(50f);

        Legend l = barchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        setData(6, 50);
    }
    private void setData(int count, float range) {
        float start = 0f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = (int) start; i < start + count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "步数统计图/km");
        set1.setDrawIcons(false);
        set1.setColor(ColorTemplate.rgb("#44CDC5"));
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

}
