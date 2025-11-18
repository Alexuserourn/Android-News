package com.example.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news.NewsInfo;
import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyHolder> {
    private List<NewsInfo.ResultBean.DataBean> mDataBeanList = new ArrayList<>();
    private Context mContext;


    public NewsListAdapter(Context context) {
        this.mContext = context;
    }


    /**
     * 为adapter 设置数据源
     */
    public void setListData(List<NewsInfo.ResultBean.DataBean> list) {
        this.mDataBeanList = list;
        //一定要调用
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //加载布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        NewsInfo.ResultBean.DataBean dataBean = mDataBeanList.get(position);

        //设置数据
        holder.author_name.setText(dataBean.getAuthor_name());
        holder.title.setText(dataBean.getTitle());
        holder.date.setText(dataBean.getDate());
        //加载图片
        Glide.with(mContext).load(dataBean.getThumbnail_pic_s()).error(R.drawable.img_error).into(holder.thumbnail_pic_s);
        //点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=monItemClickListener){
                    monItemClickListener.onItemClick(dataBean,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataBeanList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail_pic_s;
        TextView title;
        TextView author_name;
        TextView date;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail_pic_s = itemView.findViewById(R.id.thumbnail_pic_s);
            title = itemView.findViewById(R.id.title);
            author_name = itemView.findViewById(R.id.author_name);
            date = itemView.findViewById(R.id.date);

        }
    }
    private onItemClickListener monItemClickListener;



    public void setMonItemClickListener(onItemClickListener monItemClickListener) {
        this.monItemClickListener = monItemClickListener;
    }

    public interface onItemClickListener{
        void onItemClick(NewsInfo.ResultBean.DataBean dataBean,int position);
    }
}
