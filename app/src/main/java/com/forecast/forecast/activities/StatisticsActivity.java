package com.forecast.forecast.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.stepUtils.bean.StepEntity;
import com.forecast.forecast.stepUtils.db.StepDataDao;
import com.forecast.forecast.stepUtils.utils.TimeUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    private BarChart barchart;
    private Button btnDay;
    private Button btnWeek;
    private Button btnMon;
    private TextView mStatistics_date;//顶部日期
    private TextView mAll_Km;
    private TextView mAll_Date;
    private TextView mAll_Steps;
    private TextView mAll_Kaluli;
    private ImageView mBack;
    ArrayList <String>xAxisValue =new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#.##");
    private List<StepEntity> stepEntityList = new ArrayList<>();
    private StepDataDao stepDataDao;
    private String TAG="RNG";
    private String curSelDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        //获取所有的运动步数
        getRecordList();
        //zlh 获取柱状图 xml有相应的布局
        InitView();
        InitBarChart();
        InitDate();
    }

    private void InitDate() {
        String stepsTheDay="非空";
        StepEntity stepEntity=null;
        curSelDate = TimeUtil.getCurrentDate();
        try {
            stepEntity = stepDataDao.getCurDataByDate(curSelDate);
        }
        catch (Exception e){
            stepsTheDay=null;
        }
        if (stepsTheDay!= null&&stepEntity!=null) {
            int steps = Integer.parseInt(stepEntity.getSteps());

            //获取全局的步数
            mAll_Steps.setText(String.valueOf(steps)+"步");
            //计算总公里数
            mAll_Km.setText(countTotalKM(steps)+"公里");
            //计算卡路里消耗
            mAll_Kaluli.setText((int)(Double.parseDouble(countTotalKM(steps))*117)+"卡路里");


        }
        mAll_Date.setText("今天");
    }

    private void InitView() {
        //绑定控件  点击统计相应的日周月的数据
        btnDay= (Button) findViewById(R.id.btn_day);
        btnWeek=(Button)findViewById(R.id.btn_week);
        btnMon=(Button)findViewById(R.id.btn_mon);
        btnDay.setOnClickListener(this);
        btnWeek.setOnClickListener(this);
        btnMon.setOnClickListener(this);
        mStatistics_date=(TextView)findViewById(R.id.statistics_date);//顶部日期
        mAll_Km=(TextView)findViewById(R.id.all_km);
        mAll_Date=(TextView)findViewById(R.id.all_Date);
        mAll_Steps=(TextView)findViewById(R.id.all_Steps);
        mAll_Kaluli=(TextView)findViewById(R.id.all_Kaluli);
        mBack=(ImageView)findViewById(R.id.back);
        mStatistics_date.setOnClickListener(this);
        mAll_Km.setOnClickListener(this);
        mAll_Date.setOnClickListener(this);
        mAll_Steps.setOnClickListener(this);
        mAll_Kaluli.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mStatistics_date.setText(TimeUtil.getCurrentDate());
        mAll_Date.setText("今天");
    }

    private void InitBarChart() {

        barchart = (BarChart) findViewById(R.id.barchart);
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
        for (int i=0;i<aWeekDay.size();i++) {
//            List<String> aWeekDay=  TimeUtil.getBeforeDateListByNow();
//            //一周日期其中一天
//            StepEntity stepEntity = stepDataDao.getCurDataByDate(aWeekDay.get(i));

            //将一周的号数填入
            xAxisValue.add(""+aWeekDay.get(i));

        }


//        xAxisValue.add("1月");
//        xAxisValue.add("2月");
//        xAxisValue.add("3月");
//        xAxisValue.add("4月");
//        xAxisValue.add("5月");
//        xAxisValue.add("6月");

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
        leftAxis.setLabelCount(7, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinimum(0f);
        //设置Y轴的最大坐标
        leftAxis.setAxisMaximum(1f);

        Legend l = barchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        setData(7, 50);
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
            String stepsTheDay="非空";
            StepEntity stepEntity=null;
            try {
                stepEntity = stepDataDao.getCurDataByDate(aWeekDay.get(i));
            }
            catch (Exception e){
                stepsTheDay=null;
            }
            float val=1;
            if (stepsTheDay!=null&&stepEntity!=null){
                int steps = Integer.parseInt(stepEntity.getSteps());
                DecimalFormat fnum=new DecimalFormat("##0.00");
                Double tempNumber=Double.parseDouble((countTotalKM(steps)));
                val= Float.parseFloat(tempNumber+"");



            }
            else {
                val = (float) 0;
            }
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "步数统计图/km");
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

    @Override
    public void onClick(View v) {
        if (v == btnDay){
            //加载今天的数据统计
            Log.i(TAG, "onClick: btnDay");
            //柱状图加载今天的数据 默认选择是今天
            //加载近一个月的数据

            String stepsTheDay="非空";
            StepEntity stepEntity=null;
            curSelDate = TimeUtil.getCurrentDate();
            try {
                stepEntity = stepDataDao.getCurDataByDate(curSelDate);
            }
            catch (Exception e){
                stepsTheDay=null;
            }
            if (stepsTheDay!= null&&stepEntity!=null) {
                int steps = Integer.parseInt(stepEntity.getSteps());

                //获取全局的步数
                mAll_Steps.setText(String.valueOf(steps)+"步");
                //计算总公里数
                mAll_Km.setText(countTotalKM(steps)+"公里");
                //计算卡路里消耗
                mAll_Kaluli.setText((int)(Double.parseDouble(countTotalKM(steps))*117)+"卡路里");


            }
            mAll_Date.setText("今天");

        }
        else if (v == btnWeek){
            //加载近七天的数据统计
            Log.i(TAG, "onClick: btnWeek");
            //获取一周的日期
            List<String> aWeekDay=  TimeUtil.getBeforeDateListByNow();
            //一周日期其中一天
            //没东西 会报空指针 换一种写法
            String stepsTheDay="非空";
            StepEntity stepEntity=null;
            Double allKM = 0.0;
            int allSteps = 0;
            for (int i=0;i<7;i++) {
                try {
                    stepEntity = stepDataDao.getCurDataByDate(aWeekDay.get(i));
                } catch (Exception e) {
                    stepsTheDay = null;
                }

                if (stepsTheDay != null && stepEntity != null) {
                    int steps = Integer.parseInt(stepEntity.getSteps());
                    DecimalFormat fnum = new DecimalFormat("##0.00");
                    Double tempNumber = Double.parseDouble((countTotalKM(steps)));
                    allKM=allKM+tempNumber;
                    allSteps=allSteps+steps;
                }
                    //获取全局的步数
                    mAll_Steps.setText(String.valueOf(allSteps)+"步");
                    //计算总公里数
                    mAll_Km.setText(allKM+"公里");
                    //计算卡路里消耗
                    mAll_Kaluli.setText((int) (Double.parseDouble(countTotalKM(allSteps)) * 117) +"卡路里");
                    mAll_Date.setText("近七天");

                }


        }
        else if (v == btnMon){
            //加载近一个月的数据
            Log.i(TAG, "onClick: btnMon");
            //获取一周的日期
            List<String> aMonDay=  TimeUtil.getBeforeMonthDateListByNow();
            //一周日期其中一天
            //没东西 会报空指针 换一种写法
            String stepsTheDay="非空";
            StepEntity stepEntity=null;
            Double allKM = 0.0;
            int allSteps = 0;
            for (int i=0;i<7;i++) {
                try {
                    stepEntity = stepDataDao.getCurDataByDate(aMonDay.get(i));
                } catch (Exception e) {
                    stepsTheDay = null;
                }

                if (stepsTheDay != null && stepEntity != null) {
                    int steps = Integer.parseInt(stepEntity.getSteps());
                    DecimalFormat fnum = new DecimalFormat("##0.00");
                    Double tempNumber = Double.parseDouble((countTotalKM(steps)));
                    allKM=allKM+tempNumber;
                    allSteps=allSteps+steps;
                }
                //获取全局的步数
                mAll_Steps.setText(String.valueOf(allSteps)+"步");
                //计算总公里数
                mAll_Km.setText(allKM+"公里");
                //计算卡路里消耗
                mAll_Kaluli.setText((int) (Double.parseDouble(countTotalKM(allSteps)) * 117) +"卡路里");
                mAll_Date.setText("近30天");

            }
        }
        else if (v == mStatistics_date){

        }
        else if (v == mAll_Steps){
            //步数统计
            Log.i(TAG, "onClick: mAll_Steps");
        }
        else if (v == mAll_Date){
            //活动日期
            Log.i(TAG, "onClick: mAll_Date");
        }
        else if (v == mAll_Km){
            //公里数
            Log.i(TAG, "onClick: mAll_Km");
        }
        else if (v == mAll_Kaluli){
            //消耗卡路里
            Log.i(TAG, "onClick: mAll_Kaluli");
        }
        else if (v==mBack)
        {
            finish();
        }


    }
    private  void getRecordList() {
        //获取数据库
        stepDataDao = new StepDataDao(this);
        stepEntityList.clear();
        stepEntityList.addAll(stepDataDao.getAllDatas());
        if (stepEntityList.size() >= 7) {
            // TODO: 2017/3/27 在这里获取历史记录条数，当条数达到7条或以上时，就开始删除第七天之前的数据,暂未实现

        }

    }
}
