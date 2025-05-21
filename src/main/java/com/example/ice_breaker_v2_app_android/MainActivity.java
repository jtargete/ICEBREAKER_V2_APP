package com.example.ice_breaker_v2_app_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easyble.gatt.callback.BleConnectCallback;
import com.ficat.easyble.scan.BleScanCallback;

import java.security.Permissions;



public class MainActivity extends AppCompatActivity {
    public static final int BLE_REQUEST = 2;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Button start = (Button) findViewById(R.id.button);
        Button about = (Button) findViewById(R.id.button3);
        ProgressDialog Scanning = new ProgressDialog(MainActivity.this);
        BleManager bleManager = BleManager
                .getInstance()
                .init(this.getApplication());


        if (!BleManager.isBluetoothOn())
        {
            BleManager.enableBluetooth(MainActivity.this,BLE_REQUEST);
        }


        BleConnectCallback bleConnectCallback = new BleConnectCallback() {
            @Override
            public void onStart(boolean startConnectSuccess, String info, BleDevice device) {
                if (startConnectSuccess) {
                    Toast.makeText(MainActivity.this,"Connection Starting",Toast.LENGTH_SHORT).show();
                } else {
                    //fail to start connection, see details from 'info'
                    String failReason = info;
                    Toast.makeText(MainActivity.this,failReason,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int failCode, String info, BleDevice device) {
                if(failCode == BleConnectCallback.FAIL_CONNECT_TIMEOUT){
                    //connection timeout
                }else{
                    Toast.makeText(MainActivity.this,"Failed Connection",Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onConnected(BleDevice device) {
                Scanning.dismiss();
                Toast.makeText(MainActivity.this,"Successful Connection",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, PreOpPage.class);
                intent.putExtra(PreOpPage.KEY_DEVICE_INFO, device);
                startActivity(intent);

            }

            @Override
            public void onDisconnected(String info, int status, BleDevice device) {

            }
        };

        BleScanCallback bleScanCallback = new BleScanCallback () {
            @Override
            public void onLeScan(BleDevice device, int rssi, byte[] scanRecord) {

                Scanning.setMessage("Scanning for ICE BREAKER ROBOT please wait...");
                Scanning.show();
                String address = "D0:B5:C2:B4:75:F5";
                bleManager.connect(address, bleConnectCallback);
                bleManager.stopScan();

            }

            @Override
            public void onStart(boolean startScanSuccess, String info) {

                if (startScanSuccess) {
                    Toast.makeText(MainActivity.this,"Scanning Started",Toast.LENGTH_LONG).show();
                } else {
                    //fail to start scan, you can see details from 'info'
                    String failReason = info;
                    Toast.makeText(MainActivity.this,failReason,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFinish() {




            }
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bleManager.startScan(bleScanCallback);


            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AboutPage.class));
            }
        });



    }




}