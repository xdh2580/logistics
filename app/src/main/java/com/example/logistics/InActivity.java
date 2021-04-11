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
                String str= (String) spinner_in.getSelectedItem();
                Toast.makeText(InActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}