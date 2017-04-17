package com.example.yls.ad0417;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.io.UnsupportedEncodingException;

;

public class MainActivity extends AppCompatActivity {
    private EditText etInput;
private Button btnCreate;
    private ImageView ivResult;
    private Button btnScan;
    private TextView txtScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        etInput= (EditText) findViewById(R.id.etInput);
        btnCreate= (Button) findViewById(R.id.btnCreate);
        ivResult= (ImageView) findViewById(R.id.ivResult);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String contents=etInput.getText().toString().trim();
                if(contents.equals("")){
                    Toast.makeText(MainActivity.this, "文本内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 如果不用更改源码，将字符串转换成ISO-8859-1编码
                try {
                    contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Bitmap bmp=encodeAsBitmap(contents);
                ivResult.setImageBitmap(bmp);
            }
        });

        btnScan= (Button) findViewById(R.id.btnScan);
        txtScan= (TextView) findViewById(R.id.txtScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent,1001);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1001){
            String result=data.getStringExtra(Intents.Scan.RESULT);
            txtScan.setText(result);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    Bitmap encodeAsBitmap(String str){
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 200, 200);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e){
            e.printStackTrace();
        } catch (IllegalArgumentException iae){ // ?
            return null;
        }

        // 如果不使用 ZXing Android Embedded 的话，要写的代码

//        int w = result.getWidth();
//        int h = result.getHeight();
//        int[] pixels = new int[w * h];
//        for (int y = 0; y < h; y++) {
//            int offset = y * w;
//            for (int x = 0; x < w; x++) {
//                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//            }
//        }
//        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels,0,100,0,0,w,h);

        return bitmap;
    }
}
