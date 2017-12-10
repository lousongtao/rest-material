package com.shuishou.material.ui;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.Result;
import com.shuishou.material.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CameraActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    public static final String INTENTDATA_CODE = "code";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
        QRScanner();
    }
    public void QRScanner(){
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }

    @Override
    public void handleResult(Result rawResult) {
        // show the scanner result into dialog box.
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");
//        builder.setMessage(rawResult.getText());
//        AlertDialog alert1 = builder.create();
//        alert1.show();
        // If you would like to resume scanning, call this method below:
//         mScannerView.resumeCameraPreview(this);
        Intent intent = new Intent();
        intent.putExtra(INTENTDATA_CODE, rawResult.getText());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, null);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mScannerView != null)
            mScannerView.stopCamera();
    }
}
