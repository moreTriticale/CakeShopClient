package com.example.cakeshop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cakeshop.fragments.OrderFragment;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CarProductAdapter extends BaseAdapter {
    private Context context;
    private List<CarItem> list;
    private int layout;

    public CarProductAdapter(Context context, List<CarItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_car,null);
        TextView tvNameCar = layout.findViewById(R.id.tv_name_car);
        TextView tvPriceCar = layout.findViewById(R.id.tv_price_car);
        TextView tvSumCar = layout.findViewById(R.id.tv_sum_car);
        CarItem product = list.get(position);
        tvNameCar.setText(product.getName());

        Button btnAddCar = layout.findViewById(R.id.btn_add_car);
        Button btnReduceCar = layout.findViewById(R.id.btn_reduce_car);
        Button btnDeleteCar = layout.findViewById(R.id.btn_delete_car);
        Button btnBuyCar = layout.findViewById(R.id.btn_buy_car);
        Button btnSaveCar = layout.findViewById(R.id.btn_save_car);

        btnAddCar.setOnClickListener(view -> {
            int count = product.getCount();
            count++;
            product.setCount(count);
            tvPriceCar.setText("总价格:" + (Integer.parseInt(product.getPrice()) * product.getCount()) + "");
            tvSumCar.setText("总数量:" + product.getCount() + "");
        });
        btnReduceCar.setOnClickListener(view -> {
            int count = product.getCount();
            if(count > 0){
                count--;
            }
            product.setCount(count);
            tvPriceCar.setText("总价格:" + (Integer.parseInt(product.getPrice()) * product.getCount()) + "");
            tvSumCar.setText("总数量:" + product.getCount() + "");
        });

        //先把数据库数据删除，再把内存中集合元素删除，保证内存清空后依然删除
        btnDeleteCar.setOnClickListener(view -> {
            deleteThisCarItem(product);
            product.setCount(0);
            list.remove(position);
            notifyDataSetChanged();
        });
        //点击购买按钮，将数据保存到订单表，并删除购物车表的该数据
        btnBuyCar.setOnClickListener(view -> {
            SaveIntoOrderTable(product);
            deleteThisCarItem(product);
//            product.setCount(0);
            list.remove(position);
            notifyDataSetChanged();
//            Intent intent = new Intent(context, HomePageActivity.class);
//            //将账号密码传递过去，保证其他功能正常使用
//            intent.putExtra("username",product.getUid());
//            QueryPasswordByUid(product.getUid());
//            context.startActivity(intent);
        });
        //修改数据库count字段，并用Toast提示保存成功
        btnSaveCar.setOnClickListener(view -> {
            SaveCarItem(product);
            Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
        });
        tvPriceCar.setText("总价格:" + (Integer.parseInt(product.getPrice()) * product.getCount()) + "");
        tvSumCar.setText("总数量:" + product.getCount() + "");
            return layout;
    }

    private void SaveIntoOrderTable(CarItem product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/SaveOrder");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    //获取输出流
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    Date date = new Date();
                    SimpleDateFormat s = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
                    String nowTime = s.format(date);
                    Log.i("nowTime",nowTime);
                    bw.write(product.getCakeId() + ";" + product.getUid() + ";" + product.getCount() + ";" +
                            product.getDetail() + ";" + product.getStatus() + ";" + product.getName() + ";" + product.getPrice()
                    + ";" + product.getProductDesc() + ";" + nowTime);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void SaveCarItem(CarItem product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/SaveCarItem");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    //获取输出流
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(product.getCakeId() + ";" + product.getUid() + ";" + product.getCount());
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void deleteThisCarItem(CarItem product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/DeleteCarItem");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    //获取输出流
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(product.getCakeId() + ";" + product.getUid());
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

