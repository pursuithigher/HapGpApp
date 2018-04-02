package com.example.asus.gp1.helper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class RequestUtil {
    public static  String IP = "172.20.10.4";
    public static  String Port = "12345";
    public static final String ApiRoute = "/api/API";

    public static Bundle post(final String postStr){
        boolean extb = false;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            String strurl = "http://" + IP + ":" + Port + ApiRoute;
            HttpPost request = new HttpPost(strurl);
            request.addHeader("Content-Type", "application/json");
            StringEntity se = new StringEntity(postStr);
            request.setEntity(se);
            HttpResponse httpResponse = httpClient.execute(request);
            String result = EntityUtils.toString(httpResponse.getEntity());
            Bundle data = new Bundle();
            data.putString("value", result);
            Message msg = new Message();
            msg.setData(data);
            return data;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void post(final String postStr, final Handler handler) throws IOException {
        final boolean[] extb=new boolean[]{false};
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    String strurl = "http://" + IP + ":" + Port + ApiRoute;
                    HttpPost request = new HttpPost(strurl);
                    request.addHeader("Content-Type", "application/json");
                    StringEntity se = new StringEntity(postStr);
                    request.setEntity(se);
                    HttpResponse httpResponse = httpClient.execute(request);
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    Bundle data = new Bundle();
                    data.putString("value", result);
                    Message msg = new Message();
                    msg.setData(data);
                    extb[0] = true;
                    handler.handleMessage(msg);

                } catch (Exception ex) {
                    Bundle data = new Bundle();
                    data.putString("value", "网络请求出错:" + ex.getMessage());
                    Message msg = new Message();
                    msg.setData(data);
                    extb[0] = true;
                    handler.handleMessage(msg);
                }
            }
        });
        t.start();
        try {
            t.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!extb[0]) {
            Bundle data = new Bundle();
            data.putString("value", "网络请求出错:");
            Message msg = new Message();
            msg.setData(data);
            extb[0] = true;
            handler.handleMessage(msg);
        }
    }

    public static Bundle QueryClassTeacher(final Map m) {
        String poststring = "{\t\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"TeacherQueryClass\",\n" +
                "    \"Params\": {\n" +
                "    }\n" +
                "}";
        return post(poststring);
    }

    public static void DoCheckNetWork(final Handler handler) throws IOException {
        String poststring="{\t\n" +
                "    \"lid\": \"\",\n" +
                "    \"pwd\": \"\",\n" +
                "    \"operation\": \"none\",\n" +
                "    \"Params\": {\n" +
                "    }\n" +
                "}";
        post(poststring,handler);
    }

    public static Bundle Login(final Map m){
        String poststring = "{\t\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"Login\",\n" +
                "    \"Params\": {\n" +
                "    }\n" +
                "}";
        return post(poststring);
    }

    public static Bundle Regist(final Map m){
        String tt = m.containsKey("teacher") ? "teacher" : "temp";
        String str = "{\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"regist\",\n" +
                "    \"Params\": {\n" +
                "        \"" + tt + "\": true,\n" +
                "        \"name\":\"" + m.get("name") + "\"\n" +
                "    }\n" +
                "}";
        return post(str);
    }

    public static Bundle AddProject(final Map m){
        String str = "{\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"addproject\",\n" +
                "    \"Params\": {\n" +
                "        \"projectname\": \"" + m.get("projectname") + "\",\n" +
                "        \"subtitle\":\"" + m.get("subtitle") + "\" ,\n" +
                "        \"starttime\":\"" + m.get("starttime") + "\",\n" +
                "        \"endtime\":\"" + m.get("endtime") + "\",\n" +
                "        \"dayofweek\":\"" + m.get("dayofweek") + "\",\n" +
                "        \"west\":\"" + m.get("west") + "\",\n" +
                "        \"east\":\"" + m.get("east") + "\",\n" +
                "        \"south\":\"" + m.get("south") + "\",\n" +
                "        \"north\":\"" + m.get("north") + "\"\n" +
                "        \n" +
                "    }\n" +
                "}";
        return post(str);
    }

    //学生获取已选课程
    public static Bundle GetClass(final Map m){
        String str = "{\t\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"GetClass\",\n" +
                "    \"Params\": {\n" +
                "    }\n" +
                "}";
        return post(str);
    }

    public static Bundle SignIn(final Map m){
        String str = "{\t\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"SignIn\",\n" +
                "    \"Params\": {\n" +
                "    \t\"x\":\"" + m.get("x") + "\",\n" +
                "    \t\"y\":\"" + m.get("y") + "\"\n" +
                "    }\n" +
                "}";
        return post(str);

    }

    public static Bundle QuerySignIn(final Map m){
        String str = "{\t\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"QuerySignIn\",\n" +
                "    \"Params\": {\n" +
                "    }\n" +
                "}";
        return post(str);
    }

    public static Bundle TeacherSignIn(final Map m){
        String str = "{\t\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"TeacherSignIn\",\n" +
                "    \"Params\": {\n" +
                "        \"names\": \"" + m.get("names") + "\"\n" +
                "    }\n" +
                "}";
        return post(str);
    }

    public static void SelectClass(final Map m, final Handler handler) throws IOException {
        String str = "{\t\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"SelectClass\",\n" +
                "    \"Params\": {\n" +
                "        \"projectname\": \"" + m.get("projectname") + "\"\n" +
                "    }\n" +
                "}";
        post(str, handler);

    }

    //获取指定课程信息
    public static void QueryClass(final Map m, final Handler handler) throws IOException {
        String str = "{\t\n" +
                "    \"lid\": \"" + m.get("lid") + "\",\n" +
                "    \"pwd\": \"" + m.get("pwd") + "\",\n" +
                "    \"operation\": \"QueryClass\",\n" +
                "    \"Params\": {\n" +
                "        \"projectname\": \"" + m.get("projectname") + "\"\n" +
                "    }\n" +
                "}";
        post(str, handler);

    }
}
