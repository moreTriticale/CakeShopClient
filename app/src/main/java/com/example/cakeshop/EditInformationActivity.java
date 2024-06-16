package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cakeshop.R;
import com.example.cakeshop.fragments.MineFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditInformationActivity extends AppCompatActivity {
    private EditText edtMyName, edtMyOldPassword, edtMyNewPassword;
    private Button btnOk,btnName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        findViews();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = edtMyOldPassword.getText().toString();
                String newPassword = edtMyNewPassword.getText().toString();
                Intent intent = getIntent();

                String username = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");
                //与登录进来的密码比对
                if(!password.equals(oldPassword)){
                    Toast.makeText(EditInformationActivity.this,"旧密码错误",Toast.LENGTH_SHORT).show();
                } else {
                    //将数据传递给服务端EditInformationServlet
                    sendDataToEditInformationServlet(username,oldPassword,newPassword);
                }

            }
        });
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String username = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");
                String name = edtMyName.getText().toString();
                sendDataToEditNameServlet(name,username,password);
            }
        });
    }

    private void sendDataToEditNameServlet(String name, String username,String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/EditName");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    //获取输出流
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(username + ";" + name);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    String res = br.readLine();
                    if (res.equals("更新名称成功")){
                        Looper.prepare();
                        Toast.makeText(EditInformationActivity.this,"名称修改成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditInformationActivity.this, HomePageActivity.class);
                        intent.putExtra("username",username);
                        intent.putExtra("password",password);
                        startActivity(intent);
                        Looper.loop();
                    }
                    br.close();
                    bw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendDataToEditInformationServlet(String username,  String oldPassword, String newPassword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Constance.HOST + ":8080/AndroidCakeShop_war_exploded/EditInfo");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    coon.setDoInput(true);
                    coon.setDoOutput(true);
                    //获取输出流
                    OutputStream os = coon.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
                    bw.write(username + ";" + newPassword);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    coon.getResponseCode();
                    //获取输入流
                    BufferedReader br = new BufferedReader(new InputStreamReader(coon.getInputStream(),"utf-8"));
                    String res = br.readLine();
                    if(res.equals("更新密码成功")){
                        Looper.prepare();
                        Toast.makeText(EditInformationActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditInformationActivity.this, MainActivity.class);
                        startActivity(intent);
                        Looper.loop();
                    }
                    br.close();
                    bw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private void findViews() {
        edtMyName = findViewById(R.id.edt_my_name);
        edtMyOldPassword = findViewById(R.id.edt_my_old_password);
        edtMyNewPassword = findViewById(R.id.ent_my_new_password);
        btnOk = findViewById(R.id.btn_ok);
        btnName = findViewById(R.id.btn_name);
    }
}