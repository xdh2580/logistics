package com.example.logistics;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener{

    Button button_task_warn;
    Button button_task_out;
    Button button_task_random_select;
    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;
    String content;//模拟的拣货订单内容，格式如(毛巾,1)(饼干,3)(鞋子,2)……

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        init();

    }

    private void init() {
        button_task_warn = findViewById(R.id.button_task_warn);
        button_task_out = findViewById(R.id.button_task_out);
        button_task_random_select = findViewById(R.id.button_task_random_select);

        button_task_warn.setOnClickListener(this);
        button_task_out.setOnClickListener(this);
        button_task_random_select.setOnClickListener(this);

        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_task_warn:
                Intent intent = new Intent(TaskActivity.this,WarnActivity.class);
                startActivity(intent);
                break;
            case R.id.button_task_out:

                break;
            case R.id.button_task_random_select:



                content = getRandomContent();

                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("模拟生成拣货订单")
                        .setMessage("本次随机模拟生成如下订单，确认请点击生成\n"+formatStyle(content))
                        .setNegativeButton("取消",null)
                        .setPositiveButton("生成", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
                                //获取当前时间
                                Date date = new Date(System.currentTimeMillis());
                                String dateStr = simpleDateFormat.format(date);
                                String name = "模拟订单"+dateStr;
                                String content = TaskActivity.this.content;
                                db.execSQL("insert into orders (name,content)values(?,?)",new String[]{name,content});
                                Toast.makeText(TaskActivity.this, "生成成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();


                break;
            default:
                break;
        }
    }


    private String getRandomContent() {
        String randomContent = "";//模拟的拣货订单内容，格式如(毛巾,1)(饼干,3)(鞋子,2)……
        ArrayList<String> goodList = new ArrayList<>();
            Cursor cursor = db.rawQuery("select * from goods",new String[]{});
            cursor.moveToFirst();
            do{
                Log.d("xdh0414-2",cursor.getString(cursor.getColumnIndex("name")));
                goodList.add(cursor.getString(cursor.getColumnIndex("name")));
            }while(cursor.moveToNext());

            for (int i =0;i<=(int)(Math.random()*10+1);i++){

                randomContent = randomContent+
                        "("+goodList.get((int)(Math.random()*goodList.size()))+","+(int)(Math.random()*10+1)+")";
            }
        return randomContent;
    }

    //对显示的格式稍微改动，加个换行更好看
    private String formatStyle(String content) {
        String contentToShow = "\n";
        String[] a = content.split("\\)");
        for(String b:a){
            contentToShow = contentToShow+b+")\n";
        }
        Log.d("xdh0414-3",contentToShow);
        return  contentToShow;
    }

}