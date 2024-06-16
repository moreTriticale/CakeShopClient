package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnSignIn;
    EditText edtUsername, edtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        //给登录按钮注册点击事件监听器
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPwd.getText().toString();
                sendDataToLoginServlet(username,password);
            }
        });
        //给注册按钮注册点击事件监听器
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册界面
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendDataToLoginServlet(String username, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取URL连接
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/login");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    //获取输出流
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(username + ";" + password);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                    //输入流获取服务器响应
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    String res = br.readLine();
                    //账号密码正确则跳转
                    if(res.equals("登录成功")){
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,HomePageActivity.class);
                        intent.putExtra("username",username);
                        intent.putExtra("password",password);
                        startActivity(intent);
                    //错误则提示
                    }else {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this,"用户名不存在或密码错误",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    br.close();
                    bw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void findViews() {
        btnLogin = findViewById(R.id.btn_login);
        btnSignIn = findViewById(R.id.btn_sign_in);
        edtUsername = findViewById(R.id.edt_username);
        edtPwd = findViewById(R.id.edt_pwd);
    }
}
