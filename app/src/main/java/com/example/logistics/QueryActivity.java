package com.example.logistics;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener{


    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    Button button_start_query;
    ListView list_result;
    EditText edit_query_name;
    EditText edit_query_shelve;

    private static ArrayAdapter<String> adapter;
    ArrayList<String> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);


        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db =myDBOpenHelper.getWritableDatabase();

        button_start_query = (Button) findViewById(R.id.button_start_query);
        list_result = (ListView) findViewById(R.id.list_result);
        edit_query_name = (EditText) findViewById(R.id.edit_query_name);
        edit_query_shelve = (EditText) findViewById(R.id.edit_qurey_shelve);

        button_start_query.setOnClickListener(this);

        resultList = new ArrayList<>();
        resultList.add("查询结果：");
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,resultList);
        list_result.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start_query:
                String queryName = edit_query_name.getText().toString();
                String queryShelve = edit_query_shelve.getText().toString();

                if(queryName.equals("*")||queryShelve.equals("*")){
                    Cursor cursor0 = db.rawQuery("select * from goods", new String[]{});
                    showResult(cursor0);
//                    Log.d("xdh0420",cursor0.getString());
                }

                if(!queryName.isEmpty()&&queryShelve.isEmpty()) {
                    Cursor cursor1 = db.rawQuery("select * from goods where name = ? ", new String[]{queryName});
                    showResult(cursor1);
                }else if(queryName.isEmpty()&&!queryShelve.isEmpty()){
                    Cursor cursor2 = db.rawQuery("select * from goods where shelve = ? ", new String[]{queryShelve});
                    showResult(cursor2);
                }else if(!queryName.isEmpty()&&!queryShelve.isEmpty()){
                    Toast.makeText(this, "只能输入一个查询条件", Toast.LENGTH_SHORT).show();
                }else{
                Toast.makeText(this, "请输入查询限制", Toast.LENGTH_SHORT).show();
            }
                break;
            }
    }

    private void showResult(Cursor cursor1) {

        //先清除resultList的数据源，防止重复堆叠
        resultList.clear();
        resultList.add("查询结果：");

        StringBuilder total = new StringBuilder();
        //    Cursor cursor1 = db.query("goods", null, null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            do {
                String name = cursor1.getString(cursor1.getColumnIndex("name"));
                int quantity = cursor1.getInt(cursor1.getColumnIndex("quantity"));
                int shelve = cursor1.getInt(cursor1.getColumnIndex("shelve"));
                int layer = cursor1.getInt(cursor1.getColumnIndex("layer"));
                String a = "名称:" + name + "\t\t数量:"+quantity+"\t\t货架:" + shelve + "\t\t隔层:" + layer + "\n";
                resultList.add(a);
                total.append(a);
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        adapter.notifyDataSetChanged();

        String all = total.toString();
        if(!all.isEmpty()) {
            Toast.makeText(this, "查询成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "没有符合条件的货品信息", Toast.LENGTH_SHORT).show();
        }
    }
}