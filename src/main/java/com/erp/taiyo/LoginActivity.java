package com.erp.taiyo;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class LoginActivity extends AppCompatActivity {

    TextView versionLabel, userIp, userId, userPw ;
    CheckBox chkip, chkid, chkauto;
    Button btnLogin;
    String strUserIp, strUserId, strUserPw; // strUserIp
    String strStatus = "1";
    String versionString;
    String newToken;
    String telNumber = "";
    String sobId ="80";
    String orgId ="801";
    //String testIp = "106.251.238.99:8091";
    String storeVersion = "";
    String deviceVersion = "";
    RelativeLayout mainLayout;
    //log
    SharedPreferences auto;
    //login
    SharedPreferences appData;
    //키보드
    InputMethodManager imm;
    private boolean saveLoginData;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        versionLabel = findViewById(R.id.label_version);
        userIp = findViewById(R.id.userip);
        userId = findViewById(R.id.userid);
        userPw = findViewById(R.id.userpassword);
        btnLogin = findViewById(R.id.btn_login);

        userId.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); // 소문자로 입력된 값을 대문자로 바꿔줌.

        new InputFilter.AllCaps(); // 소문자로 입력된 값을 대문자로 바꿔 줌.


        //체크박스

        chkip = findViewById(R.id.chk_ip);
        chkid = findViewById(R.id.chk_id);
        chkauto = findViewById(R.id.chk_auto);

        //메인
        mainLayout = findViewById(R.id.main_layout);

        //키보드
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        //자동로그인
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();


    /*    FirebaseInstanceId.getInstance().getToken();
        //Log.d("태그", FirebaseInstanceId.getInstance().getToken());

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
            }
        });*/


        if(chkip.isChecked())
        {
            userIp.setText(strUserIp);
        }
        if(chkid.isChecked())
        {
            userId.setText(strUserId);
        }
        if(chkauto.isChecked())
        {
            userPw.setText(strUserPw);
        }

        if(chkip.isChecked() && chkid.isChecked() && chkauto.isChecked()) {
            strUserIp = userIp.getText().toString();
            strUserId = userId.getText().toString();
            strUserPw = userPw.getText().toString();

            DB_Version dbVersion = new DB_Version();
            dbVersion.execute(userIp.getText().toString());

            //DB_Login dbLogin = new DB_Login();
            //dbLogin.execute(userIp.getText().toString(), userId.getText().toString(), userPw.getText().toString(), sobId, orgId);
            // 로그인 성공시 저장 처리, 예제는 무조건 저장
            save();
        }else if(chkauto.isChecked()){
            strUserIp = userIp.getText().toString();
            strUserId = userId.getText().toString();
            strUserPw = userPw.getText().toString();

            DB_Version dbVersion = new DB_Version();
            dbVersion.execute(userIp.getText().toString());

            // 로그인 성공시 저장 처리, 예제는 무조건 저장
            save();
        }

        initializeVersionString();



        //로그인 정보
        auto = getSharedPreferences("appData_Log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_save = auto.edit();
        editor_save.clear();
        editor_save.putString("log", "");
        editor_save.commit();

        TelephonyManager mTelephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
        int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
        if (mTelephonyManager != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "전화 권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

//                } else if (mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN || mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
//                    Toast.makeText(this, "유심이 없거나, 알 수 없는 유심입니다.", Toast.LENGTH_SHORT).show();
//
//                    moveTaskToBack(true);
//                    finish();
//                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {
               //     telNumber = mTelephonyManager.getLine1Number();
                    telNumber = telNumber.replace("+82", "0");
                }
            } else {
            //    telNumber = mTelephonyManager.getLine1Number();
                telNumber = telNumber.replace("+82", "0");
            }

        }

        //로그인 버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUserIp = userIp.getText().toString();
                strUserId = userId.getText().toString();
                strUserPw = userPw.getText().toString();


                try{
                    if(strUserIp.equals("")&strUserId.equals("") & strUserPw.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "IP, ID, PASSWORD를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        DB_Version dbVersion = new DB_Version();
                        dbVersion.execute(userIp.getText().toString());

                       // DB_Login dbLogin = new DB_Login();
                       // dbLogin.execute(userIp.getText().toString(), userId.getText().toString(), userPw.getText().toString(), sobId, orgId);
                        // 로그인 성공시 저장 처리, 예제는 무조건 저장
                        save();

                        /* DB 대조 */
//                        ContentValues values = new ContentValues();
//                        values.put("UserID", strUserId);
//                        values.put("UserPassword", strUserPw);
//                        values.put("UserStatus", 1);
//
//                        String url = "http://" + strUserIp + "/HW_DB/login.jsp";
//
//                        NetworkTask networkTask = new NetworkTask(url, values);
//                        networkTask.execute();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "IP, ID, PASSWORD를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearOnClick(view);
            }
        });


    }

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(userIp.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(userId.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(userPw.getWindowToken(), 0);
    }

    // 설정값을 저장하는 함수
    private void save() {
        SharedPreferences.Editor editor = appData.edit();
        editor.putString("ip", userIp.getText().toString().trim());
        editor.putString("id", userId.getText().toString().trim());
        editor.putString("pw", userPw.getText().toString().trim());
        editor.putBoolean("chkip", chkip.isChecked());
        editor.putBoolean("chkid", chkid.isChecked());
        editor.putBoolean("chkauto", chkauto.isChecked());


        editor.commit();	//commit
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        TelephonyManager mTelephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "전화 권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                     //   telNumber = mTelephonyManager.getLine1Number();
                        telNumber = telNumber.replace("+82", "0");
                    }


                } else {
                    Toast.makeText(this,"전화 권한이 허용되지 않았습니다.",Toast.LENGTH_LONG).show();
//                    moveTaskToBack(true);
//                    finish();
//                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                return;
            }

        }
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);

        strUserIp = appData.getString("ip", "");
        strUserId = appData.getString("id", "");
        strUserPw = appData.getString("pw", "");
        chkip.setChecked(appData.getBoolean("chkip", false));
        chkid.setChecked(appData.getBoolean("chkid", false));
        chkauto.setChecked(appData.getBoolean("chkauto", false));

    }

    private void initializeVersionString() {
        try
        {
            String packageName = getApplicationContext().getPackageName();
            PackageManager packageManager = getApplicationContext().getPackageManager();
            int appVersionCode = packageManager.getPackageInfo(packageName, 0).versionCode;
            String versionName = packageManager.getPackageInfo(packageName, 0).versionName;

            String copyRightString = "Copyright 2021. Infosolution Inc. All rights reserved. ";
            versionString = "Version " + versionName + " VersionCode " + appVersionCode;

            versionLabel.setText(copyRightString + versionString);
        }
        catch (Exception e)
        {

        }
    }


    protected class DB_Login extends AsyncTask<String, Void, String>
    {
        protected  String doInBackground(String... urls)
        {
            String URL = "http://" + urls[0] + "/DRK/Login.jsp"; //자신의 웹서버 주소를 저장합니다.
            DefaultHttpClient client = new DefaultHttpClient();//HttpClient 통신을 합니다.

            try
            {
                HttpPost post = new HttpPost(URL + "?W_USER_NO=" + urls[1] +"&W_PASSWORD="+ urls[2] + "&W_SOB_ID=" + urls[3]+ "&W_ORG_ID=" + urls[4]);

                //Log 저장
                String log = "\n"+ post.toString() + "\n" +
                        "W_USER_NO = " + urls[1] +"\n" +
                        "W_PASSWORD = " + urls[2] +"\n" +
                        "W_SOB_ID = " + urls[3] +"\n" +
                        "W_ORG_ID = " + urls[4] +"\n" ;

                SharedPreferences.Editor editor_save = auto.edit();
                editor_save.putString("log",log + auto.getString("log", ""));
                editor_save.commit();

                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                String line = null;
                String result = "";

                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }

                return result;

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return ""; //결과값 리턴
        }

        @Override
            protected void onPostExecute(String result) {
            try{

                JSONObject RESULT = new JSONObject(result);

                //LOG 저장
                String log = "\n"+ RESULT.getString("RESULT");
                SharedPreferences.Editor editor_save = auto.edit();
                editor_save.putString("log",log + auto.getString("log", ""));
                editor_save.commit();

                JSONArray arr = RESULT.getJSONArray("RESULT");
                final JSONObject j_ob = arr.getJSONObject(0);

                String Flag = j_ob.getString("O_MOBILE_USER_FLAG");

                final String O_USER_ID = j_ob.getString("O_USER_ID");
                final String O_USER_NAME = j_ob.getString("O_USER_NAME");

                if(Flag.equals("Y"))
                {
                    if(j_ob.getString("msg1").equals(("succed"))){

                        //버전체크
                        if (storeVersion.compareTo(deviceVersion) > 0) {
                            // 업데이트 필요

                            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                            alert.setTitle("최신버전 업데이트");
                            alert.setCancelable(false);
                            alert.setMessage("최신버전으로 업데이트 해주세요.");
                            alert.setPositiveButton("업데이트 이동", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
//                                    UpdateApp atualizaApp = new UpdateApp();
//                                    atualizaApp.setContext(getApplicationContext());
//                                    atualizaApp.execute("http://211.253.25.11:90/NBK/flex_drk.apk"); //http://211.253.25.11:90/NBK/flex_drk.apk
                                   // Test test = new Test();
                                   // test.execute();
                                }
                            });
                            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 업데이트 불필요
                                    //토큰 저장
                                    // 업데이트 불필요
                                    //토큰 저장
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    intent.putExtra("O_USER_ID", O_USER_ID);
                                    intent.putExtra("O_USER_NAME", O_USER_NAME);
                                    // intent.putExtra("Sabeon", j_ob.getString("Sabeon"));
                                    intent.putExtra("Ip", strUserIp);
                                    intent.putExtra("versionString", versionString);
                                    startActivity(intent);
                                }
                            });
                            alert.show();
                        } else {
                            // 업데이트 불필요
                            //토큰 저장
                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            intent.putExtra("O_USER_ID", O_USER_ID);
                            intent.putExtra("O_USER_NAME", O_USER_NAME);
                            // intent.putExtra("Sabeon", j_ob.getString("Sabeon"));
                            intent.putExtra("Ip", strUserIp);
                            intent.putExtra("versionString", versionString);
                            startActivity(intent);
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "로그인 실패하였습니다. ", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "모바일 권한이 없습니다", Toast.LENGTH_SHORT).show();
                }



            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "로그인 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

