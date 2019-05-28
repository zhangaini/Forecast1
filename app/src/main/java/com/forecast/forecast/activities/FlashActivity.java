package com.forecast.forecast.activities;

import android.content.Intent;
import android.os.AsyncTask;
import com.forecast.forecast.R;
import com.forecast.forecast.activities.base.BaseActivity;
import com.forecast.forecast.constants.Constant;
import com.forecast.forecast.utils.SecurePreferences;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/*
*  启动页
*/


@EActivity(R.layout.activity_flash)
public class FlashActivity extends BaseActivity {


    @AfterViews
    void initView() {

        //启动页延时5秒进入登录
        new FlashAsynTask().execute();

    }

    class FlashAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            int roleId = SecurePreferences.getInstance().getInt(Constant.TYPE, 0);
            if (roleId ==1){
                startActivity(new Intent(getActivity(),AdminMainActivity_.class));
            }else if(roleId == 0){
                startActivity(new Intent(getActivity(),LoginActivity_.class));
            }else{
                startActivity(new Intent(getActivity(),MainActivity_.class));
            }
            finish();

        }
    }
}
