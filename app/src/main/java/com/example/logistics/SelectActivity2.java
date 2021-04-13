package com.example.logistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SelectActivity2 extends AppCompatActivity {

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;
    ListView list_select_route;
    Button button_select2_quit;
    Button button_select2_finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select2);

        init();

        String orderName = getIntent().getStringExtra("name");

        Cursor cursor = db.rawQuery("select * from orders where name=?",new String[]{orderName});
        cursor.moveToFirst();
        String orderContent = cursor.getString(cursor.getColumnIndex("content"));
        TextView textView = findViewById(R.id.textView27);
        textView.setText(orderName);

        String[] a =  orderContent.split("\\)");
        for(String b:a){
            Log.d("xdh0413-2",b);
        }


    }

    private void init() {
        list_select_route = findViewById(R.id.list_select_route);
        button_select2_quit = findViewById(R.id.button_select2_quit);
        button_select2_finish = findViewById(R.id.button_select2_finish);
        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();
    }
}