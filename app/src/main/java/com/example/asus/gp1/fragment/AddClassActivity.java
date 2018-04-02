package com.example.asus.gp1.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.gp1.R;
import com.example.asus.gp1.helper.MetaData;
import com.example.asus.gp1.task.AddClassTask;
import com.example.asus.gp1.utils.DialogHelper;

import java.util.HashMap;

public class AddClassActivity extends AppCompatActivity implements AddClassTask.AddClassCallBack {

    private AddClassTask addClassTask;

    private DialogHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("添加课程");
        setContentView(R.layout.activity_addclass);
        helper = new DialogHelper();
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View view) {
                requestAdd();
            }
        });
    }

    private void requestAdd(){
        HashMap<String,String> m=new HashMap<>();
        m.put("lid", MetaData.LID);
        m.put("pwd",MetaData.PWD);
        m.put("projectname",((EditText)findViewById(R.id.class_name)).getText().toString());
        m.put("subtitle",((EditText)findViewById(R.id.class_name1)).getText().toString());
        m.put("dayofweek",((EditText)findViewById(R.id.class_name2)).getText().toString());
        m.put("starttime",((EditText)findViewById(R.id.class_name3)).getText().toString());
        m.put("endtime",((EditText)findViewById(R.id.class_name4)).getText().toString());
        m.put("west","0");
        m.put("east","0");
        m.put("south","0");
        m.put("north","0");

        helper.createProcessDialog(this,"").show(false,false);
        addClassTask = new AddClassTask(this, m);
        addClassTask.execute();
    }

    @Override
    public void onAddSuccess() {
        helper.dismiss();
        Intent in = new Intent();
        in.setClassName( getApplicationContext(), "com.example.asus.gp1.activities.MainActivity" );
        startActivity( in );
        finish();
    }

    @Override
    public void onErrMsg(String msg) {
        helper.dismiss();
        AlertDialog.Builder builder  = new AlertDialog.Builder(AddClassActivity.this);
        builder.setMessage(msg) ;
        builder.setPositiveButton("是" ,  null );
        builder.show();
    }

    @Override
    public void onRequestErr() {
        helper.dismiss();
        Toast.makeText(this, R.string.request_failed, Toast.LENGTH_SHORT).show();
    }

}