//    public class NetworkTask extends AsyncTask<Void, Void, String> {
//
//        String url;
//        ContentValues values;
//
//        NetworkTask(String url, ContentValues values){
//            this.url = url;
//            this.values = values;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //progress bar를 보여주는 등등의 행위
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String result;
//            ServerActivity requestHttpURLConnection = new ServerActivity();
//            result = requestHttpURLConnection.request(url, values);
//            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // 통신이 완료되면 호출됩니다.
//            // 결과에 따른 UI 수정 등은 여기서 합니다.
//            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//            try{
//                JSONObject RESULT = new JSONObject(result);
//
//                //LOG 저장
//                String log = "\n"+ RESULT.getString("List");
//                SharedPreferences.Editor editor_save = auto.edit();
//                editor_save.putString("log",log + auto.getString("log", ""));
//                editor_save.commit();
//
//                JSONArray arr = RESULT.getJSONArray("List");
//                JSONObject j_ob = arr.getJSONObject(0);
//
//
//                if(j_ob.getString("msg1").equals("succed"))
//                {
//                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
//                    intent.putExtra("UserID", j_ob.getString("UserID"));
//                    intent.putExtra("UserStatus", j_ob.getString("UserStatus"));
//                    intent.putExtra("UserName", j_ob.getString("UserName"));
//                    intent.putExtra("Sabeon", j_ob.getString("Sabeon"));
//                    intent.putExtra("Ip", strUserIp);
//                    intent.putExtra("versionString", versionString);
//                    startActivity(intent);
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
protected class DB_Version extends AsyncTask<String, Void, String>
{
    protected  String doInBackground(String... urls)
    {

        String URL = "http://" + urls[0] + "/DRK/PtVersion.jsp"; //자신의 웹서버 주소를 저장합니다.
        DefaultHttpClient client = new DefaultHttpClient();//HttpClient 통신을 합니다.

        try
        {
            HttpPost post = new HttpPost(URL);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            String result = "";

            while ((line = bufreader.readLine()) != null) {
                result += line;
            }

            return result;

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return ""; //결과값 리턴
    }

    @Override
    protected void onPostExecute(String result) {
        try{
            JSONObject RESULT = new JSONObject(result);

            JSONArray arr = RESULT.getJSONArray("RESULT");
            JSONObject j_ob = arr.getJSONObject(0);

            if(j_ob.getString("msg1").equals("succed"))
            {
                storeVersion = j_ob.getString("X_MOBILE_VERSION_NAME");
                deviceVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

                DB_Login dbLogin = new DB_Login();
                dbLogin.execute(userIp.getText().toString(), userId.getText().toString(), userPw.getText().toString(), sobId, orgId);
            }
            else
            {

            }


        }catch (Exception e){
        e.printStackTrace();
    }
    }

    }

