package com.example.logistics;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.view.menu.ActionMenuItemView;
import com.iflytek.cloud.SpeechUtility;

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
    public boolean onPrepareOptionsMenu(Menu menu) {

        // Set 'delete' menu item state depending on count
        MenuItem deleteItem = menu.findItem(R.id.cur);
        deleteItem.setTitle("当前登录："+Utils.currentLoginUserName.toLowerCase());

//        ActionMenuItemView m = findViewById(R.id.cur);
//        m.setTitle(Utils.currentLoginUserName);

        return super.onPrepareOptionsMenu(menu);
    }

    //此方法的作用是创建一个选项菜单，我们要重写这个方法
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.title_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //在点击这个菜单的时候，会做对应的事，类似于侦听事件，这里我们也要重写它
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //这里是一个switch语句,主要通过menu文件中的id值来确定点了哪个菜单，然后做对应的操作，这里的menu是指你加载的那个菜单文件
        switch (item.getItemId()) {
            case R.id.cur:
                Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.item2:
//
//                Toast.makeText(this, "点", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        initView();
        SpeechUtility b = SpeechUtility.createUtility(this, "appid=47149c79");
        Log.d("xdh","utility创建完了");
        if(b==null){
            Log.d("xdh","是空的");
        }else{
            Log.d("xdh","没毛病");
        }

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