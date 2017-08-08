package com.epaylinks.download;

import java.io.Serializable;

/**
 * Created by Deken on 2017/5/23.
 */

public class DownloadEntity implements Serializable {

    private int id=0;
    private String url="";
    private String downName="";
    private float currentLen=0f;
    private float totalLen=0f;
    private DownState mDownState;
    private int speedInt=0;//k/s
    private String downedFilePath="";
    private String downedFileName="";

    public enum DownState {IDLE,STARTED,DOWNING,FINISH,ERR}

    public DownloadEntity() {
        mDownState=DownState.IDLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownName() {
        return downName;
    }

    public void setDownName(String downName) {
        this.downName = downName;
    }

    public float getCurrentLen() {
        return currentLen;
    }

    public void setCurrentLen(float currentLen) {
        this.currentLen = currentLen;
    }

    public float getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(float totalLen) {
        this.totalLen = totalLen;
    }

    public DownState getmDownState() {
        return mDownState;
    }

    public void setmDownState(DownState mDownState) {
        this.mDownState = mDownState;
    }

    public int getSpeedInt() {
        return speedInt;
    }

    public void setSpeedInt(int speedInt) {
        this.speedInt = speedInt;
    }

    public String getDownedFilePath() {
        return downedFilePath;
    }

    public void setDownedFilePath(String downedFilePath) {
        this.downedFilePath = downedFilePath;
    }

    public String getDownedFileName() {
        return downedFileName;
    }

    public void setDownedFileName(String downedFileName) {
        this.downedFileName = downedFileName;
    }
}
