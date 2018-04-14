package com.artace.ruangbudaya;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.w3c.dom.Text;

public class GenerateQrCodeActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView qrcode;
    Toolbar mToolbar;
    TextView namaeo,namaevent,tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);

        setToolbar();

        qrcode = findViewById(R.id.generate_qrcode_image);

        namaeo = findViewById(R.id.generate_qrcode_NamaPenyelenggaraAcara);
        namaevent = findViewById(R.id.generate_qrcode_namaEvent);
        tanggal = findViewById(R.id.generate_qrcode_tanggalAcara);

        Bundle extras = getIntent().getExtras();

        int kode = extras.getInt("id_tawaran_tampil");

        namaeo.setText(extras.getString("namaeo"));
        namaevent.setText(extras.getString("namaevent"));
        tanggal.setText(extras.getString("tanggal"));

        try {
            bitmap = TextToImageEncode(String.valueOf(kode));
            qrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("gamao",e.getMessage());
        }

    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    200, 200, null
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
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 200, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


    private void setToolbar(){
        mToolbar = findViewById(R.id.generate_qrcode_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Konfirmasi Kehadiran");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
