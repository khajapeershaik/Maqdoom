package com.project.maqdoom.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveResponseData {

    @Expose
    @SerializedName("ErrorCode")
    private int ErrorCode;

    @Expose
    @SerializedName("Data")
    private String data;

    @Expose
    @SerializedName("message")
    private String message;

    public SaveResponseData(int errorCode, String data, String message) {
        ErrorCode = errorCode;
        this.data = data;
        this.message = message;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
