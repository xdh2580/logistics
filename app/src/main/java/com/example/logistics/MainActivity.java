package com.example.logistics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editText_name;
    EditText editText_password;

    public SQLiteDatabase db;
    CheckBox rememberPassword;
    CheckBox autoLogin;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        sharedPreferences= getSharedPreferences(",mySP", MODE_PRIVATE);;
        editor = sharedPreferences.edit();//获取编辑器

        editText_name.setText(sharedPreferences.getString("name",""));
        editText_password.setText(sharedPreferences.getString("password",""));
        rememberPassword.setChecked(sharedPreferences.getBoolean("remember",false));
        autoLogin.setChecked(sharedPreferences.getBoolean("auto",false));

        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();
    }

    private void bindView() {
        editText_name = (EditText) findViewById(R.id.edit_login_name);
        editText_password = (EditText) findViewById(R.id.edit_login_password);
        Button button_login = (Button) findViewById(R.id.button_login);
        Button button_register = (Button) findViewById(R.id.button_register);
//        Button button_test = (Button) findViewById(R.id.button_test);
        rememberPassword  =findViewById(R.id.checkBox_rember_password);
        autoLogin = findViewById(R.id.checkBox_auto_login);

        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);

//        button_test.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:


                String loginName = editText_name.getText().toString();
                String loginPassword = editText_password.getText().toString();

                Cursor cursor1 = db.query("user", null, null, null, null, null, null);
                boolean r = false;
                if (cursor1.moveToFirst()) {
                    do {
                        String name = cursor1.getString(cursor1.getColumnIndex("name"));
                        String password = cursor1.getString(cursor1.getColumnIndex("password"));
                       if(loginName.equals(name)&&loginPassword.equals(password)){
                           //退出到指定activity并清空返回栈
                           Intent intent = new Intent(this,MActivity.class)
                                   .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                            Utils.currentLoginUserName = name;//记录当前登录的用户名
                           ChangeLog.logIn(MainActivity.this,name,db);//保存登录日志记录
                           savePreference();
                           Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                           r = true;
                           break;
                       }
                    } while (cursor1.moveToNext());
                }
                cursor1.close();
                if (!r){
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.button_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
//            case R.id.button_test:
//                StringBuilder sb = new StringBuilder();
//                //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，进一步约束
//                //指定查询结果的排序方式
//                Cursor cursor = db.query("user", null, null, null, null, null, null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        String name = cursor.getString(cursor.getColumnIndex("name"));
//                        String password = cursor.getString(cursor.getColumnIndex("password"));
//                        sb.append("id：" + name + "\t\tname：" + password + "\n");
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//                Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
//
//                Intent intent1 = new Intent(MainActivity.this,MActivity.class);
//                startActivity(intent1);
//
//                break;
            default:

                break;


        }
    }
//保存记住的账号密码和勾选的项（记住密码，自动登录）到sharedPreference
    private void savePreference() {
        Log.d("xdh0505","savePreference");
        editor.putString("name",editText_name.getText().toString());
        editor.commit();
        if(rememberPassword.isChecked()){
            editor.putString("password",editText_password.getText().toString());
            editor.putBoolean("remember",true);
            Log.d("xdh0505","success"+editText_password.getText().toString());
        }else{
            editor.putString("password","");
            editor.putBoolean("remember",false);
            Log.d("xdh0505","fail"+editText_password.getText().toString());
        }
        editor.commit();
        editor.putBoolean("auto", autoLogin.isChecked());
        Log.d("xdh0505","checked:"+rememberPassword.isChecked());
        editor.commit();
    }

}