package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

public class OrderParticularsActivity extends AppCompatActivity {
    private TextView tvOrderPariticularsName, tvOrderPariticularsPrice, tvOrderPariticularsSumPrice, tvOrderPariticularsDetail,tvOrderPariticularsTime,tvOrderPariticularsCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_particulars);
        findViews();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Order order = (Order) intent.getSerializableExtra("order");
        tvOrderPariticularsName.setText("名称:" + order.getName());
        tvOrderPariticularsDetail.setText("详情" + order.getDetail());
        tvOrderPariticularsPrice.setText("单价" + order.getPrice());
        tvOrderPariticularsSumPrice.setText("总价" + (order.getCount() * Integer.parseInt(order.getPrice())) + "");
        tvOrderPariticularsTime.setText("下单时间" + order.getCreateTime());
        tvOrderPariticularsCount.setText("数量:" + order.getCount());
    }

    private void findViews() {
        tvOrderPariticularsName = findViewById(R.id.tv_order_particulars_name);
        tvOrderPariticularsDetail = findViewById(R.id.tv_order_particulars_detail);
        tvOrderPariticularsPrice = findViewById(R.id.tv_order_particulars_price);
        tvOrderPariticularsSumPrice = findViewById(R.id.tv_order_particulars_sum_price);
        tvOrderPariticularsTime = findViewById(R.id.tv_order_particulars_time);
        tvOrderPariticularsCount = findViewById(R.id.tv_order_particulars_count);
    }
}