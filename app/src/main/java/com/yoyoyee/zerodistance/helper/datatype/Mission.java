package com.yoyoyee.zerodistance.helper.datatype;

import java.util.Date;

/**
 * Created by p1235 on 2016/3/29.
 */
public class Mission {

    public int id;             //任務id
    public String userUid;         //擁有者id
    public String schoolID;       //學校id
    public String title;          //任務名稱
    public Boolean isUrgent;      //是否緊急
    public int needNum;           //需求人數
    public int currentNum;        //目前人數
    public String content;        //任務內容
    public String imagePath;      //圖片url
    public String voicePath;      //語音url
    public String videoPath;      //影片url
    public String reward;         //獎勵
    public Date createdAt;         //創建時間
    public Date expAt;            //到期時間
    public String qaId;           //Q&A ID
    public boolean isRunning;     //是否執行中
    public boolean isFinished;     //是否完成
    public Date finishedAt;

    public Mission() {

    }
    public Mission(int id, String userUid, String schoolID, String title, boolean isUrgent,
                   int needNum, int currentNum, String content, String reward, Date createdAt,
                   Date expAt, boolean isRunning, boolean isFinished, Date finishedAt) {
        this.id = id;
        this.userUid = userUid;
        this.schoolID = schoolID;
        this.title = title;
        this.isUrgent = isUrgent;
        this.needNum = needNum;
        this.currentNum = currentNum;
        this.content = content;
        this.reward = reward;
        this.createdAt = createdAt;
        this.expAt = expAt;
        this.isRunning = isRunning;
        this.isFinished = isFinished;
        this.finishedAt = finishedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userUid;
    }

    public void setUserID(String userID) {
        this.userUid = userID;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public int getNeedNum() {
        return needNum;
    }

    public void setNeedNum(int needNum) {
        this.needNum = needNum;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Date getCreateAt() {
        return createdAt;
    }

    public void setCreateAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpAt() {
        return expAt;
    }

    public void setExpAt(Date expAt) {
        this.expAt = expAt;
    }

    public String getQaId() {
        return qaId;
    }

    public void setQaId(String qaId) {
        this.qaId = qaId;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isFinshed() {
        return isFinished;
    }

    public void setIsFinshed(boolean isFinshed) {
        this.isFinished = isFinshed;
    }




}
