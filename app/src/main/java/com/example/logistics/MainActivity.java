package com.example.logistics;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this,null,1);
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
                EditText editText_name = (EditText) findViewById(R.id.edit_login_name);
                EditText editText_password = (EditText) findViewById(R.id.edit_login_password);

                String loginName = editText_name.getText().toString();
                String loginPassword = editText_password.getText().toString();

                Cursor cursor1 = db.query("user", null, null, null, null, null, null);
                boolean r = false;
                if (cursor1.moveToFirst()) {
                    do {
                        String name = cursor1.getString(cursor1.getColumnIndex("name"));
                        String password = cursor1.getString(cursor1.getColumnIndex("password"));
                       if(loginName.equals(name)&&loginPassword.equals(password)){
                           Intent intent = new Intent(MainActivity.this,MActivity.class);
                           startActivity(intent);
                           Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                           r = true;
                           break;
                       }else{

                       }
                    } while (cursor1.moveToNext());
                }
                cursor1.close();
                if (r == false){
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }


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
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String password = cursor.getString(cursor.getColumnIndex("password"));
                        sb.append("id：" + name + "\t\tname：" + password + "\n");
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(MainActivity.this,MActivity.class);
                startActivity(intent1);

                break;
            default:

                break;


        }
    }
}