    public class UpdateApp extends AsyncTask<String,Void,Void>{
        private Context context;
        public void setContext(Context contextf){
            context = contextf;
        }


        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String PATH = "/mnt/sdcard/Download/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, "flex_drk.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/flex_drk.apk")), "application/vnd.android.package-archive"); ///mnt/sdcard/Download/update.apk")
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                context.startActivity(intent);


            } catch (Exception e) {
                Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }
    }

    public class Test extends AsyncTask<String,Void,Void>{
        private Context context;
        public void setContext(Context contextf){
            context = contextf;
        }


        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL("http://211.253.25.11:90/DRK/flex_drk.apk");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                File SDCardRoot = Environment.getExternalStorageDirectory();
                File file = new File(SDCardRoot,"Geocoder_Test.apk");
                FileOutputStream fileOutput = new FileOutputStream(file);

                InputStream inputStream = urlConnection.getInputStream();



                int totalSize = urlConnection.getContentLength();
                int downloadedSize = 0;

                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ( (bufferLength = inputStream.read(buffer)) > 0 )
                {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    //mProgressBar.setProgress(downloadedSize);
                    Log.e("DOWNLOAD", "saving...");
                }
                fileOutput.close();


            } catch (Exception e) {
                Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }
    }

    public void download_WebLink()
    {
        try
        {
            URL url = new URL("http://211.253.25.11:90/DRK/flex_drk.apk");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            File SDCardRoot = Environment.getExternalStorageDirectory();
            File file = new File(SDCardRoot,"Geocoder_Test.apk");
            FileOutputStream fileOutput = new FileOutputStream(file);

            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;

            byte[] buffer = new byte[2048];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 )
            {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                //mProgressBar.setProgress(downloadedSize);
                Log.e("DOWNLOAD", "saving...");
            }
            fileOutput.close();

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.e("DOWNLOAD", "end");

        Log.e("DOWNLOAD", "InstallAPK Method Called");
        installAPK();
    }

    public void installAPK()
    {
        Log.e("InstallApk", "Start");
        File apkFile = new File("/sdcard/Geocoder_Test.apk");

        Uri apkUri = Uri.fromFile(apkFile);

        Intent webLinkIntent = new Intent(Intent.ACTION_VIEW);
        webLinkIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");

        startActivity(webLinkIntent);
    }

}
