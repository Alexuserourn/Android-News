package com.example.news;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.news.db.HistoryDbHelper;
import com.example.news.entity.HistoryInfo;
import com.google.gson.Gson;

public class NewsDetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webView;
private NewsInfo.ResultBean.DataBean dataBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        //初始化控件
        toolbar=findViewById(R.id.toolbar);
        webView=findViewById(R.id.webView);

//获取传递的数据
       dataBean= (NewsInfo.ResultBean.DataBean) getIntent().getSerializableExtra("dataBean");
//设置数据
        if(null!=dataBean)
        {
            toolbar.setTitle(dataBean.getTitle());
            webView.loadUrl(dataBean.getUrl());
        }
        //返回
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //添加历史记录
        String datajson = new Gson().toJson(dataBean);
        HistoryDbHelper.getInstance(NewsDetailsActivity.this).addHistory(null,dataBean.getUniquekey(),datajson);

    }
}