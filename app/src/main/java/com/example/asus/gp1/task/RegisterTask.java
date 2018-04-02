package com.example.asus.gp1.task;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.asus.gp1.helper.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Qzhu on 2018/3/29.
 */
public class RegisterTask extends AsyncTask<Void,Void,Bundle>{

    private AddClassTask.AddClassCallBack callback;
    private Map<String,String> maps;

    public RegisterTask(AddClassTask.AddClassCallBack callBack, Map<String, String> maps) {
        this.callback = callBack;
        this.maps = maps;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        return RequestUtil.Regist(maps);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        if(bundle == null) {
            callback.onRequestErr();
            return ;
        }
        JSONObject json = null;
        try {
            json = new JSONObject(bundle.getString("value"));
            if ("Success".equals(json.get("excuteResult"))) {
                callback.onAddSuccess();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onRequestErr();
        }
    }
}
