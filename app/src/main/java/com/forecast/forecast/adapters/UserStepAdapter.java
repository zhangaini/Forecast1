package com.forecast.forecast.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.forecast.forecast.R;
import com.forecast.forecast.intefaces.AdapterListener;
import com.forecast.forecast.models.Step;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：首页列表
 * 创建时间： 2017/8/9 11:31
 **/
public class UserStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int VIEWTILE = 1;
    private Context mContext;
    private List<Step> list_object = new ArrayList<>();
    private AdapterListener adapterListener;

    public UserStepAdapter(Context mContext, AdapterListener adapterListener){
        this.mContext = mContext;
        this.adapterListener = adapterListener;
    }

    public void setData(List<Step> list_object){
        this.list_object = list_object;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTILE){
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_user_step, parent, false);
            return new MyViewHold(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHold)holder).tv_time.setText(list_object.get(position).getTime().getDate().substring(0,10));

        ((MyViewHold)holder).tv_number.setText(list_object.get(position).getStepNumber());

        ((MyViewHold)holder).tvDistance.setText("距离:"+list_object.get(position).getDistance()
        +"公里   热量:"+list_object.get(position).getQuantity()+"千卡");


    }

    @Override
    public int getItemCount() {
        return list_object.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEWTILE;
    }

    /*viewhold*/
    class MyViewHold extends RecyclerView.ViewHolder{

        private TextView tv_time,tv_number;
        private TextView tvDistance;
        private Button bt_modify;
        public MyViewHold(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            bt_modify = (Button) itemView.findViewById(R.id.bt_modify);
            tvDistance = (TextView)itemView.findViewById(R.id.tv_distance);
        }
    }
}
