package com.example.logistics;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {


    Spinner spinner_manage_select;
    Button button_manage_mod;
    Button button_manage_del;

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> goodList;

    @Override
    protected void onResume() {
        notifyDataChanged(arrayAdapter);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        spinner_manage_select = (Spinner) findViewById(R.id.spinner_manage_select);
        button_manage_mod = (Button) findViewById(R.id.button_manage_mod);
        button_manage_del = findViewById(R.id.button_manage_del);

        goodList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item,goodList);

        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();

        notifyDataChanged(arrayAdapter);

        spinner_manage_select.setAdapter(arrayAdapter);

        button_manage_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageActivity.this,ManageActivity2.class);
                intent.putExtra("name",spinner_manage_select.getSelectedItem().toString());
                startActivity(intent);
            }
        });
        button_manage_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageActivity.this)
                        .setTitle("删除货品信息")
                        .setMessage("确认要删除该货品在数据库中的所有信息吗？该操作执行后不可取消")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.execSQL("delete from goods where name=?",
                                        new String[]{spinner_manage_select.getSelectedItem().toString()});
                                notifyDataChanged(arrayAdapter);
                                Toast.makeText(ManageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();

            }
        });
    }

    private void notifyDataChanged(ArrayAdapter arrayAdapter) {
        Cursor cursor;
        cursor = db.rawQuery("select name from goods",null);
        cursor.moveToFirst();
        goodList.clear();
        do {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            goodList.add(name);
        } while (cursor.moveToNext());
        arrayAdapter.notifyDataSetChanged();
    }
}