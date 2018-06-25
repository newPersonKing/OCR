package com.baidu.ocr.demo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Locale;

/**
 * Created by fitz on 2018/5/16.
 */

public class IDActivity extends AppCompatActivity {
    private TextView name;
    TextToSpeech tts;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        name = (TextView) findViewById(R.id.name);

        Intent intent =getIntent();

        final String bir = intent.getStringExtra("bir");
        final String adr = intent.getStringExtra("adr");
        final String res = "识别结果"+"\n"+"签发机关："+adr+"\n"+"有效期限："+bir;

        name.setText(res);
        String filePath = "/sdcard/Test/";
        String fileName = "ocr.txt";

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                // 如果装载TTS引擎成功
                if (status == TextToSpeech.SUCCESS)
                {
                    // 设置使用美式英语朗读
                    int result = tts.setLanguage(Locale.CHINA);
                    // 如果不支持所设置的语言
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE)
                    {
                        Toast.makeText(IDActivity.this, "TTS暂时不支持这种语言的朗读。", Toast.LENGTH_SHORT)
                                .show();
                    }
                    tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });
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
    @Override
    public void onDestroy() {
        // 关闭TextToSpeech对象
        super.onDestroy();
        if (tts != null) {
            tts.shutdown();
        }
    }

}
