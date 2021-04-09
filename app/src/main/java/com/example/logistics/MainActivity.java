package com.example.logistics;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this,"mydb",null,1);
        db = myDBOpenHelper.getWritableDatabase();
    }

    private void bindView() {
        Button button_login = (Button) findViewById(R.id.button_login);
        Button button_register = (Button) findViewById(R.id.button_register);
        Button button_test = (Button) findViewById(R.id.button_test);


        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);
        button_test.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:

                break;
            case R.id.button_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button_test:
                StringBuilder sb = new StringBuilder();
                //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，进一步约束
                //指定查询结果的排序方式
                Cursor cursor = db.query("user", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        int pid = cursor.getInt(cursor.getColumnIndex("user_id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        sb.append("id：" + pid + "\t\tname：" + name + "\n");
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
                break;
            default:

                break;


        }
    }
}