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

public class TeacherTask extends AsyncTask<Void,Void,Bundle> {

    private Map<String,String> values = null;
    private onLoadCallback callback;

    public interface onLoadCallback{
        void onLoadComplete(ArrayList<HashMap<String, Object>> result);
    }

    public TeacherTask(Map<String, String> values, onLoadCallback callback) {
        this.values = values;
        this.callback = callback;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {

        return RequestUtil.QueryClassTeacher(values);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        JSONObject json=null;
        if(bundle == null) {
            callback.onLoadComplete(null);
        }
        try {
            String msgg = (String) bundle.get("value");
            if(msgg.startsWith("网络请求出错")){
                callback.onLoadComplete(null);
                return;
            }
            json=new JSONObject(msgg);
            JSONObject jslist=(JSONObject)json.get("extResult");
            JSONArray jsar=(JSONArray)jslist.get("class");


            for(int i=0;i<jsar.length();i++){
                JSONObject jsobj=jsar.getJSONObject(i);
                Iterator<String> iterator=jsobj.keys();
                HashMap<String,Object> m=new HashMap<>();
                while (iterator.hasNext()){
                    String key=iterator.next();
                    m.put(key,jsobj.get(key));
                }
                listItem.add(m);
            }
            callback.onLoadComplete(listItem);
        } catch (JSONException e) {
            callback.onLoadComplete(null);
        }
    }
}
