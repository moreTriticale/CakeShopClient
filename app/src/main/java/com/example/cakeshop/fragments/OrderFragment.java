package com.example.cakeshop.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cakeshop.Cake;
import com.example.cakeshop.Constance;
import com.example.cakeshop.Order;
import com.example.cakeshop.OrderAdapter;
import com.example.cakeshop.OrderParticularsActivity;
import com.example.cakeshop.ParticularsActivity;
import com.example.cakeshop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderFragment extends Fragment {
    private ListView listView;
    private List<Order> orders = new ArrayList<>();
    private String username;
    private Handler handler;
    private FragmentActivity activity = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_order,null);
        //绑定控件
        listView = view.findViewById(R.id.lv_order);
        Bundle arguments = this.getArguments();
        username = arguments.getString("username");
        activity = this.getActivity();
        //获取数据
        initData();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
                    Bundle bundle = msg.getData();
                    ArrayList<Order> listOrders = bundle.getParcelableArrayList("listOrders");
                    if (listOrders != null){
                        orders = listOrders;
                    }
                    //将数据通过OrderAdapter显示出来
                    OrderAdapter orderAdapter = new OrderAdapter(activity,R.layout.order_item_layout,orders);
                    listView.setAdapter(orderAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            intent.setClass(activity, OrderParticularsActivity.class);
                            intent.putExtra("username",username);
                            Order data = orders.get(position);
                            intent.putExtra("order", (Parcelable) data);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        //实例化适配器
        //绑定适配器
        return view;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/QueryOrders");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    coon.setRequestMethod("POST");
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(username);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();

                    //输入流获取order对象，添加到listOrders里面
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    String result = br.readLine();
                    Gson gson = new Gson();
                    Type listType=new TypeToken<List<Order>>(){}.getType();
                    List<Order> listOrders = gson.fromJson(result,listType);

                    //通过handler和message发送到主线程
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("listOrders", (ArrayList<? extends Parcelable>) listOrders);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
