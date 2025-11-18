package com.example.news;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.adapter.NewsListAdapter;
import com.example.news.db.HistoryDbHelper;
import com.example.news.entity.HistoryInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HistoryListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;
private List<NewsInfo.ResultBean.DataBean> mDataBeanList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history_list);
        recyclerView=findViewById(R.id.recyclerView);

        newsListAdapter=new NewsListAdapter(this);
        recyclerView.setAdapter(newsListAdapter);
        List<HistoryInfo> historyInfoList = HistoryDbHelper.getInstance(HistoryListActivity.this).queryHistoryListData(null);
        Gson gson=new Gson();
         for(int i=0;i<historyInfoList.size();i++)
       {
    mDataBeanList.add(gson.fromJson(historyInfoList.get(i).getNew_json(),NewsInfo.ResultBean.DataBean.class));

       }

         newsListAdapter.setListData(mDataBeanList);
newsListAdapter.setMonItemClickListener(new NewsListAdapter.onItemClickListener() {
    @Override
    public void onItemClick(NewsInfo.ResultBean.DataBean dataBean, int position) {
        Intent intent=new Intent(HistoryListActivity.this,NewsDetailsActivity.class);
        //传递对象
        intent.putExtra("dataBean",dataBean);
        startActivity(intent);

    }
});

        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
    @Override
      public void onClick(View v) {


     finish();
    }
});
    }
}