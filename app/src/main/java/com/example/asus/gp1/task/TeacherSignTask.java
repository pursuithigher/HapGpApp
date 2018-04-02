package com.example.asus.gp1.task;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.asus.gp1.helper.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Qzhu on 2018/3/29.
 */

public class TeacherSignTask extends AsyncTask<Void,Void,Bundle> {

    private AddClassTask.AddClassCallBack callBack;
    private Map<String,String> maps;

    public TeacherSignTask(AddClassTask.AddClassCallBack callBack, Map<String, String> maps) {
        this.callBack = callBack;
        this.maps = maps;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        return RequestUtil.TeacherSignIn(maps);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        if(bundle == null) {
            callBack.onRequestErr();
            return ;
        }
        JSONObject json=null;
        String msgg=(String)bundle.get("value");
        try {
            json=new JSONObject(msgg);
            if ("Success".equals(json.get("excuteResult"))) {
                callBack.onAddSuccess();
            } else {
                callBack.onErrMsg((String)json.get("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
