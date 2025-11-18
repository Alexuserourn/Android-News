package com.example.news;

import android.content.Intent;
import android.media.MediaRouter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.news.entity.UserInfo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.news.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   // private String[] titles = {"娱乐", "军事", "教育", "文化", "将康", "财经", "体育", "汽车", "科技"};
    private List<TitleInfo> titles=new ArrayList<>();
private TabLayout tab_layout;
private ViewPager2 viewPager;
private NavigationView nav_view;
private ImageView btn_open;
private DrawerLayout drawer_Layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化title数据
        titles.add(new TitleInfo("推荐","top"));
        titles.add(new TitleInfo("国内","guonei"));
        titles.add(new TitleInfo("国际","guoji"));
        titles.add(new TitleInfo("娱乐","yule"));
        titles.add(new TitleInfo("体育","tiyu"));
        titles.add(new TitleInfo("军事","junshi"));
        titles.add(new TitleInfo("科技","keji"));
        titles.add(new TitleInfo("财经","caijing"));
        titles.add(new TitleInfo("游戏","youxi"));
        titles.add(new TitleInfo("汽车","qiche"));
        titles.add(new TitleInfo("健康","jiankang"));

//初始化控件
        tab_layout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.viewPager);
        nav_view=findViewById(R.id.nav_view);
        btn_open=findViewById(R.id.btn_open);
        drawer_Layout=findViewById(R.id.drawer_layout);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_Layout.open();
            }
        });

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {

                if(Item.getItemId()==R.id.nav_history)
                {
//跳转历史记录
                    Intent intent=new Intent(MainActivity.this,HistoryListActivity.class);
                    startActivity(intent);
                }
                if(Item.getItemId()==R.id.about)
                {
//跳转历史记录
                    Intent intent1=new Intent(MainActivity.this,AboutActivity.class);
                    startActivity(intent1);
                }
                else if (Item.getItemId() == R.id.tuichu) {
                    // 发送一个模拟按下Home键的Intent
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startActivity(startMain);
                }
                return true;
            }
        });

        //设置adapter
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {

             String title=titles.get(position).getPy_title();

           TabNewsFragment tabNewsFragment= TabNewsFragment.newInstance(title);

                return tabNewsFragment;
            }

            @Override
            public int getItemCount() {
                return titles.size();
            }
        });

//点击事件
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中当前页
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//链接
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tab_layout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            tab.setText(titles.get(position).getTitle());
            }
        });
            tabLayoutMediator.attach();


    }


}