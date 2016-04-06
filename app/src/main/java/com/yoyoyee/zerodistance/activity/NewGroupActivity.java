package com.yoyoyee.zerodistance.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.*;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;



import java.io.File;
import java.lang.System;
import java.util.Calendar;


public class NewGroupActivity extends AppCompatActivity {

    /*------------------------------------------Tree命名規則---------------------------------------------
    原名為newGroupActivity，縮寫newGA，並以此為開頭編寫變數
    newGAPress為為連結spinnerNewGroupActPressing(緊急程度)






     -------------------------------------------------------------------------------------------------------------*/

    private ArrayAdapter<String> adapterPress,adapterPay;
    private Boolean press;//緊急程度 false是不僅緊急，true是緊急
    private Boolean oneTimesDate =true,oneTimesTime=true;//第一次進行時間日期設定判斷用的，用以顯示提示吐司
    private Boolean allreadyDate=false,allreadyTime=false;//是否有選取時間和日期
    private Boolean firstTakePicture=true;//是否第一次拍照
    private Boolean time12or24=false; //設定true為24小時制，false12小時制
    private Boolean timeAMPMAuto=false;//設定為true時為自動偵測系統時間，fales時為手動設定12或是24小時制
    private Boolean PICTURE_GONE=true;//隱藏拍照功能

    private Button buttonDate,buttonTime,buttonOk,buttonCancel,buttonPicture,buttonTakePicture;
    private Bitmap bitmapOutPut;//要輸出出去的原圖
    private Layout cc;

    private Calendar calendar;


    private EditText editTextcontent,editTextOtherPay,editTextName,editTextNumber;

    private ImageView imv;
    private Spinner spinnerPress, spinnerPay;
    private String[] stringPress ,stringPay;
    private String getThatString;
    private String getPay;

    private TextView textViewName,textViewPress,textViewPay,Display,Display2,textViewGroupDate, textViewcontent,textViewPicture,textViewPeopleNumber;
    private TextView textViewTime,textViewDate;//Timepickerdialog使用
    private Toolbar toolbar;
    private Uri uriImg;

    final int theme = 5; //TimePickerDialog的主題，有0~6;
    final int requireCodefromSdcard=101,requireCodefromCamara=100;

    private int yearNow, monthNow, dayNow, hourNow, minuteNow,pmamNow;
    private int year, month, day, hour, minute;
    private int hourAMPM;
    private int pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        //FindByID區--------------------------------------------------------------------------------
        //toolbar 定位區
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //textView 定位區
        Display = (TextView) findViewById(R.id.printf);
        Display2= (TextView) findViewById(R.id.printf2);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewGroupDate = (TextView) findViewById(R.id.textViewGroupDate);
        textViewcontent = (TextView) findViewById(R.id.textViewContent);
        textViewPicture= (TextView) findViewById(R.id.textViewPicture);
        textViewPeopleNumber= (TextView) findViewById(R.id.textViewPeopleNumber);
        //editText定位區
        editTextName= (EditText) findViewById(R.id.editTextName);
        editTextcontent = (EditText) findViewById(R.id.editTextContent);
        editTextNumber = (EditText) findViewById(R.id.editTextPeopleNumber);
        //buttom 定位區
        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonTime = (Button) findViewById(R.id.buttonTime);
        buttonCancel= (Button) findViewById(R.id.buttonCancel);
        buttonOk= (Button) findViewById(R.id.buttonOK);
        buttonPicture= (Button) findViewById(R.id.buttonPicture);
        buttonTakePicture= (Button) findViewById(R.id.buttonTakePicture);

        //spinner 定位區
        spinnerPress = (Spinner) findViewById(R.id.spinnerPress);


        //ImageView
        imv =(ImageView)findViewById(R.id.imageViewPicture);


        //ActionBar 設定區，主要為為了toolbar使用---------------------------------------------------
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.actionbar_new_mission);
        //定義區------------------------------------------------------------------------------------
        //時間F

        calendar =Calendar.getInstance();

        stringPress =getResources().getStringArray(R.array.press_new_mission_and_group);

        //取的時間設定12或24小時制
        //設定文字
