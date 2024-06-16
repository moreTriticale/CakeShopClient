package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ParticularsActivity extends AppCompatActivity {
    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDetail;
    private Button btnBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particulars);
        findViews();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Cake product = (Cake) intent.getSerializableExtra("product");
        Glide.with(this).load(product.getImg()).into(imgProduct);
        tvName.setText("名称:" + product.getName());
        tvPrice.setText("价格:" + product.getPrice());
        tvDetail.setText("详情" + product.getDetail());
        btnBuy.setOnClickListener(view -> {
            Intent intentOfParticulars = new Intent();
            intentOfParticulars.setClass(ParticularsActivity.this,CarActivity.class);
            intentOfParticulars.putExtra("username",username);
            intentOfParticulars.putExtra("buy", (Parcelable) product);
            startActivity(intentOfParticulars);
        });
    }

    private void findViews() {
        imgProduct = findViewById(R.id.img_product);
        tvName = findViewById(R.id.tv_name);
        tvPrice = findViewById(R.id.tv_price);
        tvDetail = findViewById(R.id.tv_detail);
        btnBuy = findViewById(R.id.btn_buy);
    }
}