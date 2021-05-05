package com.example.logistics;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
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

                final String inName= (String) spinner_in.getSelectedItem();

                final String addNum = edit_quantity.getText().toString();
                if(!addNum.isEmpty()) {
                    final int addNumInt = Integer.parseInt(addNum);

                    Cursor cursor = db.rawQuery("select quantity from goods where name=?", new String[]{inName});
                    cursor.moveToFirst();
                    int initNum = cursor.getInt(cursor.getColumnIndex("quantity"));

                    int finalNum = initNum + addNumInt;
                    final String finalNumStr = String.valueOf(finalNum);

                    AlertDialog.Builder builder = new AlertDialog.Builder(InActivity.this)
                            .setTitle("货品入库")
                            .setMessage("\n请您确认入库信息\n\n入库货品："+spinner_in.getSelectedItem().toString()+
                                    "\n操作人："+Utils.currentLoginUserName+"\n入库数量："+addNum)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    db.execSQL("update goods set quantity=? where name=?", new String[]{finalNumStr, inName});

                                    ChangeLog.in(InActivity.this,Utils.currentLoginUserName,db,inName+"成功入库"+addNum);
                                    Toast.makeText(InActivity.this, "入库成功", Toast.LENGTH_SHORT).show();
                                    edit_quantity.setText("");
                                }
                            })
                            .setNegativeButton("取消",null);
                    builder.show();

                }else{
                    Utils.playAnswer(InActivity.this,"入库数量不能为空");
                    Toast.makeText(InActivity.this, "入库数量不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}