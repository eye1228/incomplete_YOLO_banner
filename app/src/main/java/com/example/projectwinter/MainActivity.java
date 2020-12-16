package com.example.projectwinter;


import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.IntStream;



public class MainActivity extends AppCompatActivity {

    Bitmap image; //사용되는 이미지
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//귀찮아서 카메라 안했음. 알아서 하셈 ㅎㅎ...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //이미지 디코딩을 위한 초기화
        //image = BitmapFactory.decodeResource(getResources(), R.drawable.sample2); //샘플이미지파일
        //언어파일 경로
        datapath += getFilesDir() + "/tesseract/";
        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        String lang = "kor";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(),SendActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    //Process an Image
    public void processImage(View view) {
        String OCRresult = null;
        button.setVisibility(view.VISIBLE);//잠시 제외

        //가라
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                // 시간 지난 후 실행할 코딩
            }
        }, 2000); // 0.5초후
        ImageView imageresult = (ImageView)findViewById(R.id.imageResult);
        imageresult.setImageResource(R.drawable.sample3);
        //
        image = BitmapFactory.decodeResource(getResources(), R.drawable.sample3);
        mTess.setImage(image);
        OCRresult = mTess.getUTF8Text();
        String str = "";
        String[] array_word;
        String[] phone_number = new String[50];


        array_word = OCRresult.split("");
        int k = 0;


        //
        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        OCRTextView.setMovementMethod(new ScrollingMovementMethod());
        //OCRTextView.setText(OCRresult);
        //OCRTextView.setText(phone_number[0]);
        //OCRTextView.setText("여략쳐:회장010-4710-3574");
        OCRTextView.setText("01047103574");
    }


    //copy file to device
    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/kor2.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/kor2.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        else if(dir.exists()) {
            Log.d("check","??" + dir);
            Log.d("check","exist 있데");
            String datafilepath = datapath + "/tessdata/kor2.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
    }
}