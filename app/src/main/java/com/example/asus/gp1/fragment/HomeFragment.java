package com.example.asus.gp1.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.gp1.R;
import com.example.asus.gp1.adapter.OnFragmentInteractionListener;
import com.example.asus.gp1.helper.MetaData;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tv1 = (TextView) view.findViewById(R.id.name);
        tv1.setText(MetaData.Name);
        TextView tv2 = (TextView) view.findViewById(R.id.subview);
        if (MetaData.Role.equals("Teacher")) {
            tv2.setText(R.string.home_info);
        } else {
            tv2.setText("同学你好");
        }
        return view;
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
}
