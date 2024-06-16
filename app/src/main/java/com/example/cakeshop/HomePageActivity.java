package com.example.cakeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cakeshop.fragments.MineFragment;
import com.example.cakeshop.fragments.OrderFragment;
import com.example.cakeshop.fragments.ProductListFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private TabLayout tabChange;
    private ViewPager2 vp2Main;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        findViews();
        //给fragment容器初始化数据
        initData();
        tabChange.bringToFront();
        MyPageAdapter myPageAdapter = new MyPageAdapter(fragments,this);
        vp2Main.setAdapter(myPageAdapter);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        //将账号密码传给fragment
        Bundle bundle = new Bundle();
        bundle.putString("username",username);
        bundle.putString("password",password);
        fragments.forEach(element -> {
            element.setArguments(bundle);
        });
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabChange, vp2Main, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("商品列表");
                        break;
                    case 1:
                        tab.setText("订单");
                        break;
                    case 2:
                        tab.setText("我的");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }

    private void initData() {
        fragments.add(new ProductListFragment());
        fragments.add(new OrderFragment());
        fragments.add(new MineFragment());
    }

    private void findViews() {
        tabChange = findViewById(R.id.tab_change);
        vp2Main = findViewById(R.id.vp2_main);
    }
}