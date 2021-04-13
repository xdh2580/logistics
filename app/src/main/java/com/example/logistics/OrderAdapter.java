package com.example.logistics;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter {


    Context context;
    Cursor orderCursor;
    LayoutInflater layoutInflater;

    public OrderAdapter(Cursor orderCursor, Context context){

        this.layoutInflater = LayoutInflater.from(context);
        this.orderCursor = orderCursor;
        this.context = context;

    }

    @Override
    public int getCount() {
        //返回拣货订单的数量
        return orderCursor.getCount();
//        int num = 0;
//        orderCursor.moveToFirst();
//        while(orderCursor.moveToNext())
//            num++;
//        orderCursor.moveToFirst();
//        return num+1;
    }

    @Override
    public Cursor getItem(int position) {
        //返回对应位置的cursor,注意此时只是游标移动到了对应的那一项,而游标整个长度还是所有的order的长度
//        Cursor cursor 暂时先不写
        orderCursor.moveToFirst();
        for(int i=0;i<position;i++) {
            orderCursor.moveToNext();
            Log.d("xdh0413","移动"+i+"次:"+orderCursor.toString());
        }
        return orderCursor;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_order,null);

        TextView textView_order_info = view.findViewById(R.id.textView_order_info);
        Button button_start_select = view.findViewById(R.id.button_start_select);

        textView_order_info.setText(this.getItem(position).getString(this.getItem(position).getColumnIndex("name"))+":\n"
                +this.getItem(position).getString(this.getItem(position).getColumnIndex("content")));
        button_start_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SelectActivity2.class);
                intent.putExtra("name",
                        OrderAdapter.this.getItem(position).getString(OrderAdapter.this.getItem(position).getColumnIndex("name")));
                context.startActivity(intent);
            }
        });


        return view;
    }
}
