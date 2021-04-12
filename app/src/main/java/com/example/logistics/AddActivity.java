package com.example.logistics;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AddActivity extends AppCompatActivity {

    Button button_add_good;
    EditText edit_add_good_name;
    EditText edit_add_good_init_quantity;
    EditText edit_add_good_shelve;
    EditText edit_add_good_layer;

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();

        edit_add_good_name = (EditText) findViewById(R.id.edit_add_good_name);
        edit_add_good_init_quantity = (EditText) findViewById(R.id.edit_add_good_init_quantity);
        edit_add_good_shelve = (EditText) findViewById(R.id.edit_add_good_shelve);
        edit_add_good_layer = (EditText) findViewById(R.id.edit_add_good_layer);
        button_add_good = (Button) findViewById(R.id.button_add_good);

        button_add_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edit_add_good_name.getText().toString();
                String quantity = edit_add_good_init_quantity.getText().toString();
                String shelve = edit_add_good_shelve.getText().toString();
                String layer = edit_add_good_layer.getText().toString();

                db.execSQL("insert into goods(name,quantity,shelve,layer)VALUES(?,?,?,?)",
                        new Object[]{name,Integer.parseInt(quantity),Integer.parseInt(shelve),Integer.parseInt(layer)});
                Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}