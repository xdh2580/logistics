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

                final String outName= (String) spinner_out.getSelectedItem();

                final String outNum = edit_out_quantity.getText().toString();
                if(!outNum.isEmpty()) {
                    final int outNumInt = Integer.parseInt(outNum);

                    Cursor cursor = db.rawQuery("select quantity from goods where name=?", new String[]{outName});
                    cursor.moveToFirst();
                    int initNum = cursor.getInt(cursor.getColumnIndex("quantity"));

                    int finalNum = initNum - outNumInt;

                    final String finalNumStr = String.valueOf(finalNum);
                    if(finalNum < 0){
                        Toast.makeText(OutActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OutActivity.this)
                                .setTitle("????????????")
                                .setMessage("\n????????????????????????\n\n???????????????"+spinner_out.getSelectedItem().toString()+
                                        "\n????????????"+Utils.currentLoginUserName+"\n???????????????"+outNum)
                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.execSQL("update goods set quantity=? where name=?", new String[]{finalNumStr, outName});
                                        ChangeLog.out(OutActivity.this,Utils.currentLoginUserName,db,outName+"????????????"+outNum);
                                        Utils.playAnswer(OutActivity.this,"????????????");
                                        Toast.makeText(OutActivity.this, "????????????" , Toast.LENGTH_SHORT).show();
                                        edit_out_quantity.setText("");
                                    }
                                })
                                .setNegativeButton("??????",null);
                        builder.show();
                    }
                }else{
                    Utils.playAnswer(OutActivity.this,"????????????????????????");
                    Toast.makeText(OutActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}