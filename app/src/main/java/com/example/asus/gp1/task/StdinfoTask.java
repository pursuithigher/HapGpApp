package com.example.asus.gp1.task;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.asus.gp1.helper.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Qzhu on 2018/3/29.
 */
public class StdinfoTask extends AsyncTask<Void,Void,Bundle> {

    private AddClassTask.AddClassCallBack callBack;
    private Map<String,String> maps;

    public StdinfoTask(AddClassTask.AddClassCallBack callBack, Map<String, String> maps) {
        this.callBack = callBack;
        this.maps = maps;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        return RequestUtil.SignIn(maps);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        if(bundle == null) {
            callBack.onRequestErr();
            return ;
        }
        JSONObject json = null;
        String msgg = (String) bundle.get("value");
        try {
            json = new JSONObject(msgg);
            if ("Success".equals(json.get("excuteResult"))) {
                callBack.onAddSuccess();
            } else {
                callBack.onErrMsg(json.getString("message"));
            }
        } catch (JSONException e) {
            callBack.onRequestErr();
            e.printStackTrace();
        }
    }
}
