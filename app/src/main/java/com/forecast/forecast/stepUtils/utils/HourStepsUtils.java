package com.forecast.forecast.stepUtils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.forecast.forecast.stepUtils.bean.StepEntity;
import com.forecast.forecast.stepUtils.db.StepDataDao;

import java.util.ArrayList;
import java.util.List;

//把今天每个小时的步数存入 临时文件
public class HourStepsUtils{
    private static StepDataDao stepDataDao;
    private static String curSelDate;
    private static List<StepEntity> stepEntityList = new ArrayList<>();
    public static int getHourSteps(Context context,String Date){
    SharedPreferences myPreference=context.getSharedPreferences(Date, Context.MODE_PRIVATE);
        int step=myPreference.getInt(Date,0);
        return step;
    }
    public static void setHourSteps(String Date,int steps,Context context){
        //获取SharedPreferences对象
        SharedPreferences myPreference=context.getSharedPreferences(Date, Context.MODE_PRIVATE);
        //像SharedPreference中写入数据需要使用Editor
        SharedPreferences.Editor editor = myPreference.edit();

//存入键值对数据，注意此处的put[type]("key",value);
        editor.putInt(Date, steps);
//提交保存
//editor.apply();
        editor.commit();
    }
    public static int getSteps(Context context){
        getRecordList(context);
        String stepsTheDay="非空";
        StepEntity stepEntity=null;
        curSelDate = TimeUtil.getCurrentDate();
        int steps=0;
        try {
            stepEntity = stepDataDao.getCurDataByDate(curSelDate);
        }
        catch (Exception e){
            stepsTheDay=null;
        }
        if (stepsTheDay!= null&&stepEntity!=null) {
            steps = Integer.parseInt(stepEntity.getSteps());

        } else {
            //获取全局的步数
            steps=0;
        }
        return steps;
    }
    private static void getRecordList(Context context) {
        //获取数据库
        stepDataDao = new StepDataDao(context);
        stepEntityList.clear();
        stepEntityList.addAll(stepDataDao.getAllDatas());
        if (stepEntityList.size() >= 7) {
            // TODO: 2017/3/27 在这里获取历史记录条数，当条数达到7条或以上时，就开始删除第七天之前的数据,暂未实现

        }

    }
}
