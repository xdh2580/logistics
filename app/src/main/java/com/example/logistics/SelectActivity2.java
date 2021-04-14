package com.example.logistics;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.*;

public class SelectActivity2 extends AppCompatActivity {

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;
    ListView list_select_route;
    Button button_select2_quit;
    Button button_select2_finish;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> goodList;//content拆分的集合，元素为字符串形式的(name1,quantity1)……
    HashMap<String,Integer> goodMap;//goodList转化成的map,key为货品名，value为要拣货的数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select2);

        init();

        final String orderName = getIntent().getStringExtra("name");

        Cursor cursor = db.rawQuery("select * from orders where name=?",new String[]{orderName});
        cursor.moveToFirst();
        String orderContent = cursor.getString(cursor.getColumnIndex("content"));
        TextView textView = findViewById(R.id.textView27);
        textView.setText(orderName);

        goodList = getGoodsContent(orderContent);
        goodMap = getGoodsMap(goodList);//key:货品名，value：订单中要取出的数量
        goodList = getOrderListForShow(goodMap);

//        ArrayList<String> testList = new ArrayList<>();
//        testList = easyGetOrderListForShow(goodMap);

        arrayAdapter = new ArrayAdapter(SelectActivity2.this,R.layout.list_item,goodList);
        list_select_route.setAdapter(arrayAdapter);

        button_select2_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectActivity2.this.finish();
            }
        });
        button_select2_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("delete from orders where name=?",new String[]{orderName});
                Toast.makeText(SelectActivity2.this, "拣货完成", Toast.LENGTH_SHORT).show();
//                SelectActivity2.this.finish();

                updateGoods();//更新数据库中的goods表（货品的数量）

                Intent intent = new Intent(SelectActivity2.this,MActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    //拣货完成后更新货品数量
    private void updateGoods() {
        Iterator<Map.Entry<String,Integer>> iterator = goodMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,Integer> entry = iterator.next();
            db.execSQL("update goods set quantity =quantity-? where name=?",
                    new String[]{entry.getValue().toString(),entry.getKey()});
        }
    }


    private ArrayList<String> getOrderListForShow(HashMap<String,Integer> goodMap) {


        ArrayList<String> listWithOrder = new ArrayList<>();
        HashMap<Integer,Cursor> orderMap = new HashMap<>();//并不order，只是记录了每个货品对应游标的应该取货的顺序的值，即key的大小，但key无序
        Iterator<Map.Entry<String,Integer>> iterator = goodMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,Integer> entry = iterator.next();
            Cursor cursor = db.rawQuery("select * from goods where name=? ",new String[]{entry.getKey()});
            cursor.moveToFirst();
            do{
                Integer shelve = cursor.getInt(cursor.getColumnIndex("shelve"));
                Integer layer = cursor.getInt(cursor.getColumnIndex("layer"));
                orderMap.put(shelve*100+layer,cursor);//key：取货的顺序值，越小越先，value：要取商品对应的Cursor
            }while(cursor.moveToNext());//其实没有next，只有一条数据
        }
        listWithOrder = realGetOrderListForShow(orderMap,goodMap);
        return  listWithOrder;
    }

    //使用TreeMap对hashMap进行排序，因为TreeMap内部以key的值的顺序存储数据。
    //此方法返回要输出打印到listview子项的字符串的ArrayList集合
    //第一个map参数：key是取货品的顺序的相对值，value是要取货品对应的Cursor
    // 第二个map参数：key是货品名称，value是要取出的数量
    private ArrayList<String> realGetOrderListForShow(HashMap<Integer,Cursor> mapWithOrder,HashMap<String,Integer> goodMapWithNumber) {
        ArrayList<String> listWithOrder = new ArrayList<>();
        TreeMap<Integer,Cursor> sortedMap =new TreeMap<>();
        sortedMap.putAll(mapWithOrder);
        Iterator<Map.Entry<Integer,Cursor>> iterator = sortedMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer,Cursor> entry = iterator.next();
            Cursor cursor =entry.getValue();
            cursor.moveToFirst();
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Integer quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                Integer shelve = cursor.getInt(cursor.getColumnIndex("shelve"));
                Integer layer = cursor.getInt(cursor.getColumnIndex("layer"));
                String toShow = name + "\t\t货架:" + shelve + " 隔层:" + layer + " 剩余:" + quantity + " 应取:" + goodMapWithNumber.get(name);
                listWithOrder.add(toShow);
            } while (cursor.moveToNext());//其实没有next，只有一条数据
        }

        return listWithOrder;
    }


    private void init() {
        list_select_route = findViewById(R.id.list_select_route);
        button_select2_quit = findViewById(R.id.button_select2_quit);
        button_select2_finish = findViewById(R.id.button_select2_finish);
        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();
    }


    private ArrayList<String> getGoodsContent(String content){

        ArrayList<String> list = new ArrayList<>();
        String[] a =  content.split("\\)");
        for(String b:a){
            b=b.substring(1,b.length());
            Log.d("xdh0413-2",b);
            list.add(b);
        }
        return list;
    }

    private HashMap<String, Integer> getGoodsMap(ArrayList<String> goodList) {
        HashMap<String,Integer> map = new HashMap<>();
        for(String a:goodList){
            String[] split = a.split(",");
            map.put(split[0],Integer.parseInt(split[1]));
            Log.d("xdh0413-3",map.toString());
        }
        return map;
    }

    //这个方法暂时没用，使用拼接也不行，？占位符也不行
    private ArrayList<String> easyGetOrderListForShow(HashMap<String,Integer> goodMap) {
        ArrayList<String> listWithOrder = new ArrayList<>();
        //以下想法行不通，经过实验，rawQuery中sql语句的一个？占位符只能代表一个参数，做不到动态适应参数数量的变化，也不能使用组合出符合格式的一个字符串替代
        Set<String> goodNameSet =goodMap.keySet();
        String[] goodNameArray = goodNameSet.toArray(new String[0]);
        String b="'";
        for(String a:goodNameArray) {
            Log.d("xdh-test", a);
            b=b+a+"','";
        }
        b = b.substring(0,b.length()-2);
        Log.d("xdh-test",b);
//        Cursor cursor0 = db.rawQuery("select * from goods where name in ("+b+")",new String[]{});
//        cursor0.moveToFirst();
//        do {
//            Log.d("xdh-test", "结果"+cursor0.getString(cursor0.getColumnIndex("name")));
//        }while (cursor0.moveToNext());

        return listWithOrder;
    }

}