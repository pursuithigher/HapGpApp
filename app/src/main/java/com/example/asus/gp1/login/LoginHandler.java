package com.example.asus.gp1.login;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Qzhu on 2018/3/29.
 */

public class LoginHandler extends Handler {

    private OnLoginCallBack callBack;

    private String userName,passWord;

    public interface OnLoginCallBack{
        void onReuqestComplete();
        void onLoginResponse(boolean hasNetwork);
        void onLoginIdNotFind();
        void onLoginSuccess(JSONObject json,String userName,String passWord) throws JSONException;
        void onHandleError();
    }

    public LoginHandler(OnLoginCallBack callBack) {
        super(Looper.getMainLooper());
        this.callBack = callBack;
    }

    public void setUser(String userName,String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    @Override
    public void handleMessage(Message msg) {
        callBack.onReuqestComplete();

    }
}
