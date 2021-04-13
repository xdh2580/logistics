package com.example.logistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SelectActivity extends AppCompatActivity {

    ListView listView_select_order;
    OrderAdapter myOrderAdapter;

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();

        listView_select_order = findViewById(R.id.list_select_order);

        Cursor cursor = db.rawQuery("select * from orders",null);

        myOrderAdapter = new OrderAdapter(cursor,this);

        listView_select_order.setAdapter(myOrderAdapter);

    }
}