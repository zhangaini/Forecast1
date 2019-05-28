package com.forecast.forecast.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.forecast.forecast.R;
import com.forecast.forecast.intefaces.AdapterListener;
import com.forecast.forecast.models.User;
import com.forecast.forecast.utils.ImageLoader;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;


/**
 * 描述：首页列表
 * 创建时间： 2017/8/9 11:31
 **/
public class AdminHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int VIEWTILE = 1;
    private Context mContext;
    private List<User> list_object = new ArrayList<>();
    private AdapterListener adapterListener;

    public AdminHomeAdapter(Context mContext, AdapterListener adapterListener){
        this.mContext = mContext;
        this.adapterListener = adapterListener;
    }

    public void setData(List<User> list_object){
        this.list_object = list_object;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTILE){
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_admin_home, parent, false);
            return new MyViewHold(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        ((MyViewHold)holder).tv_name.setText(list_object.get(position).getAccount());
        ((MyViewHold)holder).tv_nick.setText(list_object.get(position).getNickName());

        if (list_object.get(position).getImg() != null ){
            ImageLoader.loadImage(list_object.get(position).getImg().getUrl(), ((MyViewHold)holder).iv_header);
        }else{
            ((MyViewHold)holder).iv_header.setBackgroundResource(R.drawable.timg);
        }
        ((MyViewHold)holder).itemView.setOnClickListener(new View.OnClickListener() {
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

        private TextView tv_name,tv_nick;
        private RoundedImageView iv_header;
        public MyViewHold(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_nick = (TextView) itemView.findViewById(R.id.tv_nick);
            iv_header = (RoundedImageView) itemView.findViewById(R.id.iv_header);

        }
    }
}
