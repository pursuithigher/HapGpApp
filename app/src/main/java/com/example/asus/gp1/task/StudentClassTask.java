package com.example.asus.gp1.task;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.asus.gp1.helper.RequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Qzhu on 2018/3/29.
 */
public class StudentClassTask extends AsyncTask<Void,Void,Bundle>{

    public interface onLoadCallback{
        void onLoadComplete(ArrayList<HashMap<String, String>> result);
        void onRequestErr();
    }

    private Map<String,String> map;
    private onLoadCallback callback;

    public StudentClassTask(Map<String, String> map, onLoadCallback callback) {
        this.map = map;
        this.callback = callback;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        return RequestUtil.GetClass(map);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        JSONObject json = null;
        final ArrayList<HashMap<String, String>> listItem = new ArrayList<>();
        if(bundle == null) {
            callback.onRequestErr();
            return ;
        }
        String msgg = (String) bundle.get("value");
        if (msgg.startsWith("网络请求出错")) {
            callback.onRequestErr();
            return;
        }
        try {
            json = new JSONObject(msgg);
            JSONObject jslist = (JSONObject) json.get("extResult");
            JSONArray jsar = (JSONArray) jslist.get("class");
            for (int i = 0; i < jsar.length(); i++) {
                JSONObject jsobj = jsar.getJSONObject(i);
                Iterator<String> iterator = jsobj.keys();
                HashMap<String, String> m = new HashMap<>();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    m.put(key, jsobj.getString(key));
                }
                listItem.add(m);
            }
            callback.onLoadComplete(listItem);
        } catch (JSONException e) {
            callback.onRequestErr();
        }
    }
}
