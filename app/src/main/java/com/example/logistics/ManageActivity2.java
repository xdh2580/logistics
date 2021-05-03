package com.example.logistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ManageActivity2 extends AppCompatActivity {


    EditText edit_manage2_name;
    EditText edit_manage2_quantity;
    EditText edit_manage2_shelve;
    EditText edit_manage2_layer;
    EditText edit_manage2_unit;
    EditText edit_manage2_min;
    EditText edit_manage2_max;
    Button button_manage2_modify;
    Button Button_manage2_quit;

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    //修改前的数据库中的值
    String name;
    int quantity;
    int shelve;
    int layer;
    String unit;
    int min;
    int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage2);

        Log.d("xdh0503", "onCreate完毕");
        String selectName = getIntent().getStringExtra("name");

        initView();
        Log.d("xdh0503", "init完毕");

        myDBOpenHelper = new MyDBOpenHelper(this, null, 1);
        db = myDBOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from goods where name=?", new String[]{selectName});
        if (cursor.getCount() == 0) {
            Log.d("xdh0503", "cursor没东西");
        }
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex("name"));
            quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
            shelve = cursor.getInt(cursor.getColumnIndex("shelve"));
            layer = cursor.getInt(cursor.getColumnIndex("layer"));
        unit = cursor.getString(cursor.getColumnIndex("unit"));
        min = cursor.getInt(cursor.getColumnIndex("min"));
        max = cursor.getInt(cursor.getColumnIndex("max"));
        Log.d("xdh0503",name+quantity+shelve+layer+unit+min+max);

            edit_manage2_name.setText(name);
            edit_manage2_quantity.setText(String.valueOf(quantity));//不能直接使用int传入，int值会被当成资源id
            edit_manage2_shelve.setText(String.valueOf(shelve));
            edit_manage2_layer.setText(String.valueOf(layer));
        edit_manage2_unit.setText(unit);
        edit_manage2_min.setText(String.valueOf(min));
        edit_manage2_max.setText(String.valueOf(max));

            cursor.close();


        button_manage2_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modName = edit_manage2_name.getText().toString();
                String modQuantity = edit_manage2_quantity.getText().toString();
                String modShelve = edit_manage2_shelve.getText().toString();
                String modLayer = edit_manage2_layer.getText().toString();
                String modUnit = edit_manage2_unit.getText().toString();
                String modMin = edit_manage2_min.getText().toString();
                String modMax = edit_manage2_max.getText().toString();
                boolean notMod = (modName.equals(name) && modQuantity.equals(String.valueOf(quantity))
                        && modShelve.equals(String.valueOf(shelve)) && modLayer.equals(String.valueOf(layer))
                        &&modUnit.equals(unit)&&modMin.equals(String.valueOf(min))&&modMax.equals(String.valueOf(max)));
                if (notMod) {
                    Toast.makeText(ManageActivity2.this, "请修改货品信息", Toast.LENGTH_SHORT).show();
                } else {
                    db.execSQL("update goods set name=?,quantity=?,shelve=?,layer=?,unit=?,min=?,max=?where name=?",
                            new Object[]{modName, Integer.parseInt(modQuantity), Integer.parseInt(modShelve), Integer.parseInt(modLayer)
                                    ,modUnit,Integer.parseInt(modMin),Integer.parseInt(modMax),name});
//
                    //修改信息写入log稍微复杂些，此处根据修改的信息不同写入不同的detail,封装成方法
                    writeModLog(modName, modQuantity, modShelve, modLayer,modUnit,modMin,modMax);
                    Toast.makeText(ManageActivity2.this, "修改成功", Toast.LENGTH_SHORT).show();
                    ManageActivity2.this.finish();
                }
            }

        });
        Button_manage2_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageActivity2.this.finish();
            }
        });
    }

    //修改货品信息时写入log记录
    private void writeModLog(String modName, String modQuantity, String modShelve, String modLayer
            ,String modUnit,String modMin,String modMax) {

        boolean changeName = !modName.equals(name);
        boolean changeQuantity = !String.valueOf(quantity).equals(modQuantity);
        boolean changeShelve = !String.valueOf(shelve).equals(modShelve);
        boolean changeLayer = !String.valueOf(layer).equals(modLayer);
        boolean changeUnit = !modUnit.equals(unit);
        boolean changeMin = !String.valueOf(min).equals(modMin);
        boolean changeMax = !String.valueOf(max).equals(modMax);

        StringBuilder detail = new StringBuilder();
        detail.append(name);
        if(changeName){
            String toAppend = "名称改为:"+modName;
            detail.append(toAppend);
        }
        if(changeQuantity){
            String toAppend = "数量改为:"+modQuantity;
            detail.append(toAppend);
        }
        if(changeShelve){
            String toAppend = "货架改为:"+modShelve;
            detail.append(toAppend);
        }
        if(changeLayer){
            String toAppend = "隔层改为:"+modLayer;
            detail.append(toAppend);
        }
        if(changeUnit){
            String toAppend = "单位改为:"+modUnit;
            detail.append(toAppend);
        }
        if(changeMin){
            String toAppend = "补货提醒数改为:"+modMin;
            detail.append(toAppend);
        }
        if(changeMax){
            String toAppend = "堆积提醒数改为:"+modMax;
            detail.append(toAppend);
        }
        ChangeLog.mod(ManageActivity2.this,Utils.currentLoginUserName,db, detail.toString());
    }
    private void initView() {
        edit_manage2_name = findViewById(R.id.edit_manage2_name);
        edit_manage2_quantity = findViewById(R.id.edit_manage2_quantity);
        edit_manage2_shelve = findViewById(R.id.edit_manage2_shelve);
        edit_manage2_layer = findViewById(R.id.edit_manage2_layer);
        edit_manage2_unit = findViewById(R.id.edit_manage2_unit);
        edit_manage2_min = findViewById(R.id.edit_manage2_min);
        edit_manage2_max = findViewById(R.id.edit_manage2_max);
        button_manage2_modify = findViewById(R.id.button_manage2_modify);
        Button_manage2_quit = findViewById(R.id.button_manage2_quit);
    }
}