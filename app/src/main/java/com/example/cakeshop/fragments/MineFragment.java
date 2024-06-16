package com.example.cakeshop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cakeshop.Constance;
import com.example.cakeshop.EditInformationActivity;
import com.example.cakeshop.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MineFragment extends Fragment {
    private Button btnEditMyInformation;
    private TextView tvNameEdit, tvUidEdit;
    private ImageButton ivHead;
    private Handler handler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_mine,null);
        findViews(view);
        Bundle arguments = this.getArguments();
        String password = arguments.getString("password");
        String username = arguments.getString("username");
        tvUidEdit.setText(username);
        //通过uid查询名称
        sendDataToQueryNameByUidServlet(username);
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
                    String queryName = (String) msg.obj;
                    Log.i("queryName",queryName);
                    tvNameEdit.setText(queryName);
                }
            }
        };
        Log.i("传递名称0",username);
        btnEditMyInformation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditInformationActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("password",password);
            startActivity(intent);
        });
        return view;
    }

    private void sendDataToQueryNameByUidServlet(String username) {
        final String[] res = {null};
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/QueryName");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    //获取输出流
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(username);
                    Log.i("传递名称",username);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    res[0] = br.readLine();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = res[0];
                    handler.sendMessage(message);
                    br.close();
                    bw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void findViews(View v) {
        btnEditMyInformation = v.findViewById(R.id.btn_edit_my_information);
        tvNameEdit = v.findViewById(R.id.tv_name_edit);
        tvUidEdit = v.findViewById(R.id.tv_uid_edit);
        ivHead = v.findViewById(R.id.iv_head);
    }
}
