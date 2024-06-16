package com.example.cakeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {
    private Button btnSign;
    private EditText edtSignUsername, edtSignPwd;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();

        //给注册按钮注册点击事件监听器
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler = new Handler();
                String username = edtSignUsername.getText().toString();
                String password = edtSignPwd.getText().toString();
                sendToSignUpServlet(username,password);
            }
        });


    }

    private void sendToSignUpServlet(String username, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/SignUp");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(coon.getOutputStream(),"utf-8"));
                    bw.write(username + ";" + password);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    String res = br.readLine();
                    //如果注册的用户名存在，则提示已存在
                    if(res.equals("用户名已存在")){
                        Looper.prepare();
                        Toast.makeText(SignUpActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    //注册成功后返回登录界面
                    if(res.equals("注册成功")){
                        Looper.prepare();
                        Toast.makeText(SignUpActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        Looper.loop();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void findViews() {
        btnSign = findViewById(R.id.btn_sign);
        edtSignPwd = findViewById(R.id.edt_sign_pwd);
        edtSignUsername = findViewById(R.id.edt_sign_username);
    }
}