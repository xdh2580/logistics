package com.example.logistics;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_query;
    private Button button_in;
    private Button button_out;
    private Button button_add;
    private Button button_manage;
    private Button button_select;
    private Button button_task;
    private Button button_other;
    private Button button_voice;
    private Button button_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        initView();

    }

    private void initView() {
        button_query = findViewById(R.id.button_query);
        button_in = findViewById(R.id.button_in);
        button_out = findViewById(R.id.button_out);
        button_add = findViewById(R.id.button_add);
        button_manage = findViewById(R.id.button_manage);
        button_select = findViewById(R.id.button_select);
        button_task = findViewById(R.id.button_task);
        button_other = findViewById(R.id.button_other);
        button_voice = findViewById(R.id.button_voice);
        button_logout = findViewById(R.id.button_logout);

        button_query.setOnClickListener(this);
        button_in.setOnClickListener(this);
        button_out.setOnClickListener(this);
        button_add.setOnClickListener(this);
        button_manage.setOnClickListener(this);
        button_select.setOnClickListener(this);
        button_task.setOnClickListener(this);
        button_other.setOnClickListener(this);
        button_voice.setOnClickListener(this);
        button_logout.setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_query:
                Intent intent = new Intent(this,QueryActivity.class);
                startActivity(intent);
                break;
            case R.id.button_in:

                break;
            case R.id.button_out:

                break;
            case R.id.button_add:

                break;
            case R.id.button_manage:

                break;
            case R.id.button_select:

                break;
            case R.id.button_task:

                break;
            case R.id.button_other:

                break;
            case R.id.button_voice:

                break;
            case R.id.button_logout:

                break;
            default:
                break;

        }
    }
}