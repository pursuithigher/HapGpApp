package com.example.asus.gp1.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.gp1.activities.MainActivity;
import com.example.asus.gp1.R;
import com.example.asus.gp1.helper.MetaData;
import com.example.asus.gp1.task.AddClassTask;
import com.example.asus.gp1.task.TeacherSignTask;
import com.example.asus.gp1.utils.DialogHelper;

import java.util.HashMap;

public class TeachersignActivity extends AppCompatActivity implements AddClassTask.AddClassCallBack {

    private DialogHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("教师补签");
        setContentView(R.layout.activity_teachersign);

        helper = new DialogHelper();
        Button bt=(Button)findViewById(R.id.bt2);
        bt.setOnClickListener(new View.OnClickListener() {

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
        m.put("names",((EditText)findViewById(R.id.et1)).getText().toString());
        helper.createProcessDialog(this,"").show(false,false);
        new TeacherSignTask(this,m).execute();
    }

    @Override
    public void onAddSuccess() {
        helper.dismiss();
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onErrMsg(String msg) {
        helper.dismiss();
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
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
