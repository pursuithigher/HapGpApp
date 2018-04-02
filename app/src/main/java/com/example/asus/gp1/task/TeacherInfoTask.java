package com.example.asus.gp1.task;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.asus.gp1.helper.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Qzhu on 2018/3/29.
 */

public class TeacherInfoTask extends AsyncTask<Void,Void,Bundle>{

    public interface onLoadCallback{
        void onLoadComplete(ArrayList<HashMap<String, String>> result);
        void onRequestErr();
    }

    private Map<String,String> map;
    private onLoadCallback callback;

    public TeacherInfoTask(Map<String, String> map, onLoadCallback callback) {
        this.map = map;
        this.callback = callback;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        return RequestUtil.QuerySignIn(map);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        JSONObject json = null;

        if(bundle == null) {
            callback.onRequestErr();
            return ;
        }
        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();
        try {
            String msgg = (String) bundle.get("value");
            if (msgg.startsWith("网络请求出错")) {
                callback.onRequestErr();
                return;
            }
            json = new JSONObject(msgg);
            if ("Success".equals(json.get("excuteResult"))) {
                JSONObject jsob = (JSONObject) ((JSONObject) json.get("extResult")).get("结果");
                Iterator<String> it = jsob.keys();
                while (it.hasNext()) {
                    String key=it.next();
                    HashMap m=new HashMap();
                    m.put("t1",key);
                    m.put("t2",jsob.get(key).toString());
                    listItem.add(m);
                }
                callback.onLoadComplete(listItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onRequestErr();
        }
    }
}
