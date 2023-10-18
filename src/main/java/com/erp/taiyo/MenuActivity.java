package com.erp.taiyo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.erp.taiyo.adapter.MenuListAdapter;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class MenuActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    NavigationView mNavigationView;

    ActionBarDrawerToggle mDrawerToggle;

    String strUserId, strUserPassword, strUserStatus, strUserName, strSabeon, strVerison, strIp;
    String strSobId = "80";
    String strOrgId = "801";

    TextView tvId;
    //버튼
    Button btnWare, btnWait, btnOem, btnMerger, btnInventory , btnRelease, btnMobf0007 , btnSearh;
    GridView gv;

    MenuItem ItemId;

    String[] MENU_NAME ;
    String[] MENU_PROMPT ;
    String[] MENU_DESC = new String[2];
    String[] MENU_SHOW_FLAG;

    ArrayList<String> arr2 = new ArrayList<>();

    private List<ResolveInfo> menu;
    private PackageManager pm;
    com.erp.taiyo.item.MenuIListtem menuIListtem;
    MenuListAdapter menuListAdapter;

    //login
    SharedPreferences appData;

    private ArrayList<String> arrayListTokens = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Intent intent = getIntent();
        strUserId = intent.getStringExtra("O_USER_ID");
        strUserName = intent.getStringExtra("O_USER_NAME");
        //strSabeon = intent.getStringExtra("Sabeon");
        strVerison = intent.getStringExtra("versionString");
        strIp = intent.getStringExtra("Ip");
        overridePendingTransition(R.anim.silde_in_down, R.anim.silde_out_down);


        //로그인 정보
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        menuListAdapter = new MenuListAdapter();


        //그리드뷰
        gv = (GridView) findViewById(R.id.gv_main_menu);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MenuActivity.this, ""+i, Toast.LENGTH_SHORT).show();
            }
        });

        //메인
        tvId = (TextView) findViewById(R.id.tv_main_id);
        //tvId.setText(strUserName + "(" + strSabeon + ")");
        tvId.setText(strUserName);

        //Toolbar 설정
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);


        btnWait = findViewById(R.id.btn_wait);
        btnMerger = findViewById(R.id.btn_Merger);
        btnMobf0007 = findViewById(R.id.btn_mobf0007);


        Menu menu =  mNavigationView.getMenu();

        ItemId = menu.findItem(R.id.user_id);
        ItemId.setTitle(strUserId);

        MenuItem ItemName = menu.findItem(R.id.user_name);
        ItemName.setTitle(strUserName);

        //
        MenuItem MOBF0001 = menu.findItem(R.id.MOBF0009);
        MenuItem MOBF0004 = menu.findItem(R.id.MOBF0010);
        MenuItem MOBF0007 = menu.findItem(R.id.MOBF0011);

        MenuItem logOut   = menu.findItem(R.id.log_out);
        MenuItem appOut   = menu.findItem(R.id.app_out);


        MenuItem ItemVerison = menu.findItem(R.id.user_verison);
        ItemVerison.setTitle(strVerison);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // 햄버거 아이콘 활성화

        //매뉴
        DB_Menu_Header dbMenuHeader = new DB_Menu_Header();
        dbMenuHeader.execute();
        DB_Menu_Detail dbMenuDetail = new DB_Menu_Detail();
        dbMenuDetail.execute(strUserId);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_id:
                        break;
                    case R.id.user_name:
                        break;
                 /*   case R.id.MOBF0009:
                        Intent MOBF0001 = new Intent(MenuActivity.this, MatUseRegisterActivity.class);
                        MOBF0001.putExtra("O_USER_ID", strUserId);
                        MOBF0001.putExtra("O_USER_NAME", strUserName);
                        MOBF0001.putExtra("Ip", strIp);
                        MOBF0001.putExtra("TOP_MENU_DESC", btnWait.getText().toString());
                        startActivity(MOBF0001);
                        break;
                   case R.id.MOBF0010:
                        Intent MOBF0004 = new Intent(MenuActivity.this, MatTransferActivity.class);
                        MOBF0004.putExtra("O_USER_ID", strUserId);
                        MOBF0004.putExtra("O_USER_NAME", strUserName);
                        MOBF0004.putExtra("Ip", strIp);
                        MOBF0004.putExtra("TOP_MENU_DESC", btnMerger.getText().toString());
                        startActivity(MOBF0004);
                        break;
                    case R.id.MOBF0011:
                        Intent MOBF0007 = new Intent(MenuActivity.this, MatSearchOnhandActivity.class);
                        MOBF0007.putExtra("O_USER_ID", strUserId);
                        MOBF0007.putExtra("O_USER_NAME", strUserName);
                        MOBF0007.putExtra("Ip", strIp);
                        MOBF0007.putExtra("TOP_MENU_DESC", btnMobf0007.getText().toString());
                        startActivity(MOBF0007);*/

                    default:
                        break;
                }

                return false;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

      /* btnWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMat = new Intent(MenuActivity.this, MatUseRegisterActivity.class);
                intentMat.putExtra("O_USER_ID", strUserId);
                intentMat.putExtra("O_USER_NAME", strUserName);
                intentMat.putExtra("Ip", strIp);
                intentMat.putExtra("TOP_MENU_DESC", btnWait.getText().toString());
                startActivity(intentMat);
            }
        });

        btnMerger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMat = new Intent(MenuActivity.this, MatTransferActivity.class);
                intentMat.putExtra("O_USER_ID", strUserId);
                intentMat.putExtra("O_USER_NAME", strUserName);
                intentMat.putExtra("Ip", strIp);
                intentMat.putExtra("TOP_MENU_DESC", btnMerger.getText().toString());
                startActivity(intentMat);
            }
        });

        btnMobf0007.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMat = new Intent(MenuActivity.this, MatSearchOnhandActivity.class);
                intentMat.putExtra("O_USER_ID", strUserId);
                intentMat.putExtra("O_USER_NAME", strUserName);
                intentMat.putExtra("Ip", strIp);
                intentMat.putExtra("TOP_MENU_DESC", btnMobf0007.getText().toString());
                startActivity(intentMat);
            }
        });
*/
        logOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                alert.setTitle("로그아웃");
                alert.setMessage("로그아웃 하시겠습니까?");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = appData.edit();
                        editor.remove("chkauto");
                        Intent intentHome = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intentHome);
                        finish();
                    }

                });
                alert.show();

                return false;
            }
        });

        appOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                alert.setTitle("종료");
                alert.setMessage("어플리케이션을 종료 하시겠습니까?");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.finishAffinity(MenuActivity.this);
                        System.exit(0);

                    }

                });
                alert.show();

                return false;
            }
        });


        //종합 상황판
      /*  btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  //sendMessage(new JSONArray(arrayListTokens), "h system utility", "test utility", "h system", "h system");
                Intent intentMat = new Intent(MenuActivity.this, TotalMultiScreenActivity.class);
                intentMat.putExtra("Sabeon", strSabeon);
                intentMat.putExtra("UserName", strUserName);
                intentMat.putExtra("Ip", strIp);
                startActivity(intentMat);
            }
        });*/

        //LOG
 /*       btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMat = new Intent(MenuActivity.this, LogActivity.class);
                intentMat.putExtra("Sabeon", strSabeon);
                intentMat.putExtra("UserName", strUserName);
                intentMat.putExtra("Ip", strIp);
                startActivity(intentMat);
            }
        });

        //Token
        btnToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMat = new Intent(MenuActivity.this, TokenActivity.class);
                intentMat.putExtra("Sabeon", strSabeon);
                intentMat.putExtra("UserName", strUserName);
                intentMat.putExtra("Ip", strIp);
                startActivity(intentMat);
            }
        });*/

        //버튼 로그아웃
        tvId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                alert.setTitle("로그아웃");
                alert.setMessage("로그아웃 하시겠습니까?");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = appData.edit();
                        editor.remove("chkauto");
                        editor.remove("pw");
                        editor.commit();
                        Intent intentHome = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intentHome);
                        finish();
                    }

                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                alert.show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected class DB_Menu_Header extends AsyncTask<String, Void, String>
    {
        protected  String doInBackground(String... urls)
        {
            String URL = "http://" + strIp + "/DRK/MenuHeader.jsp"; //자신의 웹서버 주소를 저장합니다.
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


                //tvLog.append();
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

                Menu menu =  mNavigationView.getMenu();

                ItemId = menu.findItem(R.id.user_id);
                ItemId.setTitle(strUserId);

                MenuItem ItemName = menu.findItem(R.id.user_name);

                //
                MenuItem MOBF0001 = menu.findItem(R.id.MOBF0009);
               /* MenuItem MOBF0002 = menu.findItem(R.id.MOBF0002);
                MenuItem MOBF0003 = menu.findItem(R.id.MOBF0003);*/
                MenuItem MOBF0004 = menu.findItem(R.id.MOBF0010);
              /*  MenuItem MOBF0005 = menu.findItem(R.id.MOBF0005);
                MenuItem MOBF0006 = menu.findItem(R.id.MOBF0006);*/
                MenuItem MOBF0007 = menu.findItem(R.id.MOBF0011);
               // MenuItem MOBF0008 = menu.findItem(R.id.MOBF0008);

                for(int i=0; i< arr.length(); i++)
                {
                    JSONObject j_ob = arr.getJSONObject(i);
                    if(j_ob.getString("TOP_MENU_NAME").equals("MOBF0009"))
                    {
                        if(j_ob.getString("TOP_MENU_SHOW_FLAG").equals("Y"))
                        {
                            btnWait.setEnabled(true);
                            btnWait.setText(j_ob.getString("TOP_MENU_DESC"));
                            MOBF0001.setTitle(j_ob.getString("TOP_MENU_DESC"));
                        }
                        else
                        {
                            btnWait.setEnabled(false);
                            btnWait.setText(j_ob.getString("TOP_MENU_DESC"));
                            btnWait.setBackgroundColor(Color.GRAY);
                            MOBF0001.setTitle(j_ob.getString("TOP_MENU_DESC"));
                        }
                    }


                    if(j_ob.getString("TOP_MENU_NAME").equals("MOBF0010"))
                    {
                        if(j_ob.getString("TOP_MENU_SHOW_FLAG").equals("Y"))
                        {
                            btnMerger.setEnabled(true);
                            btnMerger.setText(j_ob.getString("TOP_MENU_DESC"));
                            MOBF0004.setTitle(j_ob.getString("TOP_MENU_DESC"));
                        }
                        else
                        {
                            btnMerger.setEnabled(false);
                            btnMerger.setText(j_ob.getString("TOP_MENU_DESC"));
                            btnMerger.setBackgroundColor(Color.GRAY);
                            MOBF0004.setTitle(j_ob.getString("TOP_MENU_DESC"));
                        }
                    }


                    if(j_ob.getString("TOP_MENU_NAME").equals("MOBF0011"))
                    {
                        if(j_ob.getString("TOP_MENU_SHOW_FLAG").equals("Y"))
                        {
                            btnMobf0007.setText(j_ob.getString("TOP_MENU_DESC"));
                            btnMobf0007.setEnabled(true);
                            MOBF0007.setTitle(j_ob.getString("TOP_MENU_DESC"));
                        }
                        else
                        {
                            btnMobf0007.setEnabled(false);
                            btnMobf0007.setText(j_ob.getString("TOP_MENU_DESC"));
                            btnMobf0007.setBackgroundColor(Color.GRAY);
                            MOBF0007.setTitle(j_ob.getString("TOP_MENU_DESC"));
                        }
                    }



                }

//                for(int i = 0; i < arr.length(); i++) {
//                    JSONObject j_ob = arr.getJSONObject(i);
//                    menuIListtem = new MenuIListtem();
//
//                    menuListAdapter.addItem(j_ob.getString("TOP_MENU_DESC"));
//
//                }

                //gv.setAdapter(menuListAdapter);

//                for(int i = 0; i < arr.length(); i++) {
//                    JSONObject j_ob = arr.getJSONObject(i);
//                    //MENU_NAME[i] = j_ob.getString("TOP_MENU_NAME");
//                    //MENU_PROMPT[i] = j_ob.getString("TOP_MENU_PROMPT");
//                    MENU_DESC[i] = j_ob.getString("TOP_MENU_DESC");
//                    //MENU_SHOW_FLAG[i] = j_ob.getString("TOP_MENU_SHOW_FLAG");
//
//                }

                //MenuButtonAdapter buttonAdapter = new MenuButtonAdapter(getApplicationContext(), MENU_DESC);
                //gv.setAdapter(buttonAdapter);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected class DB_Menu_Detail extends AsyncTask<String, Void, String>
    {
        protected  String doInBackground(String... urls)
        {
            String URL = "http://" + strIp + "/DRK/MenuDetail.jsp"; //자신의 웹서버 주소를 저장합니다.
            DefaultHttpClient client = new DefaultHttpClient();//HttpClient 통신을 합니다.

            try
            {
                HttpPost post = new HttpPost(URL+ "?W_USER_ID=" + urls[0]);

                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                String line = null;
                String result = "";

                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }


                //tvLog.append();
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


                for(int i=0; i< arr.length(); i++)
                {
                    JSONObject j_ob = arr.getJSONObject(i);
                    if(j_ob.getString("TOP_NAME").equals("MOBF0009"))
                    {
                        if(j_ob.getString("AUTHORITY_FLAG").equals("Y"))
                        {

                        }
                        else
                        {
                          //  btnWait.setEnabled(false);
                           // btnWait.setBackgroundColor(Color.GRAY);
                        }
                    }

                    if(j_ob.getString("TOP_NAME").equals("MOBF0010"))
                    {
                        if(j_ob.getString("AUTHORITY_FLAG").equals("Y"))
                        {
                        }
                        else
                        {
                            //btnMerger.setEnabled(false);
                           // btnMerger.setBackgroundColor(Color.GRAY);
                        }
                    }


                    if(j_ob.getString("TOP_NAME").equals("MOBF0011"))
                    {
                        if(j_ob.getString("AUTHORITY_FLAG").equals("Y"))
                        {
                        }
                        else
                        {
                           // btnMobf0007.setEnabled(false);
                           // btnMobf0007.setBackgroundColor(Color.GRAY);
                        }
                    }

                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
