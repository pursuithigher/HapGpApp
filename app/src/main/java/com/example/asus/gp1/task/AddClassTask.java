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

public class AddClassTask extends AsyncTask<Void,Void,Bundle>{

    public interface AddClassCallBack{
        void onAddSuccess();
        void onErrMsg(String msg);
        void onRequestErr();
    }

    private AddClassCallBack callBack;
    private Map<String,String> maps;

    public AddClassTask(AddClassCallBack callBack, Map<String, String> maps) {
        this.callBack = callBack;
        this.maps = maps;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        return RequestUtil.AddProject(maps);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        JSONObject json=null;
        if(bundle == null) {
            callBack.onRequestErr();
            return ;
        }
        String msgg=(String)bundle.get("value");
        try {
            json=new JSONObject(msgg);
            if("Success".equals(json.get("excuteResult"))){
                callBack.onAddSuccess();
            }else
            {
                callBack.onErrMsg((String)json.get("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onRequestErr();
        }
    }
}
