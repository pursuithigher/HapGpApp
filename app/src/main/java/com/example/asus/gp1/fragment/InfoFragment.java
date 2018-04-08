package com.example.asus.gp1.fragment;

import android.app.AlertDialog;
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
import com.example.asus.gp1.task.TeacherInfoTask;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoFragment extends Fragment implements TeacherInfoTask.onLoadCallback {

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        FloatingActionButton bt2=(FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(),TeachersignActivity.class);
                startActivity( in );
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        load();
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
        new TeacherInfoTask(m,this).execute();
    }

    private void deallist(ArrayList<HashMap<String, String>> listItem, View view){
        ListView listView = (ListView) view.findViewById(R.id.tinfo_listview);

        if(listItem.size()==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("t1", "您没有创建任何课程，请添加");
            map.put("t1", "");
            listItem.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItem,
                android.R.layout.simple_list_item_2
                , new String[]{"t1", "t2"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoadComplete(ArrayList<HashMap<String, String>> result) {
        deallist(result, getView());
    }

    @Override
    public void onRequestErr() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.request_failed) ;
        builder.setPositiveButton("是" ,  null );
        builder.show();
    }
}
