package com.example.asus.gp1.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.asus.gp1.R;
import com.example.asus.gp1.adapter.OnFragmentInteractionListener;
import com.example.asus.gp1.helper.MetaData;
import com.example.asus.gp1.helper.RequestUtil;
import com.example.asus.gp1.task.AddClassTask;
import com.example.asus.gp1.task.StdinfoTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class StdinfoFragment extends Fragment implements AddClassTask.AddClassCallBack {
    private LocationManager locationManager;
    private double latitude = 0.0;
    private double longitude = 0.0;

    private OnFragmentInteractionListener mListener;
    private ViewGroup viewContainer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stdinfo, container, false);
        viewContainer = view.findViewById(R.id.coordinate_container);
        Button bt = (Button) view.findViewById(R.id.bt4);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load();
            }
        });
        return view;
    }


    LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、临时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider被enable时触发此函数，比方GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider被disable时触发此函数，比方GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }

        // 当坐标改变时触发此函数，假设Provider传进同样的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // 经度
                latitude = location.getLatitude();
                // 纬度
                longitude = location.getLongitude();
            }
        }
    };

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
    }


    @SuppressLint("MissingPermission")
    private void toggleGPS() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(getContext(), 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location1 != null) {
                // 经度
                latitude = location1.getLatitude();
                // 纬度
                longitude = location1.getLongitude();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
            //gps已打开
        } else {
            toggleGPS();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLocation();
                }
            }, 2000);
        }
    }

    private void load(){
        HashMap m = new HashMap();
        m.put("lid", MetaData.LID);
        m.put("pwd", MetaData.PWD);
        m.put("x", "" + longitude);
        m.put("y", "" + latitude);
        new StdinfoTask(this,m).execute();
    }

    @Override
    public void onAddSuccess() {
        String msgheader="当前签到位置["+longitude+":"+latitude+"]\n";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msgheader+"签到成功");
        builder.setPositiveButton("是", null);
        builder.show();
    }

    @Override
    public void onErrMsg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg);
        builder.setPositiveButton("是", null);
        builder.show();
    }

    @Override
    public void onRequestErr() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.request_failed) ;
        builder.setPositiveButton("是" ,  null );
        builder.show();
//        Snackbar.make(viewContainer, "签到成功", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        }).show();
    }
}
