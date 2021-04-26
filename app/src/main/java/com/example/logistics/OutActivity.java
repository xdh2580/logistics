package com.example.logistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class OutActivity extends AppCompatActivity {

    Button button_out;
    EditText edit_out_quantity;
    Spinner spinner_out;
    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> goodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        spinner_out = (Spinner) findViewById(R.id.spinner_out);
        button_out = (Button) findViewById(R.id.button_out_storage);
        edit_out_quantity = (EditText) findViewById(R.id.edit_out_quantity);



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


        spinner_out.setAdapter(arrayAdapter);


        button_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String outName= (String) spinner_out.getSelectedItem();

                String outNum = edit_out_quantity.getText().toString();
                if(!outNum.isEmpty()) {
                    int outNumInt = Integer.parseInt(outNum);

                    Cursor cursor = db.rawQuery("select quantity from goods where name=?", new String[]{outName});
                    cursor.moveToFirst();
                    int initNum = cursor.getInt(cursor.getColumnIndex("quantity"));

                    int finalNum = initNum - outNumInt;

                    String finalNumStr = String.valueOf(finalNum);
                    if(finalNum < 0){
                        Toast.makeText(OutActivity.this, "库存不足", Toast.LENGTH_SHORT).show();
                    }else {
                        db.execSQL("update goods set quantity=? where name=?", new String[]{finalNumStr, outName});
                        ChangeLog.out(OutActivity.this,Utils.currentLoginUserName,db,outName+"成功出库"+outNum);
                        Toast.makeText(OutActivity.this, outName + "成功出库" + outNumInt, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OutActivity.this, "出库数量不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}