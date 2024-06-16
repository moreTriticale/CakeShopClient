package com.example.cakeshop.fragments;

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
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cakeshop.Cake;
import com.example.cakeshop.Constance;
import com.example.cakeshop.EditInformationActivity;
import com.example.cakeshop.ParticularsActivity;
import com.example.cakeshop.ProductAdapter;
import com.example.cakeshop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {
    private GridView gvRecommend;
    private ArrayList<Cake> cakes = new ArrayList<>();
    private Handler handler;
    private String username;
    private FragmentActivity activity = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_product_list,null);
        gvRecommend = view.findViewById(R.id.gv_recommend);
        Bundle arguments = this.getArguments();
        username = arguments.getString("username");
        //查询数据库cake_tb表，返回一个List集合
        initDatas();
        activity = this.getActivity();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
                    Bundle data = msg.getData();
                    ArrayList<Cake> listCakes = data.getParcelableArrayList("listCakes");
                    if (listCakes != null){
                        cakes =  listCakes;
                    }

                    //将数据通过ProductAdapter显示出来
                    ProductAdapter productAdapter = new ProductAdapter(activity, R.layout.item_main, cakes);
                    gvRecommend.setAdapter(productAdapter);
                    gvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //点击商品跳转到详情页
                            Intent intent = new Intent();
                            intent.setClass(activity, ParticularsActivity.class);
                            intent.putExtra("username",username);
                            Cake data = cakes.get(position);
                            intent.putExtra("product", (Parcelable) data);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        return view;
    }
    public List<Cake> getDataList(){
        return cakes;
    }
    private void initDatas() {
        List<Cake> listCakes;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/QueryCake");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    coon.setRequestMethod("POST");
                    coon.getResponseCode();

                    //输入流获取cake对象，添加到listCakes里面
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    String result = br.readLine();
                    Gson gson = new Gson();
                    Type listType=new TypeToken<List<Cake>>(){}.getType();
                    List<Cake> listCakes = gson.fromJson(result,listType);
                    //通过handle和message把listCakes发送到主线程
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("listCakes", (ArrayList<? extends Parcelable>) listCakes);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
