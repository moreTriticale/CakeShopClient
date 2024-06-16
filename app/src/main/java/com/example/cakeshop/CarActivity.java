package com.example.cakeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.MeshSpecification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CarActivity extends AppCompatActivity {
    static ArrayList<Cake> carItems = new ArrayList<>();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        ListView productList = findViewById(R.id.product_list);
        //放入数据
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Cake product = (Cake) intent.getSerializableExtra("buy");
        //判断购物车是否已经存在该商品
        isHavingThisCak(product.getId(),username);
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i("购物车查询状态",msg.arg1 + "");
                //购物车存在该蛋糕
                if (msg.what == 1){
                    if (msg.arg1 == 1){
                        //查询数据库所有数据
                        queryAllDataFromCarDataBase(productList,username);
                    }
                    //购物车不存在该蛋糕
                    if (msg.arg1 == 0){
                        addThisCakeToCar(product,username);
                    }
                }

            }
        };
    }

    private void queryAllDataFromCarDataBase(ListView productList, String username) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/QueryCarAllData");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    coon.setRequestMethod("POST");
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(username);
                    Log.i("获取所有购物车的uid", username);
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();

                    //输入流获取cake对象，添加到listCakes里面
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    String result = br.readLine();
                    Gson gson = new Gson();
                    Type listType=new TypeToken<List<CarItem>>(){}.getType();
                    List<CarItem> listCarItem = gson.fromJson(result,listType);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CarProductAdapter carProductAdapter = new CarProductAdapter(CarActivity.this,listCarItem);
                            productList.setAdapter(carProductAdapter);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addThisCakeToCar(Cake product, String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/InsertIntoCar");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    CarItem carItem = new CarItem(product.getId() + "",username,product.getDesc(),product.getPrice(),product.getDetail(),1,product.getName(),1);
                    Gson gson = new Gson();
                    String js = gson.toJson(carItem);
                    bw.write(js);
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                    //插入数据库完成后，arg1则更新为1
                    Message message = new Message();
                    message.what = 1;
                    message.arg1 = 1;
                    handler.sendMessage(message);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean isHavingThisCak(int id, String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/QueryCar");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    //获取输出流
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(id + "" + ";" + username);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    String result = br.readLine();
                    Log.i("购物车查询状态",result);
                    Message message = new Message();
                    message.what = 1;
                    if (result.equals("该蛋糕已存在于购物车")){
                        message.arg1 = 1;
                    }else {
                        message.arg1 = 0;
                    }
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        return false;
    }
}