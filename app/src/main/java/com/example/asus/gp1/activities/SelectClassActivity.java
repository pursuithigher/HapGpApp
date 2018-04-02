package com.example.asus.gp1.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.gp1.R;
import com.example.asus.gp1.helper.MetaData;
import com.example.asus.gp1.task.AddClassTask;
import com.example.asus.gp1.task.SelectClassTask;
import com.example.asus.gp1.utils.DialogHelper;

import java.util.HashMap;

public class SelectClassActivity extends AppCompatActivity implements AddClassTask.AddClassCallBack {

    private DialogHelper helper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectclass);
        setTitle("选择课程");

        helper = new DialogHelper();
        findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                load();
            }
        });
    }

    private void load(){
        HashMap m=new HashMap();
        m.put("lid", MetaData.LID);
        m.put("pwd",MetaData.PWD);
        m.put("projectname",((EditText)findViewById(R.id.et3)).getText().toString());
        helper.createProcessDialog(this,"").show(false,false);
        new SelectClassTask(this, m).execute();
    }

    @Override
    public void onAddSuccess() {
        helper.dismiss();
        Intent in = new Intent();
        in.setClassName(getApplicationContext(), "com.example.asus.gp1.activities.MainActivity");
        startActivity(in);
        finish();
    }

    @Override
    public void onErrMsg(String msg) {
        helper.dismiss();
        AlertDialog.Builder builder  = new AlertDialog.Builder(SelectClassActivity.this);
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
