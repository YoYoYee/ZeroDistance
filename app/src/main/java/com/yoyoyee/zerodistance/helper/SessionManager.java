package com.yoyoyee.zerodistance.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "ZeroDistanceLogin";
	
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_USER_EMAIL = "email";
	private static final String KEY_USER_PASSWORD = "password";
	private static final String KEY_USER_UID = "uid";
	private static final String KEY_USER_ACCESS_KEY = "accessKey";
	private static final String KEY_USER_NAME = "name";
	private static final String KEY_USER_NICK_NAME = "nickName";
    private static final String KEY_USER_IS_TEACHER = "isTeacher";
	private static final String KEY_USER_SCHOOL_ID = "schoolID";
    private static final String KEY_USER_SCHOOL_NAME = "schoolName";
	private static final String KEY_USER_STUDENT_ID = "studentID";
    private static final String KEY_USER_IS_CONFIRMED = "isConfirmed";
    private static final String KEY_USER_PROFESSION = "profession";
    private static final String KEY_USER_LEVEL = "level";
    private static final String KEY_USER_EXP = "exp";
    private static final String KEY_USER_MONEY = "money";
    private static final String KEY_USER_STRENGTH = "strength";
    private static final String KEY_USER_INTELLIGENCE = "intelligence";
    private static final String KEY_USER_AGILE = "agile";
    private static final String KEY_USER_INTRODUCTION = "introduction";
    private static final String KEY_USER_TEXTSIZE = "usertextsize";
    private static final String KEY_USER_BECONTEXT = "becontext";
    private static final String KEY_USER_SORTWAY = "sortway";
    private static final String KEY_USER_CARDLAYOUTWAY = "CardlayoutWay";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
    public void setbecontext(boolean YN) {
        editor.putBoolean(KEY_USER_BECONTEXT, YN);
        editor.commit();
    }
    public void setSortWay(int setSortWay){
        editor.putInt(KEY_USER_SORTWAY, setSortWay);
        editor.commit();
    }
    public int getCardlayoutWay(){
        return pref.getInt(KEY_USER_CARDLAYOUTWAY, 1);
    }
    public void setCardlayoutWay(int CardlayoutWay){
        editor.putInt(KEY_USER_CARDLAYOUTWAY, CardlayoutWay);
        editor.commit();
    }
    public int getSortWay(){
        return pref.getInt(KEY_USER_SORTWAY, 1);
    }
    public boolean getbecontext(){
        return pref.getBoolean(KEY_USER_BECONTEXT, true);
    }

    public void setUserTextSize(float usertextsize) {

        editor.putFloat(KEY_USER_TEXTSIZE, usertextsize);
        editor.commit();
    }
    public float getUserTextSize() {
        return pref.getFloat(KEY_USER_TEXTSIZE, 10);
    }
	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setIsTeacher(boolean isTeacher) {
        editor.putBoolean(KEY_USER_IS_TEACHER, isTeacher);
        editor.commit();
    }

    public boolean isTeacher() {
        return pref.getBoolean(KEY_USER_IS_TEACHER, false);
    }

    public void setUserEmail(String email) {

		editor.putString(KEY_USER_EMAIL, email);
		editor.commit();

	}

	public String getUserEmail() {
		return pref.getString(KEY_USER_EMAIL, "");
	}

	public void setUserPassword(String password) {

		editor.putString(KEY_USER_PASSWORD, password);
		editor.commit();

	}

	public String getUserPassword() {
		return pref.getString(KEY_USER_PASSWORD, "");
	}

    public void setUserUid(String uid) {
        editor.putString(KEY_USER_UID, uid);
        editor.commit();
    }

    public String getUserUid() {
        return  pref.getString(KEY_USER_UID, "");
    }

    public void setUserAccessKey(String accessKey) {
        editor.putString(KEY_USER_ACCESS_KEY, accessKey);
        editor.commit();
    }

    public String getUserAccessKey() {
        return pref.getString(KEY_USER_ACCESS_KEY, "");
    }

    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public void setUserNickName(String nickName) {
        editor.putString(KEY_USER_NICK_NAME, nickName);
        editor.commit();
    }

    public String getUserNickName() {
        return pref.getString(KEY_USER_NICK_NAME, "");
    }

    public void setUserSchoolID(int schoolID) {
        editor.putInt(KEY_USER_SCHOOL_ID, schoolID);
        editor.commit();
    }

    public int getUserSchoolID() {
        return pref.getInt(KEY_USER_SCHOOL_ID, -1);
    }

    public void setUserStudentID(String studentID) {
        editor.putString(KEY_USER_STUDENT_ID, studentID);
        editor.commit();
    }

    public String getStudentID() {
        return pref.getString(KEY_USER_STUDENT_ID, "");
    }

    public void setUserSchoolName(String schoolName) {
        editor.putString(KEY_USER_SCHOOL_NAME, schoolName);
        editor.commit();
    }

    public String getUserSchoolName() {
        return pref.getString(KEY_USER_SCHOOL_NAME, "");
    }

    public void setUserIsConfirmed(boolean isConfirmed) {
        editor.putBoolean(KEY_USER_IS_CONFIRMED, isConfirmed);
        editor.commit();
    }

    public boolean isConfirmed() {
        return pref.getBoolean(KEY_USER_IS_CONFIRMED, false);
    }

    public void setUserProfession(int profession) {
        editor.putInt(KEY_USER_PROFESSION, profession);
        editor.commit();
    }

    public int getUserProfession() {
        return pref.getInt(KEY_USER_PROFESSION, 0);
    }

    public void setUserLevel(int level){
        editor.putInt(KEY_USER_LEVEL, level);
        editor.commit();
    }

    public int getUserLevel(){
        return pref.getInt(KEY_USER_LEVEL, 1);
    }

    public void setUserExp(int exp) {
        editor.putInt(KEY_USER_EXP, exp);
        editor.commit();
    }

    public int getUserExp() {
        return pref.getInt(KEY_USER_EXP, 0);
    }

    public void setUserMoney(int money) {
        editor.putInt(KEY_USER_MONEY, money);
        editor.commit();
    }

    public int getUserMoney() {
        return pref.getInt(KEY_USER_MONEY, 0);
    }

    public void setUserStrength(int strength) {
        editor.putInt(KEY_USER_STRENGTH, strength);
        editor.commit();
    }

    public int getUserStrength() {
        return pref.getInt(KEY_USER_STRENGTH, 0);
    }

    public void setUserIntelligence(int intelligence) {
        editor.putInt(KEY_USER_INTELLIGENCE, intelligence);
        editor.commit();
    }

    public int getUserIntelligence() {
        return pref.getInt(KEY_USER_INTELLIGENCE, 0);
    }

    public void setUserAgile(int agile) {
        editor.putInt(KEY_USER_AGILE, agile);
        editor.commit();
    }

    public int getUserAgile() {
        return pref.getInt(KEY_USER_AGILE, 0);
    }

    public void setUserIntroduction(String introduction) {
        editor.putString(KEY_USER_INTRODUCTION, introduction);
        editor.commit();
    }

    public String getUserIntroduction() {
        return pref.getString(KEY_USER_INTRODUCTION,"");
    }


}
