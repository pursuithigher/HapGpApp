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

public class SelectClassTask extends AsyncTask<Void, Void, Bundle>{

    private AddClassTask.AddClassCallBack callback;
    private Map<String,String> maps;

    public SelectClassTask(AddClassTask.AddClassCallBack callback, Map<String, String> maps) {
        this.callback = callback;
        this.maps = maps;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        return RequestUtil.SelectClass(maps);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        if(bundle == null) {
            callback.onRequestErr();
            return ;
        }
        JSONObject json=null;
        String msgg = bundle.getString("value");
        try {
            json=new JSONObject(msgg);
            if ("Success".equals(json.get("excuteResult"))) {
                callback.onAddSuccess();
            } else {
                callback.onErrMsg(json.getString("message"));
            }
        } catch (JSONException e) {
            callback.onRequestErr();
            e.printStackTrace();
        }
    }
}
