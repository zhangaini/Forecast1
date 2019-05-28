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
import com.forecast.forecast.models.RateClam;
import com.forecast.forecast.models.RateUp;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：首页列表
 * 创建时间： 2017/8/9 11:31
 **/
public class RateUpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int VIEWTILE = 1;
    private Context mContext;
    private List<RateUp> list_object = new ArrayList<>();
    private AdapterListener adapterListener;

    public RateUpAdapter(Context mContext, AdapterListener adapterListener){
        this.mContext = mContext;
        this.adapterListener = adapterListener;
    }

    public void setData(List<RateUp> list_object){
        this.list_object = list_object;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTILE){
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_step, parent, false);
            return new MyViewHold(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHold)holder).tv_time.setText(list_object.get(position).getTime().getDate().substring(0,16));

        ((MyViewHold)holder).tv_number.setText(list_object.get(position).getNumber());

        ((MyViewHold)holder).bt_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapterListener.setItemClickListener(list_object.get(position),position);
            }
        });
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
        private Button bt_modify;
        public MyViewHold(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            bt_modify = (Button) itemView.findViewById(R.id.bt_modify);
        }
    }
}
