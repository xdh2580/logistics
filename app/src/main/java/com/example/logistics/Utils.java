package com.example.logistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Utils {
    static String currentLoginUserName;//用来记录当前登录的用户
    private static MyDBOpenHelper myDBOpenHelper;
    private static SQLiteDatabase db;


    public static String answer(Context context,String recoResult){
        myDBOpenHelper = new MyDBOpenHelper(context,null,1);
        db = myDBOpenHelper.getWritableDatabase();

        String answer=null;
        if(recoResult.contains("在哪")){
            int l =  recoResult.indexOf("在哪");
            String name = recoResult.substring(0,l);
            boolean e = checkGoodsName(name);
            if(!e) {
                answer = "错误，没有找到该货品";
            }else{
                Log.d("xdh", "即将开始查询");
                Cursor cursor = db.rawQuery("select * from goods where name=?", new String[]{name});
                cursor.moveToFirst();
                int shelve = cursor.getInt(cursor.getColumnIndex("shelve"));
                int layer = cursor.getInt(cursor.getColumnIndex("layer"));
                answer = name + "货架" + String.valueOf(shelve) + "隔层" + String.valueOf(layer);
            }
        }
        if(recoResult.contains("库存")||recoResult.contains("还有多少")||recoResult.contains("还剩多少")){
            int l = 0;
            if(recoResult.contains("库存")) {
                l = recoResult.indexOf("库存");
            }else if (recoResult.contains("还有多少")){
                l = recoResult.indexOf("还有多少");
            }else if(recoResult.contains("还剩多少")){
                l = recoResult.indexOf("还剩多少");
            }
            String name = recoResult.substring(0,l);
            boolean e = checkGoodsName(name);
            if(!e){
                answer = "错误，没有找到该货品";
            }else {
                Cursor cursor = db.rawQuery("select * from goods where name=?", new String[]{name});
                cursor.moveToFirst();
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                answer = name + "库存" + String.valueOf(quantity);
            }
        }
        return answer;
    }

    private static boolean checkGoodsName(String name) {
        Cursor cursor0 = db.rawQuery("select * from goods",new String[]{});
        cursor0.moveToFirst();
        boolean e = false;
        do{
            String na = cursor0.getString(cursor0.getColumnIndex("name"));
            if((na.equals(name))){
                e=true;
                Log.d("xdh0430","success");
            }
        }while (cursor0.moveToNext());
        Log.d("xdh0430","not exit");
        return  e;
    }


}
