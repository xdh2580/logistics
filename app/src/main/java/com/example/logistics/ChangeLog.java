package com.example.logistics;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeLog  {


    static void logIn(Context context,String userName,SQLiteDatabase db){
            db.execSQL("insert into log(user,type,detail,time)values(?,'login','成功登录',?)",new String[]{userName,getCurrentTime()});
    }
    static void register(Context context,String userName,SQLiteDatabase db,String password){
        db.execSQL("insert into log(user,type,detail,time)values(?,'register',?,?)",
                new String[]{userName,"用户名:"+userName+"密码:"+password,getCurrentTime()});
    }
    static void in(Context context,String userName,SQLiteDatabase db,String detail){
        db.execSQL("insert into log(user,type,detail,time)values(?,'goodIn',?,?)",
                new String[]{userName,detail,getCurrentTime()});
    }
    public static void out(Context context, String userName, SQLiteDatabase db, String detail) {
        db.execSQL("insert into log(user,type,detail,time)values(?,'goodOut',?,?)",
                new String[]{userName,detail,getCurrentTime()});
    }
    public static void add(Context context, String userName, SQLiteDatabase db, String detail) {
        db.execSQL("insert into log(user,type,detail,time)values(?,'addGood',?,?)",
                new String[]{userName,detail,getCurrentTime()});
    }
    public static void del(Context context, String userName, SQLiteDatabase db, String detail) {
        db.execSQL("insert into log(user,type,detail,time)values(?,'delGood',?,?)",
                new String[]{userName,detail,getCurrentTime()});
    }
    public static void mod(Context context, String userName, SQLiteDatabase db, String detail) {
        db.execSQL("insert into log(user,type,detail,time)values(?,'modGood',?,?)",
                new String[]{userName,detail,getCurrentTime()});
    }
    public static void select(Context context, String userName, SQLiteDatabase db, String detail) {
        db.execSQL("insert into log(user,type,detail,time)values(?,'modGood',?,?)",
                new String[]{userName,detail,getCurrentTime()});
    }

    private static String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    //弹出所有数据库log记录，调试用方法
    static void toastCheckAll(Context context, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select * from log",new String[]{});
        cursor.moveToFirst();
        StringBuilder infoToShow = new StringBuilder("result\n");
        do {
            String user = cursor.getString(cursor.getColumnIndex("user"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String detail = cursor.getString(cursor.getColumnIndex("detail"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String toAppend = "用户:"+user+"  操作:"+type+"  详情:"+detail+"  时间:"+time+"\n";
            infoToShow.append(toAppend);
        }while(cursor.moveToNext());
        Toast.makeText(context, infoToShow.toString(), Toast.LENGTH_SHORT).show();
    }


}
