package com.example.logistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class OtherCheckActivity extends AppCompatActivity {

    Spinner spinner_check_type;
    ListView list_other_check;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String>arrayAdapterOfType;
    ArrayList<String> logList;
    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_check);
        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();
        spinner_check_type = findViewById(R.id.spinner_check_type);
        arrayAdapterOfType = new ArrayAdapter<String>(this,R.layout.list_item,
                new String[]{"所有日志","登录日志","注册日志","入库日志","出库日志","新增货品日志","删除货品日志","修改货品日志","拣货完成日志"});
        arrayAdapterOfType.notifyDataSetChanged();
        spinner_check_type.setAdapter(arrayAdapterOfType);
        logList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,R.layout.list_item,logList);
        updateLogList("all");
        list_other_check = findViewById(R.id.list_other_check);
        arrayAdapter.notifyDataSetChanged();
        list_other_check.setAdapter(arrayAdapter);
        spinner_check_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        updateLogList("all");
                        break;
                    case 1:
                        updateLogList("login");
                        break;
                    case 2:
                        updateLogList("register");
                        break;
                    case 3:
                        updateLogList("goodIn");
                        break;
                    case 4:
                        updateLogList("goodOut");
                        break;
                    case 5:
                        updateLogList("addGood");
                        break;
                    case 6:
                        updateLogList("delGood");
                        break;
                    case 7:
                        updateLogList("modGood");
                        break;
                    case 8:
                        updateLogList("selGood");
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateLogList(String typeOfCheck) {
        logList.clear();
        Cursor cursor;
        if(typeOfCheck.equals("all")){
            cursor = db.rawQuery("select * from log order by time desc", new String[]{});
        }else {
            cursor = db.rawQuery("select * from log where type=? order by time desc", new String[]{typeOfCheck});
        }
        if(cursor.getCount()==0){
            logList.add("没有查询到该类型的日志记录");
        }else {
            cursor.moveToFirst();
            do {
                String user = cursor.getString(cursor.getColumnIndex("user"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String toAppend = "用户:" + user + "\t\t操作:" + type + "\t\t详情:" + detail + "\t\t时间:" + time;
                logList.add(toAppend);
            } while (cursor.moveToNext());
        }
        arrayAdapter.notifyDataSetChanged();
    }
}