package com.example.asus.gp1.task;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.asus.gp1.callback.BaseInterface;
import com.example.asus.gp1.helper.RequestUtil;
import com.example.asus.gp1.login.LoginHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qzhu on 2018/3/29.
 */

public class UserLoginTask extends AsyncTask<Void,Void,Bundle>{

    private final String mEmail;
    private final String mPassword;
    private LoginHandler.OnLoginCallBack callBack;

    public UserLoginTask(String email, String password, LoginHandler.OnLoginCallBack callBack) {
        mEmail = email;
        mPassword = password;
        this.callBack = callBack;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        Map<String,String> m = new HashMap<>();
        m.put("lid",mEmail);
        m.put("pwd", mPassword);
        return RequestUtil.Login(m);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        if(bundle != null) {
            JSONObject json=null;
            try {
                String msgg = bundle.getString("value");
                if(msgg.startsWith("网络请求出错")){
                    callBack.onLoginResponse(false);
                    return;
                }else {
                    callBack.onLoginResponse(true);
                }
                json=new JSONObject(msgg);
                if("We cant find the Login ID".equals(json.get("message"))){
                    callBack.onLoginIdNotFind();
                }else if("Success".equals(json.get("excuteResult"))){
                    callBack.onLoginSuccess(json, mEmail, mPassword);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                callBack.onHandleError();
            }
        }else{
            callBack.onHandleError();
        }
    }
}