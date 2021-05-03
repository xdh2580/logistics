package com.example.logistics;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class WarnActivity extends AppCompatActivity {

    ListView list_warn;
    Button button_warn_in;
    Button button_warn_out;
    Button button_warn_quit;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> warnList;

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn);
        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();

        list_warn = findViewById(R.id.list_warn);
        button_warn_in = findViewById(R.id.button_warn_in);
        button_warn_out = findViewById(R.id.button_warn_out);
        button_warn_quit = findViewById(R.id.button_warn_quit);

        warnList =new ArrayList<>();
        updateList();
        arrayAdapter =new ArrayAdapter<String>(this,R.layout.list_item,warnList);
        list_warn.setAdapter(arrayAdapter);
        button_warn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarnActivity.this,InActivity.class);
                startActivity(intent);
            }
        });
        button_warn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarnActivity.this,OutActivity.class);
                startActivity(intent);
            }
        });
        button_warn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarnActivity.this,MActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void updateList() {
        warnList.clear();
        Cursor cursor = db.rawQuery("select * from goods where quantity>max or quantity<min",new String[]{});
        int num = cursor.getCount();
        if(num == 0 ){
            warnList.add("所有货品库存正常，没有需要补货或及时出库的货品");
        }else{
            cursor.moveToFirst();
            warnList.add("以下货品库存异常，请及时调度出库或补货");
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                int min = cursor.getInt(cursor.getColumnIndex("min"));
                int max = cursor.getInt(cursor.getColumnIndex("max"));
                String unit = cursor.getString(cursor.getColumnIndex("unit"));
                Log.d("xdh0503",name+"库存异常");
                warnList.add(name+"当前库存："+quantity+unit+"，正常范围："+min+"~"+max);
            }while (cursor.moveToNext());
        }
    }
}