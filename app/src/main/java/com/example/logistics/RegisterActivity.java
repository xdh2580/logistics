package com.example.logistics;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    public SQLiteDatabase db;

    EditText nameEdit;
    EditText passwordEdit1;
    EditText passwordEdit2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();


        nameEdit = (EditText) findViewById(R.id.edit_name);
        passwordEdit1 = (EditText) findViewById(R.id.edit_password1);
        passwordEdit2 = (EditText) findViewById(R.id.edit_password2);

        Button button_next =(Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = nameEdit.getText().toString();
                String password1 = passwordEdit1.getText().toString();
                String password2 = passwordEdit2.getText().toString();

                if(!password1.equals(password2)){
                    Toast.makeText(RegisterActivity.this, "两次密码不一致，请再次确认", Toast.LENGTH_SHORT).show();
                } else if (password1.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put("name", name);
                    values.put("password", password1);
                    db.insert("user", null, values);
                    ChangeLog.register(RegisterActivity.this,name,db,password1);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


}