/*
        textViewName.setText(R.string.missionname_new_mission);
        textViewPay.setText(R.string.pay_new_mission);
        textViewPicture.setText(R.string.uploadpicture_new_mission_and_group);
        textViewcontent.setText(R.string.content_new_mission);
        textViewGroupDate.setText(R.string.date_new_mission);
        textViewPeopleNumber.setText(R.string.peoplenumber_new_mission);
        buttonTime.setText(R.string.buttomtime_new_mission_and_group);
        buttonDate.setText(R.string.buttomdate_new_mission_and_group);
        buttonPicture.setText(R.string.uploadpictruebuttom_new_mission_and_group);
        buttonTakePicture.setText(R.string.takepicturebuttom_new_mission_and_group);*/

        Display2.setText(Integer.toString(Build.VERSION.SDK_INT));


        imv.setVisibility(View.GONE);

        adapterPress = new ArrayAdapter<String>(this, R.layout.spinner,getResources().getStringArray(R.array.press_new_mission_and_group));
        adapterPress.setDropDownViewResource(R.layout.spinner);
        spinnerPress.setAdapter(adapterPress);



        //spinner的監聽
        //監聽緊急程度
        spinnerPress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Display.setText(stringPress[position]);
                press =(position==0)? false: true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //監聽獎勵


        //圖片監聽
        //經由長按進行取消選取圖片動作
        imv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imv.setVisibility(View.GONE);
                return true;
            }
        });
        //短按進行圖片放大瀏覽動作(待處理
        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        //此區為隱藏功能用--------------------------------------------------------------------------
        if(PICTURE_GONE) {
            textViewPicture.setVisibility(View.GONE);
            buttonPicture.setVisibility(View.GONE);
            buttonTakePicture.setVisibility(View.GONE);
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // TimePickerDialog設置彈出視窗-----------------------------------------------------------------
    //按鈕選擇日期
    public void onClickDate(View v) {
        if (oneTimesDate == true) {
            yearNow = calendar.get(Calendar.YEAR);
            monthNow =  calendar.get(Calendar.MONTH);
            dayNow =  calendar.get(Calendar.DAY_OF_MONTH);
            year =yearNow;
            month =monthNow;
            day =dayNow;
            Toast.makeText(this,"開始設定", Toast.LENGTH_SHORT).show();
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, theme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int lisYear, int lisMonth, int lisDay) {
                year = lisYear;
                month = lisMonth;
                day = lisDay;
                allreadyDate =true;
                buttonDate.setText(year + getResources().getString(R.string.year_new_mission_and_group) + (month+1) + getResources().getString(R.string.month_new_mission_and_group) + day + getResources().getString(R.string.day_new_mission_and_group));
            }
        }, year, month, day);
        datePickerDialog.show();

        // Display.setText(String.valueOf(year));
        oneTimesDate =false;
    }
    //按鈕選擇時間
    public void onClickTime(View v) {
        if (oneTimesTime == true) {
            hourNow =calendar.get(Calendar.HOUR_OF_DAY);
            minuteNow =calendar.get(Calendar.MINUTE);
            hour =hourNow;
            minute =minuteNow;
            // Toast.makeText(this,"開始設定", Toast.LENGTH_SHORT).show();
        }

        if (timeAMPMAuto==true) {//自動偵測系統設定的時間格式為12小時制或24小時制
            ContentResolver cv = this.getContentResolver();
            String timeAMPM = Settings.System.getString(cv, Settings.System.TIME_12_24);
            if (timeAMPM.equals("12")) {
                time12or24 = false;
            }
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, theme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int lisHour, int lisminute) {
                String tempAMPM;
                hour = lisHour;
                minute = lisminute;
                allreadyTime =true;
                if (hour>=12 && hour<24){
                    if (hour>12) {
                        tempAMPM = new String(getResources().getString(R.string.PM_new_mission_and_group));
                        lisHour-=12;
                    }
                    else {
                        tempAMPM = new String(getResources().getString(R.string.PM_new_mission_and_group));
                    }
                }
                else{
                    tempAMPM =new String(getResources().getString(R.string.AM_new_mission_and_group));
                }
                buttonTime.setText(lisHour + getResources().getString(R.string.hour_new_mission_and_group) + minute + getResources().getString(R.string.minute_new_mission_and_group)+" "+tempAMPM);
            }
        }, hour, minute, time12or24);
        timePickerDialog.show();
        oneTimesTime =false;
    }
    //圖片控制區------------------------------------------------------------------------------------
    //按鈕開啟相機，但要先取得相機權限
    public void useCamera(View v){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) { //if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE權限，
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) == false) {
                    showMessageOKCancel(getResources().getString(R.string.restorereadpermission_new_mission_and_group), new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requireCodefromCamara);
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        openCamara();
                    }
                }
            }
            else {
                openCamara();
            }
        }
        else{
            openCamara();
        }
    }
    //按鈕選取圖片
    public void onClickPickimg(View v){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) { //if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE權限，
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel(getResources().getString(R.string.restorereadpermission_new_mission_and_group), new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requireCodefromSdcard);
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Pickimg();
                    }
                }
            }
            else
                Pickimg();
        }
        else{
            Pickimg();
        }
    }
    //顯示彈出視窗時的建立程式碼
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(NewGroupActivity.this)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.okbuttom_new_mission_and_group), okListener)
                .setNegativeButton(getResources().getString(R.string.cancelbuttom_new_mission_and_group), null)
                .create()
                .show();
    }
    //忘了，待處理
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }
    //從檔案讀取圖片(由onClickPickimg進行呼叫)
    public void Pickimg(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requireCodefromSdcard);
        }
        Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requireCodefromSdcard);
    }
    //開啟相機(由useCamara進行呼叫)
    private void openCamara(){
        Intent camera =new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String fname ="p"+ System.currentTimeMillis() +".jpg";
        uriImg =Uri.parse("file://" + dir + "/" +fname);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uriImg);
        startActivityForResult(camera, requireCodefromCamara);

       /* File tmpFile = new File(Environment.getExternalStorageDirectory(),"image.jpg");

        Uri outputFileUri = Uri.fromFile(tmpFile);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);*/
    }
    //傳回並顯示圖片，從開啟相機和從檔案讀取圖片(onActivityResult的選擇執行對作)
    protected void onActivityResult(int requireCode,int resultCode,Intent data){
        super.onActivityResult(requireCode, resultCode, data);
        switch (requireCode) {
            case requireCodefromCamara: {
                if (resultCode == Activity.RESULT_OK ) {
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    //option.inJustDecodeBounds =true;//只讀圖檔資訊
                    option.inSampleSize = 1;//設定縮小倍率，2為1/2倍
                    Bitmap bitmap = BitmapFactory.decodeFile(uriImg.getPath(), option); //讀取圖檔資訊，存入option中，已進行修改

                    // Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmapOutPut, bitmapOutPut.getWidth()/5, bitmapOutPut.getHeight()/5); //圖片壓縮

                    imv.setVisibility(View.VISIBLE);
                    imv.setImageBitmap(bitmap);
                    if (firstTakePicture == true) {
                        Toast.makeText(this, R.string.takepicturemessage_new_mission_and_group, Toast.LENGTH_LONG).show();
                    }
                    firstTakePicture = false;
                }
                else {
                    Toast.makeText(this, R.string.notakepicture_new_mission_and_group, Toast.LENGTH_SHORT).show();
                }
            }
            case requireCodefromSdcard: {
                if (resultCode == Activity.RESULT_OK ) {
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    //option.inJustDecodeBounds =true;//只讀圖檔資訊
                    option.inSampleSize = 1;//設定縮小倍率，2為1/2倍
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg", option); //讀取圖檔資訊，存入option中，已進行修改

                    // Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmapOutPut, bitmapOutPut.getWidth()/5, bitmapOutPut.getHeight()/5); //圖片壓縮

                    imv.setVisibility(View.VISIBLE);
                    imv.setImageBitmap(bitmap);
                    if (firstTakePicture == true) {
                        Toast.makeText(this, R.string.takepicturemessage_new_mission_and_group, Toast.LENGTH_LONG).show();
                    }
                    firstTakePicture = false;
                }
                else {
                    Toast.makeText(this, R.string.notakepicture_new_mission_and_group, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
/*
    //按完成鈕送出
    //防止空白資訊
    public void onClickOkOutputData(View v){
        if (editTextName.getText().toString().trim().equals("")) {
            Toast.makeText(this, R.string.errornoname_new_mission, Toast.LENGTH_SHORT).show();
        }
        else{
            missionData.setTitle(editTextName.getText().toString());
            if ( editTextNumber.getText().toString().trim().equals("")){
                Toast.makeText(this, R.string.errorpeoplenumber_new_mission_and_group, Toast.LENGTH_SHORT).show();
            }
            else {
                missionData.needNum=Integer.parseInt(editTextNumber.getText().toString());
                if (allreadyDate == false) {
                    Toast.makeText(this, R.string.errornoDate_new_mission_and_group, Toast.LENGTH_SHORT).show();
                }
                else {
                    //待添加DATE
                    if (allreadyTime == false) {
                        Toast.makeText(this, R.string.errornoTime_new_mission_and_group, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //待添加TIME
                        if (editTextcontent.getText().toString().trim().equals("")) {
                            Toast.makeText(this, R.string.errornocontent_new_mission, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            missionData.content = editTextcontent.getText().toString();
                            calendar.set(year,month,day,hour,minute);
                            ClientFunctions.publishMission(
                                    editTextName.getText().toString(),
                                    press,
                                    Integer.valueOf(editTextNumber.getText().toString()),
                                    editTextcontent.getText().toString(),
                                    getPay,
                                    calendar.getTime(),
                                    new ClientResponse() {
                                        @Override
                                        public void onResponse(String response) {

                                        }

                                        @Override
                                        public void onErrorResponse(String response) {

                                        }
                                    });
                            this.finish();
                        }
                    }
                }
            }
        }
    }
    //按取消鈕返回
    public void onClickCancel(View v){


    }*/

}