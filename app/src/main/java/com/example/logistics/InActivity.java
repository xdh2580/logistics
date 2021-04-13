package com.example.logistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class InActivity extends AppCompatActivity {

    Button button_in;
    EditText edit_quantity;
    Spinner spinner_in;

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> goodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);

        spinner_in = (Spinner) findViewById(R.id.spinner_in);
        button_in = (Button) findViewById(R.id.button_in_storage);
        edit_quantity = (EditText) findViewById(R.id.edit_in_quantity);



        goodList = new ArrayList<String>();
        arrayAdapter =new ArrayAdapter<String>(this,R.layout.list_item,goodList);
        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select name from goods",null);
        cursor.moveToFirst();
        do {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            goodList.add(name);
        } while (cursor.moveToNext());
        arrayAdapter.notifyDataSetChanged();


        spinner_in.setAdapter(arrayAdapter);


        button_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inName= (String) spinner_in.getSelectedItem();

                String addNum = edit_quantity.getText().toString();
                if(!addNum.isEmpty()) {
                    int addNumInt = Integer.parseInt(addNum);

                    Cursor cursor = db.rawQuery("select quantity from goods where name=?", new String[]{inName});
                    cursor.moveToFirst();
                    int initNum = cursor.getInt(cursor.getColumnIndex("quantity"));

                    int finalNum = initNum + addNumInt;
                    String finalNumStr = String.valueOf(finalNum);

                    db.execSQL("update goods set quantity=? where name=?", new String[]{finalNumStr, inName});


                    Toast.makeText(InActivity.this, inName + "成功入库" + addNumInt, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(InActivity.this, "入库数量不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}