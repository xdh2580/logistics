package com.example.logistics;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;

public class OtherActivity extends AppCompatActivity implements View.OnClickListener{


    Button button_other_export;
    Button button_other_import;
    Button button_other_check;
    File fileOfDatabase;
//     dirForImport;
    File dirForImport;

    SQLiteDatabase db;
    MyDBOpenHelper myDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        dirForImport = new File(getExternalFilesDir(null).toString()+"/importDatabaseFile");
//        File dirForImport = new File(getExternalFilesDir(null).toString()+"/importDatabaseFile");
        boolean b = dirForImport.mkdir();
        Log.d("xdh0417",String.valueOf(b));
        Toast.makeText(this, dirForImport.toString(), Toast.LENGTH_SHORT).show();

        init();

//        File root = new File("/data/data/com.xdh.allbymyself/databases");
////                getExternalFilesDir(null);
//        File[] files = root.listFiles();
//        String s = "";
//        for(File f : files){
//            s = s+f.toString()+"\n";
//        }
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }

    private void init() {
        button_other_export = findViewById(R.id.button_other_export);
        button_other_import = findViewById(R.id.button_other_import);
        button_other_check = findViewById(R.id.button_other_check);

        button_other_export.setOnClickListener(this);
        button_other_import.setOnClickListener(this);
        button_other_check.setOnClickListener(this);

        fileOfDatabase = new File("/data/data/com.example.logistics/databases/my.db");

        myDBOpenHelper = new MyDBOpenHelper(this,null,1);
        db = myDBOpenHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_other_export:


                String length = String.valueOf(fileOfDatabase.length());
//                Toast.makeText(this, length, Toast.LENGTH_SHORT).show();
                File newDbFile  = new File(getExternalFilesDir(null)+"/my.db");
//                boolean b = fileOfDatabase.renameTo(newDbFile);
               boolean b = copyFileByStream(fileOfDatabase.toString(),newDbFile.toString());
                if(b){
                    Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_other_import:

                Log.d("xdh0419","dirForImport:"+dirForImport.toString());
                boolean existDb = false;
                for(File f: dirForImport.listFiles()){
                    Log.d("xdh0419",f.toString());
                    if(f.toString().endsWith(".db")){
                        existDb = true;
                        fileOfDatabase.delete();
                        boolean s = copyFileByStream(f.toString(),fileOfDatabase.toString());
//                        boolean s = f.renameTo(fileOfDatabase);
                        if(s) {
                            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(existDb == false)
                    Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();


                break;
            case R.id.button_other_check:
//                ChangeLog.toastCheckAll(OtherActivity.this,db);
                Intent intent = new Intent(OtherActivity.this,OtherCheckActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }



    /**
     * ????????????????????????????????????
     * @param sourcePath ???????????????
     * @param targetPath ??????????????????
     */
    public static boolean copyFileByStream(String sourcePath,String targetPath){
        //???????????????
        File source = new File(sourcePath);
        //??????????????????
        File target = new File(targetPath);

        //???????????????????????????????????????
        if(!source.exists()){
            return false;
        }
        //??????????????????????????????????????????
        if(!target.getParentFile().exists()){
            return false;
//            target.getParentFile().mkdirs();
        }

        try {
            //?????????????????????
            InputStream inputStream = new FileInputStream(source);
            OutputStream outputStream = new FileOutputStream(target);
            int temp = 0;
            //????????????1024?????????
            byte[] data = new byte[1024];
            //?????????????????????????????????????????????????????????????????????????????????
            while ((temp = inputStream.read(data)) != -1){
                //????????????
                outputStream.write(data,0,temp);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}