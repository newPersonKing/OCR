package com.baidu.ocr.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.RandomAccessFile;


public class IdCardFrontActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_front);
        Bundle intent=getIntent().getExtras();

        String name=intent.getString("name");
        String gender=intent.getString("gender");
        String birthday=intent.getString("birthday");
        String ethnic=intent.getString("ethnic");
        String idNumber=intent.getString("idNumber");
        String address=intent.getString("address");

         TextView nameTv= (TextView) findViewById(R.id.name);
        TextView genderTv= (TextView) findViewById(R.id.gender);
        TextView birthdayTv= (TextView) findViewById(R.id.birthday);
        TextView ethnicTv= (TextView) findViewById(R.id.ethnic);
        TextView idNumberTv= (TextView) findViewById(R.id.idNumber);
        TextView addressTv= (TextView) findViewById(R.id.address);
        nameTv.setText(name);
        genderTv.setText(gender);
        birthdayTv.setText(birthday);
        ethnicTv.setText(ethnic);
        idNumberTv.setText(idNumber);
        addressTv.setText(address);

        String res="name=="+name+",gender=="+gender+",birthday=="+birthday+",ethnic=="+ethnic+",idNumber=="+idNumber+",address=="+address;
        String filePath = "/sdcard/Test/";
        String fileName = "ocrFront.txt";
        writeTxtToFile(res, filePath, fileName);

    }

    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

}
