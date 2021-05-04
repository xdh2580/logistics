package com.example.logistics;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_query;
    private Button button_in;
    private Button button_out;
    private Button button_add;
    private Button button_manage;
    private Button button_select;
    private Button button_task;
    private Button button_other;
    private Button button_voice;
    private Button button_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        initView();

    }

    private void initView() {
        button_query = findViewById(R.id.button_query);
        button_in = findViewById(R.id.button_in);
        button_out = findViewById(R.id.button_out);
        button_add = findViewById(R.id.button_add);
        button_manage = findViewById(R.id.button_manage);
        button_select = findViewById(R.id.button_select);
        button_task = findViewById(R.id.button_task);
        button_other = findViewById(R.id.button_other);
        button_voice = findViewById(R.id.button_voice);
        button_logout = findViewById(R.id.button_logout);

        button_query.setOnClickListener(this);
        button_in.setOnClickListener(this);
        button_out.setOnClickListener(this);
        button_add.setOnClickListener(this);
        button_manage.setOnClickListener(this);
        button_select.setOnClickListener(this);
        button_task.setOnClickListener(this);
        button_other.setOnClickListener(this);
        button_voice.setOnClickListener(this);
        button_logout.setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_query:
                Intent intent1 = new Intent(this,QueryActivity.class);
                startActivity(intent1);
                break;
            case R.id.button_in:
                Intent intent2 = new Intent(this,InActivity.class);
                startActivity(intent2);
                break;
            case R.id.button_out:
                Intent intent3 = new Intent(this,OutActivity.class);
                startActivity(intent3);
                break;
            case R.id.button_add:
                Intent intent4 = new Intent(this,AddActivity.class);
                startActivity(intent4);
                break;
            case R.id.button_manage:
                Intent intent5 = new Intent(this,ManageActivity.class);
                startActivity(intent5);
                break;
            case R.id.button_select:
                Intent intent6 = new Intent(this,SelectActivity.class);
                startActivity(intent6);
                break;
            case R.id.button_task:
                Intent intent7 = new Intent(this,TaskActivity.class);
                startActivity(intent7);
                break;
            case R.id.button_other:
                Intent intent8 = new Intent(this,OtherActivity.class);
                startActivity(intent8);
                break;
            case R.id.button_voice:
                Intent intent9 = new Intent(this,VoiceActivity.class);
                startActivity(intent9);
                break;
            case R.id.button_logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("退出登录")
                        .setMessage("\n登出当前账户？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent10 = new Intent(MActivity.this,MainActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent10);
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();
                break;
            default:
                break;

        }
    }
}