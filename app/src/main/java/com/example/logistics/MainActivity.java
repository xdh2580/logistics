package com.example.logistics;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mysql.cj.jdbc.Driver;

import javax.sql.ConnectionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button;
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cls = "com.mysql.cj.jdbc.Driver";
                String url = "jdbc:mysql://192.168.137.1/test1";
                String user = "root";
                String password = "123456";

                int count = 0 ;
                try{
//                    Class.forName(cls);
                    Connection connection = DriverManager.getConnection(url,user,password);
                    String a = String.valueOf(connection);
                    String sql = "select count(1) as sl from table1";
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    while (rs.next()){
                        count = rs.getInt("sl");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this,"successful"+ count +"!!!",Toast.LENGTH_SHORT).show();
            }
        });

        Button button2;
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cls = "com.mysql.cj.jdbc.Driver";
                String url = "jdbc:mysql://192.168.137.1/test1";
                String user = "root";
                String password = "123456";
                int count = 0 ;
                try{
                    Toast.makeText(MainActivity.this,"begin",Toast.LENGTH_SHORT).show();
                    Class.forName(cls);
//                    DriverManager.registerDriver(driver);
                    Connection connection = DriverManager.getConnection(url,user,password);
                    String a = String.valueOf(connection);
                    Toast.makeText(MainActivity.this,a,Toast.LENGTH_SHORT).show();
                    String sql = "select count(1) as sl from table1";
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    while (rs.next()){
                        count = rs.getInt("sl");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }
}