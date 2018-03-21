package com.example.administrator.cloudnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegeisterActivity extends Activity {

    private EditText username_et;
    private EditText password_et;
    private EditText re_password_et;

    private Button regeister_btn;
    private Button exit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regeister);


        initView();

        View.OnClickListener mlistener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.regeister_btn:regeister_event();break;
                    case R.id.exit_btn:exit_event();break;
                    default :return;
                }
            }
        };
        regeister_btn.setOnClickListener(mlistener);
        exit_btn.setOnClickListener(mlistener);

    }

    private void regeister_event() {

        if(!judge_password(password_et.getText().toString(),re_password_et.getText().toString())) {
            new AlertDialog.Builder(RegeisterActivity.this)
                    .setTitle("提示")
                    .setMessage("您输入的密码不一致，请重新输入!")
                    .setPositiveButton("确定",null)
                    .setNegativeButton("取消",null)
                    .show();
            return;
        };

        String username=username_et.getText().toString();
        String password=password_et.getText().toString();
        String re_password=re_password_et.getText().toString();

        if(username.equals("")||password.equals("")||re_password.equals("")){
            new AlertDialog.Builder(RegeisterActivity.this)
                    .setTitle("提示")
                    .setIcon(R.drawable.scare)
                    .setMessage("输入的信息不能为空!")
                    .setPositiveButton("确定",null)
                    .show();
            return;
        }


        if(!regeisterUser()){
            Toast.makeText(getApplication(),"注册用户失败",Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(RegeisterActivity.this)
                .setTitle("提示")
                .setIcon(R.drawable.question)
                .setMessage("成功注册一名用户\n" +
                        "用户名"+username_et.getText().toString()+"\n" +
                        "密码："+password_et.getText().toString())
                .setPositiveButton("确定",null)
                .show();
    }

    private boolean regeisterUser() {


        /**
         * 进行用户插入操作,并且返回结果
         */
        return true;
    }

    private void exit_event() {
        new AlertDialog.Builder(RegeisterActivity.this)
                .setTitle("提示")
                .setMessage("您确定要退出吗？")
                .setIcon(R.drawable.question)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    private boolean judge_password(String pw,String re_pw){
        return pw.equals(re_pw);
    }

    private void initView() {

        username_et= (EditText) findViewById(R.id.username_et);
        password_et= (EditText) findViewById(R.id.password_et);
        re_password_et= (EditText) findViewById(R.id.re_password_et);

        exit_btn= (Button) findViewById(R.id.exit_btn);
        regeister_btn= (Button) findViewById(R.id.regeister_btn);

        initData();

    }

    private void initData() {
    }
}
