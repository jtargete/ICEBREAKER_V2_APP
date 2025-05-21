package com.example.ice_breaker_v2_app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easyble.BleReceiver;
import com.ficat.easyble.gatt.BleGatt;
import com.ficat.easyble.gatt.callback.BleWriteCallback;


import java.nio.charset.StandardCharsets;

public class PreOpPage extends AppCompatActivity {
    public static final String KEY_DEVICE_INFO = "keyDeviceInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_op_page);
        getSupportActionBar().hide();

        ProgressDialog Sending = new ProgressDialog(PreOpPage.this);

        BleManager bleManager = BleManager
                .getInstance();

        BleDevice device = getIntent().getParcelableExtra(KEY_DEVICE_INFO);


        String SERVUUID = "0000FFE0-0000-1000-8000-00805F9B34FB";
        String CHARUUID = "0000FFE1-0000-1000-8000-00805F9B34FB";
        
        Button start= (Button) findViewById(R.id.button4);
        TextView EnterText = (TextView) findViewById(R.id.textView7);
        TextView WidthText = (TextView) findViewById(R.id.textView9);
        TextView LengthText = (TextView) findViewById(R.id.textView8);
        EditText LengthNumber = (EditText) findViewById(R.id.LengthEdit);
        EditText WidthNumber = (EditText) findViewById(R.id.WidthEdit);


        Button Continue = (Button) findViewById(R.id.button2);
        TextView TitleText = (TextView) findViewById(R.id.textView3);
        TextView AreaText = (TextView) findViewById(R.id.textView4);
        TextView FigureText = (TextView) findViewById(R.id.textView5);
        TextView RobotText = (TextView) findViewById(R.id.textView6);
        ImageView AreaImage = (ImageView) findViewById(R.id.imageView2);
        ImageView RobotImage = (ImageView) findViewById(R.id.imageView3);

        start.setVisibility(View.INVISIBLE);
        EnterText.setVisibility(View.INVISIBLE);
        LengthText.setVisibility(View.INVISIBLE);
        WidthText.setVisibility(View.INVISIBLE);
        LengthNumber.setVisibility(View.INVISIBLE);
        WidthNumber.setVisibility(View.INVISIBLE);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Continue.setVisibility(View.INVISIBLE);
                TitleText.setVisibility(View.INVISIBLE);
                AreaText.setVisibility(View.INVISIBLE);
                FigureText.setVisibility(View.INVISIBLE);
                RobotText.setVisibility(View.INVISIBLE);
                AreaImage.setVisibility(View.INVISIBLE);
                RobotImage.setVisibility(View.INVISIBLE);

                start.setVisibility(View.VISIBLE);
                EnterText.setVisibility(View.VISIBLE);
                LengthText.setVisibility(View.VISIBLE);
                WidthText.setVisibility(View.VISIBLE);
                LengthNumber.setVisibility(View.VISIBLE);
                WidthNumber.setVisibility(View.VISIBLE);


            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ToRobotString = LengthNumber.getText().toString() + " " + WidthNumber.getText().toString();
                byte[] ToRobotByte = ToRobotString.getBytes();
                Sending.setMessage("Sending Data please wait...");
                Sending.show();

                bleManager.write(device, SERVUUID, CHARUUID, ToRobotByte, new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(byte[] data, BleDevice device) {
                        Sending.dismiss();
                        Toast.makeText(PreOpPage.this,"Data Sent",Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(PreOpPage.this, OperationPage.class);
                        intent2.putExtra(OperationPage.KEY_DEVICE_INFO2, device);
                        startActivity(intent2);


                    }

                    @Override
                    public void onFailure(int failCode, String info, BleDevice device) {

                        Toast.makeText(PreOpPage.this,info,Toast.LENGTH_LONG).show();

                    }
                });


            }
        });



    }
}