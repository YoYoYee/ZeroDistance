package com.yoyoyee.zerodistance.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.CustomToast;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.SessionManager;
import com.yoyoyee.zerodistance.helper.SlidingTabLayout;
import com.yoyoyee.zerodistance.helper.ViewPagerAdapter;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;
    public static Context context;
    ViewPagerAdapter adapter;
    Toolbar tool_bar;
    ViewPager pager;
    SlidingTabLayout tabs;
    private ProgressDialog pDialog;
    public static boolean PD=false;
    private SQLiteHandler db;
    String Titles[];
    int upDataCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Titles = getResources().getStringArray(R.array.tabstyle);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);

        pDialog.setMessage("載入中 ...");
        context = this;

        //更新手機資料庫
        updataphoneDB();
        //更新資料


//設置toolbar標題


//        LayoutInflater layout=this.getLayoutInflater();
//        View view=layout.inflate(R.layout.maintool_bar, null);
        tool_bar = (Toolbar)findViewById(R.id.tool_bar);
        TextView UserNames = (TextView)tool_bar.findViewById(R.id.UserName); //  tool_bar.findViewById(R.id.UserName) 會null
        TextView UserSchool = (TextView)tool_bar.findViewById(R.id.UserSchool);
        UserNames.setText(SessionFunctions.getUserNickName()+"");
        UserSchool.setText(SessionFunctions.getUserSchoolID()+"");
        setSupportActionBar(tool_bar);
        ActionBar actionBar = getSupportActionBar();

        //actionBar.setTitle(SessionFunctions.getUserNickName()+"     我塞塞塞塞塞塞塞塞塞");
//設置toolbar標題

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.container);


        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width


        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.ColorPrimaryToolbar);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout



    }
    protected void onStart(){
        super.onStart();

    }

    /**
     * A placeholder fragment containing a simple view.
     */


    //設置字體大小
    private void setFontSize(){
        TextView textViewTemp;
        SessionFunctions SF= new SessionFunctions();

    }
    private void showDialog() {
        if (!pDialog.isShowing()){
            pDialog.show();
            PD=true;
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            PD=false;
        }
    }
    private void delphoneDB(){
        QueryFunctions.deleteAllMissions();
        QueryFunctions.deleteAllGroups();
    }

    private void updataphoneDB(){//更新手機資料
        delphoneDB();//資料都先削掉
        showDialog();
        //fabtime();
        updataMissionDB(); //成功會更新Group

    }
    private void updataMissionDB(){  //成功會更新Group
        ClientFunctions.updateMissions(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                db = AppController.getDB();
                String TAG = AppController.class.getSimpleName();
                ArrayList<Mission> missions = db.getMissions();
                if (missions.size() > 0) {
                    Log.d(TAG, "onResponse: " + missions.get(0).getTitle() + " " + missions.get(0).createdAt + " " + missions.get(0).finishedAt);
                }
                updataGroupDB();//更新揪團
                upDataCount=0;
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=5){
                CustomToast.showToast(context, "更新失敗ON", 1500);
            }else{
                upDataCount+=1;
                    updataMissionDB();
            }
            }
        });
    }
    private void updataGroupDB(){
        ClientFunctions.updateGroups(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                SQLiteHandler db = AppController.getDB();
                String TAG = AppController.class.getSimpleName();
                ArrayList<Group> Group = db.getGroups();
                if (Group.size() > 0) {
                    Log.d(TAG, "onResponse: " + Group.get(0).getTitle() + " " + Group.get(0).createdAt + " " + Group.get(0).finishedAt);
                }
                MakeTabAndContext();
                hideDialog();
            }

            @Override
            public void onErrorResponse(String response) {
                //  Toast.makeText(context, "更新失敗", Toast.LENGTH_SHORT).show();
                hideDialog();
                if(upDataCount>=5){
                    CustomToast.showToast(context, "更新失敗ON", 1500);
                }else{
                    upDataCount+=1;
                    updataGroupDB();
                }
            }
        });
    }

    private void MakeTabAndContext(){
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Titles.length);
        pager.setOffscreenPageLimit(6);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }
    public void fabtime(){
        final int[] i = {0};
        new CountDownTimer(5000,200){

            @Override
            public void onFinish() {
                if(pDialog.isShowing()){
                   // pDialog.dismiss();
                    CustomToast.showToast(context, "關閉pDialog"+i[0], 200);
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                i[0] +=1;
              //  CustomToast.showToast(context, ""+i[0], 200);
            }

        }.start();
    }

}
