package com.yoyoyee.zerodistance.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.app.FontManager;
import com.yoyoyee.zerodistance.app.TapService;
import com.yoyoyee.zerodistance.app.TextLenghLimiter;
import com.yoyoyee.zerodistance.app.TextNextLineLimiter;
import com.yoyoyee.zerodistance.app.TimeBroadcastReceiver;
import com.yoyoyee.zerodistance.app.UpdataFunction;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.Permission;
import com.yoyoyee.zerodistance.helper.datatype.QA;


import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class NewMissionActivity extends AppCompatActivity {

    private static final String TAG = "NewMission";
    /*------------------------------------------Tree命名規則---------------------------------------------
        原名為newGroupActivity，縮寫newGA，並以此為開頭編寫變數
        newGAPress為為連結spinnerNewGroupActPressing(緊急程度)






         -------------------------------------------------------------------------------------------------------------*/
    private ArrayAdapter<String> adapterPress,adapterPay,adapterVirtue;
    private Boolean press;//緊急程度 false是不僅緊急，true是緊急
    private Boolean oneTimesDate =true,oneTimesTime=true;//第一次進行時間日期設定判斷用的，用以顯示提示吐司
    private Boolean allreadyDate=false,allreadyTime=false;//是否有選取時間和日期
    private Boolean firstTakePicture=true;//是否第一次拍照
    private Boolean time12or24=false; //設定true為24小時制，false12小時制
    private Boolean timeAMPMAuto=false;//設定為true時為自動偵測系統時間，fales時為手動設定12或是24小時制
    private Boolean PICTURE_GONE=false;//隱藏拍照功能
    private Boolean isOtherPay=false;//獎勵是否為其他
    private Boolean isEdit=false;
    private Boolean savePicture=false;//設定：照片是否要存檔

    private Button buttonDate,buttonTime,buttonOk,buttonCancel,buttonPicture,buttonTakePicture;
    private Bitmap bitmapOutPut;//要輸出出去的原圖
    private Layout cc;

    private Calendar calendar;


    private EditText editTextcontent,editTextOtherPay,editTextName,editTextNumber,editTextWhere;

    private ImageView imv;
    private Spinner spinnerPress, spinnerPay,spinnerVirtue;
    private String[] stringPress ,stringPay;
    private String picturePath;
    private String getPay;
    private String dir,fname;


    private TextView textViewName,textViewPress,textViewPay,Display,Display2,textViewMissionDate, textViewcontent,textViewPicture,textViewPeopleNumber,textViewWhere,textViewVirtue,textPeople;
    private TextView textViewTime,textViewDate;//Timepickerdialog使用
    private Toolbar toolbar;
    private Uri uriImg=null;

    final int theme = 5; //TimePickerDialog的主題，有0~6;
    final int REQUIRE_SDCARD=101,REQUIRE_CAMARA=100;

    private int yearNow, monthNow, dayNow, hourNow, minuteNow,pmamNow;
    private int year, month, day, hour, minute;
    int strV,intV,agiV;
    private int missionID;
    private int hourAMPM;
    private int pay;
    private int virtue=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mission);

        //FindByID區--------------------------------------------------------------------------------
        //toolbar 定位區
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //textView 定位區
        Display = (TextView) findViewById(R.id.printf);
        Display2= (TextView) findViewById(R.id.printf2);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewPress = (TextView) findViewById(R.id.textViewPress);
        textViewPay = (TextView) findViewById(R.id.textViewPay);
        textViewMissionDate = (TextView) findViewById(R.id.textViewMissionDate);
        textViewcontent = (TextView) findViewById(R.id.textViewContent);
        textViewPicture= (TextView) findViewById(R.id.textViewPicture);
        textViewPeopleNumber= (TextView) findViewById(R.id.textViewPeopleNumber);
        textViewWhere= (TextView) findViewById(R.id.textViewWhere);
        textViewVirtue=(TextView) findViewById(R.id.textViewVirtue);
        textPeople= (TextView) findViewById(R.id.people_Mission);

        //editText定位區
        editTextName= (EditText) findViewById(R.id.editTextName);
        editTextcontent = (EditText) findViewById(R.id.editTextContent);
        editTextOtherPay = (EditText) findViewById(R.id.editTextOtherPay);
        editTextNumber = (EditText) findViewById(R.id.editTextPeopleNumber);
        editTextWhere = (EditText) findViewById(R.id.editTextWhere);        //buttom 定位區
        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonTime = (Button) findViewById(R.id.buttonTime);
        buttonCancel= (Button) findViewById(R.id.buttonCancel);
        buttonOk= (Button) findViewById(R.id.buttonOK);
        buttonPicture= (Button) findViewById(R.id.buttonPicture);
        buttonTakePicture= (Button) findViewById(R.id.buttonTakePicture);
        //spinner 定位區
        spinnerPress = (Spinner) findViewById(R.id.spinnerPress);
        spinnerPay = (Spinner) findViewById(R.id.spinnerPay);
        spinnerVirtue= (Spinner) findViewById(R.id.spinnerVirtue);
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
        stringPay =getResources().getStringArray(R.array.pay_new_mission);
        //取的時間設定12或24小時制
        //設定文字
        textViewName.setText(R.string.missionname_new_mission);
        textViewPress.setText(R.string.press_new_mission_and_group);
        textViewPay.setText(R.string.pay_new_mission);
        textViewPicture.setText(R.string.uploadpicture_new_mission_and_group);
        textViewcontent.setText(R.string.content_new_mission);
        textViewMissionDate.setText(R.string.date_new_mission);
        textViewPeopleNumber.setText(R.string.peoplenumber_new_mission_and_group);
        buttonTime.setText(R.string.buttomtime_new_mission_and_group);
        buttonDate.setText(R.string.buttomdate_new_mission_and_group);
        buttonPicture.setText(R.string.uploadpictruebuttom_new_mission_and_group);
        buttonTakePicture.setText(R.string.takepicturebuttom_new_mission_and_group);
        Display2.setText(Integer.toString(Build.VERSION.SDK_INT));
        editTextOtherPay.setVisibility(View.GONE);
        imv.setVisibility(View.GONE);

        adapterPress = new ArrayAdapter<String>(this, R.layout.spinner,getResources().getStringArray(R.array.press_new_mission_and_group));
        adapterPay = new ArrayAdapter<String>(this, R.layout.spinner,getResources().getStringArray(R.array.pay_new_mission));
        adapterVirtue =new ArrayAdapter<String>(this,R.layout.spinner,getResources().getStringArray(R.array.virtue_new_mission));
        adapterPress.setDropDownViewResource(R.layout.spinner);
        adapterPay.setDropDownViewResource(R.layout.spinner);
        adapterVirtue.setDropDownViewResource(R.layout.spinner);
        spinnerPress.setAdapter(adapterPress);
        spinnerPay.setAdapter(adapterPay);
        spinnerVirtue.setAdapter(adapterVirtue);

        limitLong();//名稱字數限制以及字型大小限制
        allTextSize(SessionFunctions.getUserTextSize());
        isEdit();
        imvListener();

        final Button serviceStart,serviceStop;
        serviceStart= (Button) findViewById(R.id.serviceStart);
        serviceStop= (Button)findViewById(R.id.serviceStop);
        //---隱藏測試用
        Boolean test=false;
        if (test==false) {
            serviceStart.setVisibility(View.GONE);
            serviceStop.setVisibility(View.GONE);
        }
        serviceStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent (NewMissionActivity.this,TapService.class);
                startService(intent);
                Intent intentUpdata = new Intent("com.yoyoyee.MY_BROADCAST_RECEIVAR");
                sendBroadcast(intentUpdata);
                Log.d("BOARDC","start");

            }
        });
        serviceStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent (NewMissionActivity.this,TapService.class);
                stopService(intent);
                Toast.makeText(NewMissionActivity.this,"STOP",Toast.LENGTH_SHORT).show();
                UpdataFunction updataFunction =new UpdataFunction();
                updataFunction.showUpdataDialog(NewMissionActivity.this);


            }
        });


        //edit的監聽

        //spinner的監聽
        //監聽緊急程度
        spinnerPress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                press = (position == 0) ? false : true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //監聽獎勵
        spinnerPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == (getResources().getStringArray(R.array.pay_new_mission).length - 1)) {
                    editTextOtherPay.setVisibility(View.VISIBLE);
                    isOtherPay = true;
                } else {
                    getPay = stringPay[position];
                    editTextOtherPay.setVisibility(View.GONE);
                    isOtherPay = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerVirtue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                virtue=position;
                Toast.makeText(getApplicationContext(),String.valueOf(virtue),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                virtue=0;
            }
        });

                toNotificationClass();
        //此區為隱藏功能用--------------------------------------------------------------------------
        //hide(PICTURE_GONE);
    }

    void toNotificationClass (){
        Intent intent = getIntent();
        Boolean isNotification = intent.getBooleanExtra("notificationModel",false);
        if (isNotification==true){
            Intent intentNotification =new Intent(this,MainActivity.class);
            startActivity(intentNotification);
        }
    }

    public void hide(Boolean PICTURE_GONE){
        if(PICTURE_GONE) {
            textViewPicture.setVisibility(View.GONE);
            buttonPicture.setVisibility(View.GONE);
            buttonTakePicture.setVisibility(View.GONE);
        }
    }
    public void imvListener(){//尚未啟用 有5.0版本問題
        //圖片監聽
        //經由長按進行取消選取圖片動作

        imv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imv.setVisibility(View.GONE);
                uriImg=Uri.parse("null");
                return true;
            }
        });
        //短按進行圖片放大瀏覽動作(待處理
        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void isEdit(){
        Intent intent=getIntent();
        isEdit=intent.getBooleanExtra("isEdit",false);
        missionID =intent.getIntExtra("id",0);
        if (isEdit){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.actionbar_edit_mission);
            Mission mission =QueryFunctions.getMission(missionID);
            SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmm");
            Date date=mission.getExpAt();
            String dateString =format.format(date);
            year=Integer.valueOf(dateString.substring(0,4));
            month=Integer.valueOf(dateString.substring(4,6));
            day=Integer.valueOf(dateString.substring(6,8));
            hour=Integer.valueOf(dateString.substring(8,10));
            minute=Integer.valueOf(dateString.substring(10,12));
            strV=mission.strength;
            agiV=mission.agile;
            intV=mission.intelligence;
            String tempAMPM;
            if (hour>=12 && hour<24){
                if (hour>12) {
                    tempAMPM = new String(getResources().getString(R.string.PM_new_mission_and_group));
                    hour-=12;
                }
                else {
                    tempAMPM = new String(getResources().getString(R.string.PM_new_mission_and_group));
                }
            }
            else{
                tempAMPM =new String(getResources().getString(R.string.AM_new_mission_and_group));
            }
            buttonTime.setText(hour + getResources().getString(R.string.hour_new_mission_and_group) + minute + getResources().getString(R.string.minute_new_mission_and_group)+" "+tempAMPM);
            buttonDate.setText(year + getResources().getString(R.string.year_new_mission_and_group) + (month+1) + getResources().getString(R.string.month_new_mission_and_group) + day + getResources().getString(R.string.day_new_mission_and_group));
            allreadyDate=true;
            allreadyTime=true;
            editTextName.setText(mission.getTitle());
            editTextWhere.setText(mission.getPlace());
            editTextNumber.setText(String.valueOf(mission.getNeedNum()));
            editTextcontent.setText(mission.getContent());

            oneTimesDate=false;
            oneTimesTime=false;
            press=mission.isUrgent;
            spinnerPress.setSelection((mission.isUrgent) ? 1 : 0);
            for (int i=0;i<stringPay.length;i++){
               if (stringPay[i].equals(mission.getReward()))
               {
                   spinnerPay.setSelection(i);
                   break;
               }
                else if(i==stringPay.length-1){
                   spinnerPay.setSelection(i);
                   spinnerPay.setVisibility(View.VISIBLE);
                   editTextOtherPay.setText(mission.getReward());
               }
            }
            if (strV==3){
                virtue=1;
            }
            else if(intV==3){
                virtue=2;
            }
            else if(agiV==3){
                virtue=3;
            }
            else{
                virtue=0;
            }
            Toast.makeText(getApplicationContext(),String.valueOf(strV)+"---"+String.valueOf(intV)+"---"+String.valueOf(agiV)+"---"+String.valueOf(missionID),Toast.LENGTH_SHORT).show();
            spinnerVirtue.setSelection(virtue);



        }
    }

    public void  limitLong (){
        editTextName.addTextChangedListener(new TextLenghLimiter(60));
        editTextWhere.addTextChangedListener(new TextLenghLimiter(60));
        editTextNumber.addTextChangedListener(new TextLenghLimiter(60));
        editTextName.addTextChangedListener(new TextNextLineLimiter());
    }

    public void allTextSize(float size){
        textViewName.setTextSize(size+5);
        textViewPress.setTextSize(size+5);
        textViewPay.setTextSize(size+5);
        textViewPicture.setTextSize(size+5);
        textViewcontent.setTextSize(size+5);
        textViewMissionDate.setTextSize(size+5);
        textViewPeopleNumber.setTextSize(size+5);
        textViewVirtue.setTextSize(size+5);
        textPeople.setTextSize(size+5);
        buttonTime.setTextSize(size);
        buttonDate.setTextSize(size);
        buttonPicture.setTextSize(size);
        buttonTakePicture.setTextSize(size);
        textViewWhere.setTextSize(size+5);
        buttonOk.setTextSize(size);
        buttonCancel.setTextSize(size);
        buttonTime.setTextSize(size);
        buttonDate.setTextSize(size);
        //FontManager.changeFonts((ViewGroup) this.findViewById(android.R.id.content),NewMissionActivity.this);

        /*Typeface face =Typeface.createFromAsset(this.getAssets(),"setofont.ttf");
        textViewName.setTypeface(face);
        textViewPress.setTypeface(face);
        textViewPay.setTypeface(face);
        textViewPicture.setTypeface(face);
        textViewcontent.setTypeface(face);
        textViewMissionDate.setTypeface(face);
        textViewPeopleNumber.setTypeface(face);
        textViewVirtue.setTypeface(face);
        textPeople.setTypeface(face);
        buttonTime.setTypeface(face);
        buttonDate.setTypeface(face);
        buttonPicture.setTypeface(face);
        buttonTakePicture.setTypeface(face);
        textViewWhere.setTypeface(face);
        buttonOk.setTypeface(face);
        buttonCancel.setTypeface(face);
        buttonTime.setTypeface(face);
        buttonDate.setTypeface(face);*/

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permission.writeStorage(this,this);
            Log.d("useCamera", "useCamera: "+String.valueOf(shouldShowRequestPermissionRationale(Permission.WRITE_PERMISSION) == true));
            if(checkSelfPermission(Permission.WRITE_PERMISSION)==PackageManager.PERMISSION_GRANTED){
                openCamara();
            }
        }
        else{
            openCamara();
        }

    }
    //按鈕選取圖片
    public void onClickPickimg(View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permission.writeStorage(this,this);
            Log.d(TAG, "useCamera: "+String.valueOf(shouldShowRequestPermissionRationale(Permission.WRITE_PERMISSION) == true));
            if(checkSelfPermission(Permission.WRITE_PERMISSION)==PackageManager.PERMISSION_GRANTED){
                Pickimg();
            }
        }
        else{
            Pickimg();
        }
    }
    //從檔案讀取圖片(由onClickPickimg進行呼叫)
    public void Pickimg(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,REQUIRE_SDCARD);
       // }/
    }
    //開啟相機(由useCamara進行呼叫)
    private void openCamara(){
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        fname = "Temp_Picture.jpg";
        uriImg =Uri.parse( "file://" + dir + "/"+ fname);
        picturePath=dir + "/"+ fname;
      //  Toast.makeText(this, String.valueOf(uriImg), Toast.LENGTH_SHORT).show();
        Intent camera =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uriImg);
        startActivityForResult(camera, REQUIRE_CAMARA);

    }
    //傳回並顯示圖片，從開啟相機和從檔案讀取圖片(onActivityResult的選擇執行對作)
    protected void onActivityResult(int requireCode,int resultCode,Intent data){
        super.onActivityResult(requireCode, resultCode, data);
        switch (requireCode) {
            case REQUIRE_CAMARA:
                if (resultCode == Activity.RESULT_OK ) {
                    DisplayMetrics phoneSize = new DisplayMetrics();//取得手機螢幕解析度
                    getWindowManager().getDefaultDisplay().getMetrics(phoneSize);
                    int phonewidth =phoneSize.widthPixels;
                    int phoneheight =phoneSize.heightPixels;
                    // int phoneWidth=phoneSize.widthPixels;讀取手機螢幕寬度
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inJustDecodeBounds = true;
                    Bitmap bmp = BitmapFactory.decodeFile(uriImg.getPath(), option);
                    int pictureWidth = option.outWidth;
                    int pictureHetght =option.outHeight;
                    int math =pictureWidth/phonewidth;
                    Log.d("", "onActivityResult: "+String.valueOf(math));
                   // option.inJustDecodeBounds =true;//只讀圖檔資訊
                    final BitmapFactory.Options optionT = new BitmapFactory.Options();
                    optionT.inJustDecodeBounds = false;
                    optionT.inSampleSize=math;
                    Toast.makeText(this,"寬"+String.valueOf(pictureHetght)+"高"+String.valueOf(pictureWidth)+"經計算"+String.valueOf( math),Toast.LENGTH_LONG).show();

                    // Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmapOutPut, bitmapOutPut.getWidth()/5, bitmapOutPut.getHeight()/5); //圖片壓縮

                    final Bitmap bitmap =BitmapFactory.decodeFile(uriImg.getPath(), optionT); //讀取圖檔資訊，存入option中，已進行修改
                   /* if (pictureHetght>pictureWidth){
                        Matrix vMatrix = new Matrix();
                        vMatrix.setRotate( 45 );
                        bitmap =Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),vMatrix,true);
                    }*/
                    try {
                        File file = new File(dir, fname); //存壓縮過後的檔案
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        out.flush();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
                break;
            case REQUIRE_SDCARD:
                if (resultCode == Activity.RESULT_OK ) {
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    //option.inJustDecodeBounds =true;//只讀圖檔資訊
                    option.inSampleSize = 1;//設定縮小倍率，2為1/2倍
                    uriImg = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(uriImg,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath , option); //讀取圖檔資訊，存入option中，已進行修改
                   // Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();
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
                break;
        }
    }
    //按完成鈕送出
    //防止空白資訊
    public void onClickOkOutputData(final View v){
        if (editTextName.getText().toString().trim().equals("")) {
            Toast.makeText(this, R.string.errornoname_new_mission, Toast.LENGTH_SHORT).show();
        } else {
            // missionData.setTitle(editTextName.getText().toString());
            if (editTextNumber.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.errorpeoplenumber_new_mission_and_group, Toast.LENGTH_SHORT).show();
            } else {
                //   missionData.needNum=Integer.parseInt(editTextNumber.getText().toString());
                if (allreadyDate == false) {
                    Toast.makeText(this, R.string.errornoDate_new_mission_and_group, Toast.LENGTH_SHORT).show();
                } else {
                    //待添加DATE
                    if (allreadyTime == false) {
                        Toast.makeText(this, R.string.errornoTime_new_mission_and_group, Toast.LENGTH_SHORT).show();
                    } else {
                        if (editTextWhere.getText().toString().trim().equals("")) {
                            Toast.makeText(this, R.string.errorwhere_new_mission, Toast.LENGTH_SHORT).show();
                        } else {
                            if (virtue<0) {
                                Toast.makeText(this, R.string.errorvirtue_new_mission, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),String.valueOf(virtue),Toast.LENGTH_SHORT).show();
                                switch (virtue){
                                    case 0:{
                                        strV=1;
                                        intV=1;
                                        agiV=1;
                                        break;
                                    }
                                    case 1:{
                                        strV=3;
                                        intV=0;
                                        agiV=0;
                                        break;
                                    }
                                    case 2:{
                                        strV=0;
                                        intV=3;
                                        agiV=0;
                                        break;
                                    }
                                    case 3:{
                                        strV=0;
                                        intV=0;
                                        agiV=3;
                                        break;
                                    }
                                    default:{
                                        strV=1;
                                        intV=1;
                                        agiV=1;
                                    }
                                }
                                //待添加TIME
                                if (editTextcontent.getText().toString().trim().equals("")) {
                                    Toast.makeText(this, R.string.errornocontent_new_mission, Toast.LENGTH_SHORT).show();
                                } else {
                                    //    missionData.content = editTextcontent.getText().toString();
                                    if (isOtherPay) {
                                        getPay = editTextOtherPay.getText().toString();
                                    }
                                    calendar.set(year, month, day, hour, minute);
                                    if (!isEdit) {
                                        final ProgressDialog progressDialog = new ProgressDialog(this);
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        ClientFunctions.publishMission(
                                                editTextName.getText().toString(),
                                                press,
                                                Integer.valueOf(editTextNumber.getText().toString()),
                                                editTextWhere.getText().toString(),
                                                editTextcontent.getText().toString(),
                                                getPay,
                                                strV,
                                                intV,
                                                agiV,
                                                calendar.getTime(),
                                                new ClientResponse() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (!String.valueOf(uriImg).equals("null")) {
                                                            ClientFunctions.uploadMissionImage(Integer.valueOf(response), picturePath, new ClientResponse() {
                                                                @Override
                                                                public void onResponse(String response) {

                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(v.getContext(), "上傳成功", Toast.LENGTH_SHORT).show();

                                                                    NewMissionActivity.this.finish();
                                                                }

                                                                @Override
                                                                public void onErrorResponse(String response) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(v.getContext(), "上傳完成", Toast.LENGTH_SHORT).show();
                                                                    NewMissionActivity.this.finish();
                                                                }
                                                            });
                                                        } else {

                                                            Toast.makeText(v.getContext(), "上傳成功", Toast.LENGTH_SHORT).show();
                                                            NewMissionActivity.this.finish();
                                                        }
                                                        ClientFunctions.updateMissions(new ClientResponse() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                            }

                                                            @Override
                                                            public void onErrorResponse(String response) {
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onErrorResponse(String response) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(v.getContext(), response, Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                    } else {
                                        ClientFunctions.publishUpdateMission(missionID,
                                                editTextName.getText().toString(),
                                                press,
                                                Integer.valueOf(editTextNumber.getText().toString()),
                                                editTextWhere.getText().toString(),
                                                editTextcontent.getText().toString(),
                                                getPay,
                                                calendar.getTime(),
                                                strV,
                                                intV,
                                                agiV,
                                                new ClientResponse() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Toast.makeText(NewMissionActivity.this, "編輯成功", Toast.LENGTH_SHORT).show();
                                                        ClientFunctions.updateMissions(new ClientResponse() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                NewMissionActivity.this.finish();
                                                            }

                                                            @Override
                                                            public void onErrorResponse(String response) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onErrorResponse(String response) {
                                                        Toast.makeText(NewMissionActivity.this, response + "_4", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                        );

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //按取消鈕返回
    public void onClickCancel(View v){
    finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode==Permission.WRITE_EXTERNAL_STORAGE_KEY){
            if (shouldShowRequestPermissionRationale(Permission.WRITE_PERMISSION) == true) {
                if (ActivityCompat.checkSelfPermission(this, Permission.WRITE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,getResources().getString(R.string.permission_no_premission_picture) , Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if (ActivityCompat.checkSelfPermission(this, Permission.WRITE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                    //建立DIALOG
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getResources().getString(R.string.permission_new_mission_and_group));
                    builder.setCancelable(false);
                    builder.setPositiveButton(getResources().getString(R.string.permission_gotoset), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_tip), Toast.LENGTH_LONG).show();
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.permission_cencel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();
                }
            }

        }

    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
//
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


/*預備軍
    }
        public void onClickDate(View v) {
            // 获取日历对象

            calendar = Calendar.getInstance();
// 获取当前对应的年、月、日的信息
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);



// 初始化DatePickerDialog
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setTitle(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
            }, year, month, day).show();
        }


     /*SimpleDateFormat date_sim = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date_date = new Date(System.currentTimeMillis());
        outPutString = date_sim.format(date_date);
        useToCompare = Float.parseFloat(outPutString);*/

  /* 初始化spinner用的
         contextNGAP = this.getApplicationContext();
        arraysNGAPress = new ArrayAdapter<String>(NewGroupActivity.this, R.layout.support_simple_spinner_dropdown_item, stringPress);
        spinnerPress.setAdapter(arraysNGAPress);
        arraysNGAPPay = new ArrayAdapter<String>(NewGroupActivity.this, R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.pay_new_group));
        spinnerPay.setAdapter(arraysNGAPPay);
//抓取螢幕解析度
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);//取得螢幕解析度


*/
