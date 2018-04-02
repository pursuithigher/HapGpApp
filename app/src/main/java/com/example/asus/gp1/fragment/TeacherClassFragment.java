package com.example.asus.gp1.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.asus.gp1.R;
import com.example.asus.gp1.adapter.OnFragmentInteractionListener;
import com.example.asus.gp1.helper.MetaData;
import com.example.asus.gp1.task.TeacherTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherClassFragment extends Fragment implements TeacherTask.onLoadCallback {
    private OnFragmentInteractionListener mListener;
    private TeacherTask task;

    @Override
    public void onStart() {
        super.onStart();
        load();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_class, container, false);
        FloatingActionButton bt=(FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), AddClassActivity.class);
                startActivity( in );
            }
        });
        return view;
    }

    private void load() {
        HashMap<String,String> m=new HashMap<>();
        m.put("lid", MetaData.LID);
        m.put("pwd",MetaData.PWD);
        task = new TeacherTask(m,this);
        task.execute();
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

    @Override
    public void onLoadComplete(ArrayList<HashMap<String, Object>> result) {
        deallist(result);
    }

    private void deallist(ArrayList<HashMap<String, Object>> listItem){
        if(listItem == null) {
            return ;
        }
        ListView listView_main_news = (ListView) getActivity().findViewById(R.id.tcf_listview);
        List<Map<String, String>> list = new ArrayList<>();

        for (HashMap<String, Object> m : listItem) {
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
        if(list.size()==0){
            Map<String, String> map = new HashMap<String, String>();
            map.put("t1", "您没有创建任何课程，请添加");
            map.put("t1", "");
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
                android.R.layout.simple_list_item_2
                , new String[]{"t1", "t2"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView_main_news.setAdapter(adapter);
    }
}
