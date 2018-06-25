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
 * Created by fitz on 2018/5/15.
 */

public class PutongActivity extends AppCompatActivity {
    private TextView putongtxt;
    TextToSpeech tts;
    String word = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putong);

        putongtxt= (TextView) findViewById(R.id.putongtxt);
        Intent intent =getIntent();
        String res = intent.getStringExtra("result");

        try {
            JSONObject object = new JSONObject(res);
            System.out.println("11111");
            System.out.println( );
            JSONArray array=new JSONArray(object.get("words_result").toString());
            for (int i =0;i <array.length();i++){
                JSONObject j = new JSONObject(String.valueOf(array.get(i)));

                    word = word+"\n"+j.get("words").toString();


            }
            System.out.println("11111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        putongtxt.setText(word);
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
                        Toast.makeText(PutongActivity.this, "TTS暂时不支持这种语言的朗读。", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                tts.speak(word, TextToSpeech.QUEUE_ADD, null);
            }
        });
        writeTxtToFile(word, filePath, fileName);
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
