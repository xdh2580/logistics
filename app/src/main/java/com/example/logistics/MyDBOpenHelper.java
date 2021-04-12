package com.example.logistics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    public MyDBOpenHelper(Context context, SQLiteDatabase.CursorFactory factory,
                          int version) {super(context, "my.db", null, 1); }
    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE user(name VARCHAR(20),password VARCHAR(20))");//AUTOINCREMENT
        db.execSQL("INSERT INTO user(name,password)VALUES('admin','123456')");
        db.execSQL("INSERT INTO user(name,password)VALUES('user1','123456')");
        db.execSQL("INSERT INTO user(name,password)VALUES('user2','654321')");

        db.execSQL("CREATE TABLE goods(gid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20),quantity INTEGER,shelve INTEGER,layer INTEGER)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('牛奶',268,1,2)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('纸巾',354,2,1)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('洗衣液',56,2,2)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('毛巾',135,2,3)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('方便面',257,1,1)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('饼干',457,1,4)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('牛肉干',569,1,3)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('咖啡',395,1,5)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('面包',563,1,6)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('鞋子',146,3,2)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('夹克',79,3,1)");
        db.execSQL("INSERT INTO goods(name,quantity,shelve,layer)VALUES('牛仔裤',246,3,2)");


    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) ");
    }
}