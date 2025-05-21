package com.example.ice_breaker_v2_app_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easyble.Logger;
import com.ficat.easyble.gatt.callback.BleNotifyCallback;
import com.ficat.easyble.gatt.callback.BleReadCallback;
import com.ficat.easyble.gatt.callback.BleWriteCallback;

public class OperationPage extends AppCompatActivity {

    public static final String KEY_DEVICE_INFO2 = "keyDeviceInfo2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_page);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationChannel ObstacleChannel = new NotificationChannel("ObstacleNOTIFY","Obstacle", NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel StopChannel = new NotificationChannel("StopNOTIFY","Stop", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(ObstacleChannel);
            manager.createNotificationChannel(StopChannel);

        }
        Button stop = (Button) findViewById(R.id.button6);
        String stopString = "S";
        byte[] stopbyte = stopString.getBytes();
        TextView WarningText = (TextView) findViewById(R.id.textView16);
        TextView ObstacleText = (TextView) findViewById(R.id.textView17);
        TextView StopText = (TextView) findViewById(R.id.textView18);
        ObstacleText.setVisibility(View.INVISIBLE);
        StopText.setVisibility(View.INVISIBLE);

        BleManager bleManager = BleManager
                .getInstance();

        BleDevice device = getIntent().getParcelableExtra(KEY_DEVICE_INFO2);

        String SERVUUID = "0000FFE0-0000-1000-8000-00805F9B34FB";
        String CHARUUID = "0000FFE1-0000-1000-8000-00805F9B34FB";

        BleWriteCallback writeCallback = new BleWriteCallback() {
            @Override
            public void onWriteSuccess(byte[] data, BleDevice device) {

            }

            @Override
            public void onFailure(int failCode, String info, BleDevice device) {

            }
        };
        BleNotifyCallback notifyCallback = new BleNotifyCallback() {
             @Override
             public void onFailure(int failCode, String info, BleDevice device) {

             }

             @Override
            public void onCharacteristicChanged(byte[] data, BleDevice device) {
                String s = new String(data);


                switch (s)
                {
                    case "OBSTACLE DETECTED":
                        WarningText.setVisibility(View.INVISIBLE);
                        ObstacleText.setVisibility(View.VISIBLE);
                        OBSTACLENOTIFY();
                        break;

                    case "STOP":
                        WarningText.setVisibility(View.INVISIBLE);
                        StopText.setVisibility(View.VISIBLE);
                        STOPNOTIFY();
                }

             }

             @Override
             public void onNotifySuccess(String notifySuccessUuid, BleDevice device) {

             }
         };

        bleManager.notify(device,SERVUUID,CHARUUID,notifyCallback);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bleManager.write(device,SERVUUID,CHARUUID,stopbyte,writeCallback);
            }
        });
    }

    void OBSTACLENOTIFY()
    {
        NotificationCompat.Builder ObstacleBuild = new NotificationCompat.Builder(OperationPage.this, "ObstacleNOTIFY");
        ObstacleBuild.setContentTitle("OBSTACLE DETECTED");
        ObstacleBuild.setContentText("Please Move Object From Robot to Resume");
        ObstacleBuild.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        ObstacleBuild.setSmallIcon(R.mipmap.ic_launcher);
        ObstacleBuild.setPriority(NotificationCompat.PRIORITY_HIGH);
        ObstacleBuild.setAutoCancel(true);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(OperationPage.this);
        managerCompat.notify(1,ObstacleBuild.build());
    }

    void STOPNOTIFY()
    {
        NotificationCompat.Builder StopBuild = new NotificationCompat.Builder(OperationPage.this, "StopNOTIFY");
        StopBuild.setContentTitle("ROBOT HAS STOPPED");
        StopBuild.setContentText("Robot has stopped moving");
        StopBuild.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        StopBuild.setSmallIcon(R.mipmap.ic_launcher);
        StopBuild.setPriority(NotificationCompat.PRIORITY_HIGH);
        StopBuild.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(OperationPage.this);
        managerCompat.notify(1,StopBuild.build());
    }

}


