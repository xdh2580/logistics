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

        //账户表user
        db.execSQL("CREATE TABLE user(name varchar(20),password varchar(20))");//AUTOINCREMENT
        db.execSQL("INSERT INTO user(name,password)VALUES('a','123')");
        db.execSQL("INSERT INTO user(name,password)VALUES('admin','123456')");
        db.execSQL("INSERT INTO user(name,password)VALUES('user1','123456')");
        db.execSQL("INSERT INTO user(name,password)VALUES('user2','654321')");
        //货品表goods
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

        //拣货订单表（订单名称，订单内容）
        //内容格式存储为字符串(goodName1,quantity1)(goodName2,quantity2)……
        db.execSQL("create table orders(name varchar(50),content varchar(900))");
        db.execSQL("insert into orders(name,content)values('拣货订单1','(牛奶,2)(饼干,3)')");
        db.execSQL("insert into orders(name,content)values('拣货订单2','(毛巾,1)(饼干,3)(鞋子,2)')");
        db.execSQL("insert into orders(name,content)values('拣货订单3','(牛奶,2)(饼干,3)(夹克,1)(洗衣液,2)(咖啡,4)')");
        db.execSQL("insert into orders(name,content)values('拣货订单4','(饼干,2)(鞋子,3)(咖啡,1)(牛肉干,2)(洗衣液,4)')");
        db.execSQL("insert into orders(name,content)values('拣货订单5','(面包,5)(方便面,6)(咖啡,4)(牛仔裤,2)(夹克,2)(纸巾,5)')");

        //Log日志表，纪录所有的操作及变更
        //type为操作变更的类型，login登录，logout退出登录，register注册，in入库，out出库，add新增货品，mod修改货品信息，del删除货品信息,select完成拣货
        //detail为该条操作的详细信息，time为操作的时间
        db.execSQL("create table log(user varchar(20),type varchar(20),detail varchar(50),time varchar(30))");
    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}