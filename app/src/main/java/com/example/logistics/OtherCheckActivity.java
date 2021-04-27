package com.example.logistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class OtherCheckActivity extends AppCompatActivity {

    ListView list_other_check;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> logList;
    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_check);
        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();
        logList = new ArrayList<>();
        initLogList();
        list_other_check = findViewById(R.id.list_other_check);
        arrayAdapter = new ArrayAdapter(this,R.layout.list_item,logList);
        arrayAdapter.notifyDataSetChanged();
        list_other_check.setAdapter(arrayAdapter);
    }

    private void initLogList() {
        logList.clear();
        Cursor cursor = db.rawQuery("select * from log order by time desc",new String[]{});
        cursor.moveToFirst();
        do {
            String user = cursor.getString(cursor.getColumnIndex("user"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String detail = cursor.getString(cursor.getColumnIndex("detail"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String toAppend = "用户:"+user+"\t\t操作:"+type+"\t\t详情:"+detail+"\t\t时间:"+time;
            logList.add(toAppend);
        }while(cursor.moveToNext());
    }
}