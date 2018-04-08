package com.example.asus.gp1.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.asus.gp1.R;
import com.example.asus.gp1.activities.SelectClassActivity;
import com.example.asus.gp1.adapter.OnFragmentInteractionListener;
import com.example.asus.gp1.helper.MetaData;
import com.example.asus.gp1.task.StudentClassTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassFragment extends Fragment implements StudentClassTask.onLoadCallback {

    private OnFragmentInteractionListener mListener;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class, container, false);
        initView(view);
        load();
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.std_listview);
        FloatingActionButton bt2 = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), SelectClassActivity.class);
                startActivity(in);
            }
        });
    }

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

    private void load(){
        HashMap<String,String> m = new HashMap<>();
        m.put("lid", MetaData.LID);
        m.put("pwd", MetaData.PWD);
        new StudentClassTask(m,this).execute();

    }

    @Override
    public void onLoadComplete(ArrayList<HashMap<String, String>> result) {
        deallist(result);
    }

    private void deallist(ArrayList<HashMap<String, String>> listItem) {
        List<Map<String, String>> list = new ArrayList<>();
        for (HashMap<String, String> m : listItem) {
            String str1 = "";
            String str2 = "";
            for (String key : m.keySet()) {
                if ("课程名称".equals(key)) {
                    str1 += key + ":" + m.get(key) + "\n";
                }
                else {
                    str2 += key + ":" + m.get(key) + "\n";
                }
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("t1", str1);
            map.put("t2", str2);
            list.add(map);
        }

        if (list.size() == 0) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("t1", "您没有创建任何课程，请添加");
            map.put("t1", "");
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
                android.R.layout.simple_list_item_2
                , new String[]{"t1", "t2"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    @Override
    public void onRequestErr() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.request_failed) ;
        builder.setPositiveButton("是" ,  null );
        builder.show();
    }
}
