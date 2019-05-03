
package com.riktheguy.qrattendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QRActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    Spinner subject;
    String GenText ;
    ProgressBar progressBar;

    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        imageView = (ImageView)findViewById(R.id.iv_QRCode);
        subject = (Spinner)findViewById(R.id.spinner_subject_type);
        button = (Button)findViewById(R.id.bt_generate);
        progressBar = (ProgressBar)findViewById(R.id.progress_circular);

        findViewById(R.id.bt_checkAtt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ManualActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddStudentActivity.class);
                startActivity(i);
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                subject.setEnabled(false);
                button.setEnabled(false);
                button.setAlpha(0.3f);
                imageView.setAlpha(0.1f);
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                GenText = "{\"name\":\""+subject.getSelectedItem().toString()+"\",\"address\":\""+format.format(d)+"\"}";
                new ScanTask().execute();
            }
        });
    }

    public void Scan(){
        try {
            bitmap = TextToImageEncode(GenText);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorPrimary):getResources().getColor(R.color.qr_bg);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public class ScanTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Scan();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(bitmap);
            subject.setEnabled(true);
            button.setEnabled(true);
            button.setAlpha(1f);
            imageView.setAlpha(1f);
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}