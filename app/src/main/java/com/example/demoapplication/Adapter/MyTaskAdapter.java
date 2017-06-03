package com.example.demoapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demoapplication.ModelClass.MyTaskModel;
import com.example.demoapplication.R;

import java.util.List;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.MyViewHolder> {
    Context mContext;
    private List<MyTaskModel> mMyTaskModelList;

    public MyTaskAdapter(List<MyTaskModel> myTaskModel, Context context) {
        mMyTaskModelList = myTaskModel;
        mContext = context;
    }

    @Override
    public MyTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_list, parent, false);
        MyTaskAdapter.MyViewHolder vh = new MyTaskAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyTaskAdapter.MyViewHolder holder, final int position) {

        MyTaskModel itemPhoto = mMyTaskModelList.get(position);
        holder.title_tv.setText(mMyTaskModelList.get(position).getTitleName());
        holder.description_title.setText(itemPhoto.getsDescription());
        holder.img.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return mMyTaskModelList.size();
    }

    public void removeItem(int position) {
        mMyTaskModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mMyTaskModelList.size());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView title_tv, description_title;

        public MyViewHolder(View v) {
            super(v);

            title_tv = (TextView) v.findViewById(R.id.tv_title);
            description_title = (TextView) v.findViewById(R.id.tv_description);
            img = (ImageView) v.findViewById(R.id.imageView);

        }

    }
}