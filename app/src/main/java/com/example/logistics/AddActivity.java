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
    EditText edit_add_good_unit;
    EditText edit_add_good_min;
    EditText edit_add_good_max;

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
        edit_add_good_unit = (EditText) findViewById(R.id.edit_add_good_unit);
        edit_add_good_min = (EditText) findViewById(R.id.edit_add_good_min);
        edit_add_good_max = (EditText) findViewById(R.id.edit_add_good_max);

        button_add_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edit_add_good_name.getText().toString();
                String quantity = edit_add_good_init_quantity.getText().toString();
                String shelve = edit_add_good_shelve.getText().toString();
                String layer = edit_add_good_layer.getText().toString();
                String unit = edit_add_good_unit.getText().toString();
                String min = edit_add_good_min.getText().toString();
                String max = edit_add_good_max.getText().toString();


                if(name.isEmpty()||quantity.isEmpty()||shelve.isEmpty()||layer.isEmpty()||
                unit.isEmpty()||min.isEmpty()||max.isEmpty()){
                    Toast.makeText(AddActivity.this, "请输入完整的货品信息", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        db.execSQL("insert into goods(name,quantity,shelve,layer,unit,min,max)VALUES(?,?,?,?,?,?,?)",
                                new Object[]{name, Integer.parseInt(quantity), Integer.parseInt(shelve), Integer.parseInt(layer),
                                unit,Integer.parseInt(min),Integer.parseInt(max)});
                        ChangeLog.add(AddActivity.this,Utils.currentLoginUserName,db,
                                "新增货品名称:"+name+" 初始数量:"+quantity+" 单位:"+unit+" 货架:"+shelve+" 隔层:"+layer+" 范围:"+min+"~"+max);
                        Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        AddActivity.this.finish();
                    }catch (NumberFormatException ex){
                        Toast.makeText(AddActivity.this, "输入的格式不正确", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}