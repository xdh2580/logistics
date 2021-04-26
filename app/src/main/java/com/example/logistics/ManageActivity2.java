package com.example.logistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    Button button_manage2_modify;
    Button Button_manage2_quit;

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    //修改前的数据库中的值
    String name;
    int quantity;
    int shelve;
    int layer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage2);

        String selectName = getIntent().getStringExtra("name");

        initView();

        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from goods where name=?",new String[]{selectName});
        cursor.moveToFirst();
        name =cursor.getString(cursor.getColumnIndex("name"));
        quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        shelve = cursor.getInt(cursor.getColumnIndex("shelve"));
        layer = cursor.getInt(cursor.getColumnIndex("layer"));

        edit_manage2_name.setText(name);
        edit_manage2_quantity.setText(String.valueOf(quantity));//不能直接使用int传入，int值会被当成资源id
        edit_manage2_shelve.setText(String.valueOf(shelve));
        edit_manage2_layer.setText(String.valueOf(layer));
        cursor.close();

        button_manage2_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modName = edit_manage2_name.getText().toString();
                String modQuantity = edit_manage2_quantity.getText().toString();
                String modShelve = edit_manage2_shelve.getText().toString();
                String modLayer = edit_manage2_layer.getText().toString();
                boolean notMod = (modName.equals(name)&&modQuantity.equals(String.valueOf(quantity))
                        &&modShelve.equals(String.valueOf(shelve))&&modLayer.equals(String.valueOf(layer)));
                if(notMod){
                    Toast.makeText(ManageActivity2.this, "请修改货品信息", Toast.LENGTH_SHORT).show();
                }else{
                    db.execSQL("update goods set name=?,quantity=?,shelve=?,layer=? where name=?",
                            new Object[]{modName,Integer.parseInt(modQuantity),Integer.parseInt(modShelve),
                                    Integer.parseInt(modLayer),name});
                    //修改信息写入log稍微复杂些，此处根据修改的信息不同写入不同的detail,封装成方法
                    writeModLog(modName,modQuantity,modShelve,modLayer);
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
    private void writeModLog(String modName, String modQuantity, String modShelve, String modLayer) {
        boolean changeName = !modName.equals(name);
        boolean changeQuantity = !String.valueOf(quantity).equals(modQuantity);
        boolean changeShelve = !String.valueOf(shelve).equals(modShelve);
        boolean changeLayer = !String.valueOf(layer).equals(modLayer);

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
        ChangeLog.mod(ManageActivity2.this,Utils.currentLoginUserName,db, detail.toString());
//        if(changeName&&!changeQuantity&&!changeShelve&&!changeLayer){
//            ChangeLog.mod(ManageActivity2.this, Utils.currentLoginUserName, db,
//                    "修改货品" + name + "的信息为名称:" + modName);
//        }else if(!changeName&&changeQuantity&&!changeShelve&&!changeLayer){
//            ChangeLog.mod(ManageActivity2.this, Utils.currentLoginUserName, db,
//                    "修改货品" + name + "的信息为名称:" + modName);
//        }else if(!changeName&&!changeQuantity&&changeShelve&&!changeLayer){
//            ChangeLog.mod(ManageActivity2.this, Utils.currentLoginUserName, db,
//                    "修改货品" + name + "的信息为名称:" + modName);
//        }else if(!changeName&&!changeQuantity&&!changeShelve&&changeLayer){
//            ChangeLog.mod(ManageActivity2.this, Utils.currentLoginUserName, db,
//                    "修改货品" + name + "的信息为名称:" + modName);
//        }else if(changeName&&changeQuantity&&!changeShelve&&!changeLayer){
//            ChangeLog.mod(ManageActivity2.this, Utils.currentLoginUserName, db,
//                    "修改货品" + name + "的信息为名称:" + modName);
//        }else if(!changeName&&changeQuantity&&changeShelve&&!changeLayer){
//            ChangeLog.mod(ManageActivity2.this, Utils.currentLoginUserName, db,
//                    "修改货品" + name + "的信息为名称:" + modName);
//        }else if(!changeName&&!changeQuantity&&changeShelve&&changeLayer){
//            ChangeLog.mod(ManageActivity2.this, Utils.currentLoginUserName, db,
//                    "修改货品" + name + "的信息为名称:" + modName);
//        }
    }
    private void initView() {
        edit_manage2_name = findViewById(R.id.edit_manage2_name);
        edit_manage2_quantity = findViewById(R.id.edit_manage2_quantity);
        edit_manage2_shelve = findViewById(R.id.edit_manage2_shelve);
        edit_manage2_layer = findViewById(R.id.edit_manage2_layer);
        button_manage2_modify = findViewById(R.id.button_manage2_modify);
        Button_manage2_quit = findViewById(R.id.button_manage2_quit);
    }
}