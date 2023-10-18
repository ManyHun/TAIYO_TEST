package com.erp.taiyo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    String deviceVersion;
    String storeVersion;
    public static Activity firstActivity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }



    private final DeviceVersionCheckHandler deviceVersionCheckHandler = new DeviceVersionCheckHandler(this);

    // 핸들러 객체 만들기
    private static class DeviceVersionCheckHandler extends Handler {
        private final WeakReference<MainActivity> mainActivityWeakReference;
        public DeviceVersionCheckHandler(MainActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mainActivityWeakReference.get();
            if (activity != null) {

                activity.handleMessage(msg);
                // 핸들메세지로 결과값 전달
            }
        }
    }


    private void handleMessage(Message msg) {
        //핸들러에서 넘어온 값 체크

        if (storeVersion.compareTo(deviceVersion) > 0) {
            // 업데이트 필요

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("최신버전 업데이트");
            alert.setCancelable(false);
            alert.setMessage("최신버전으로 업데이트 해주세요.");
            alert.setPositiveButton("업데이트 이동",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 업데이트 불필요
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("Store_Version", storeVersion);
                    startActivity(intent);
                    firstActivity = MainActivity.this;
                }
            });
            alert.show();
        } else {
            // 업데이트 불필요
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("Store_Version", storeVersion);
            startActivity(intent);
            firstActivity = MainActivity.this;
        }
    }



}
