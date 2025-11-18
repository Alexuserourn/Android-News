package com.example.news;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.news.adapter.NewsListAdapter;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TabNewsFragment extends Fragment {

private String url="http://v.juhe.cn/toutiao/index?key=2a0e337db3062922af002bf7a92196cb&type=";
private RecyclerView recyclerView;
private View rootVIew;
    private static final String ARG_PARAM = "title";
    private String title;
    private NewsListAdapter newsListAdapter;

    public TabNewsFragment() {

    }

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                String data = (String) msg.obj;
                NewsInfo newsInfo=new Gson().fromJson(data,NewsInfo.class);
                if(newsInfo!=null && newsInfo.getError_code()==0){
                    if(null!=newsListAdapter){
                        newsListAdapter.setListData(newsInfo.getResult().getData());
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"获取数据失败，请稍后重试",Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public static TabNewsFragment newInstance(String param) {
        TabNewsFragment fragment = new TabNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootVIew=inflater.inflate(R.layout.fragment_tab_news, container, false);
        recyclerView=rootVIew.findViewById(R.id.recyclerView);
        return rootVIew;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取数据
        //初始化适配器
        newsListAdapter=new NewsListAdapter(getActivity());
//设置适配器
        recyclerView.setAdapter(newsListAdapter);
        //点击跳转
        newsListAdapter.setMonItemClickListener(new NewsListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NewsInfo.ResultBean.DataBean dataBean, int position) {
                Intent intent=new Intent(getActivity(),NewsDetailsActivity.class);
                //传递对象
                intent.putExtra("dataBean",dataBean);
                startActivity(intent);
            }
        });
        getHttpData();
    }
    private void getHttpData(){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+title)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
       call.enqueue(new Callback() {
           @Override
           public void onFailure(@NonNull Call call, @NonNull IOException e) {
               Log.d("-------------", "onFailure: "+e.toString());
           }

           @Override
           public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
               String data = response.body().string();
              // Log.d("-------------", "onResponse: "+data);


               Message message = new Message();
               //指定一个标识符
               message.what = 100;
               message.obj = data;
               mHandler.sendMessage(message);

           }
       });
    }
}