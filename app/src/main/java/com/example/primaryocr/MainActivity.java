package com.example.primaryocr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    //각각 이미지 저장 변수
    //경로 변수
    //언어 변수
    //언어 경로
    private Bitmap image = null;
    private String dataPath = "";
    private final String lang = "eng";
    private final String korPath = "tessdata/"+lang+".traineddata";

    //Tess-two 변
    private TessBaseAPI mTess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //이미지
        int imageId = R.drawable.example;

        Button button = this.findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTess.setImage(image);
                String result = mTess.getUTF8Text();
                TextView textView = findViewById(R.id.textView);
                textView.setText(result);
            }
        });

        ImageView imageView = this.findViewById(R.id.imageView);
        imageView.setImageResource(imageId);

        //이미지 bitmap 으로 변환 후 값 넣기
        image = BitmapFactory.decodeResource(getResources(), imageId);

        //언어 파일 경로
        dataPath = getFilesDir() + "/tesseract/";

        checkFile(new File(dataPath + "tessdata/"));

        mTess = new TessBaseAPI();
        mTess.init(dataPath, lang);

    }

    private void copyFiles() {
        try {
            String filePath = dataPath + "/" + korPath;
            AssetManager assetManager = getAssets();
            InputStream inStream = assetManager.open(korPath);
            OutputStream outStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[1024];
            int read;
            while((read = inStream.read(buffer)) != -1)
                outStream.write(buffer, 0, read);
            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir) {
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles();
        }
        if (dir.exists()) {
            String dataFilePath = dataPath + "/" + korPath;
            File datafile = new File(dataFilePath);
            if (!datafile.exists()) copyFiles();
        }
    }
}