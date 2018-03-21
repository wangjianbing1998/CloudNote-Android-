package com.example.administrator.cloudnote;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.entity.User;
import com.wjb.utils.MD5Util;
import com.wjb.utils.MyHttpUtil;
import com.wjb.utils.MyUrlUtil;
import com.wjb.utils.SharePreferenceUtil;

public class RegisterActivity extends Activity {

    private final String PWD_LENGTH="pwd_length";
    private final String HAS_MD5="has_md5_";
    private final String HAS_SAVE_PASSWORD="has_save_password";
    private final String LOGIN_AUTO="has_login_auto";
    private final String USERNAME="username";
    private final String PASSWORD="password";



    private EditText username_et;
    private EditText password_et;
    private EditText re_password_et;

    private Button regeister_btn;
    private Button exit_btn;

    private boolean hasMD=true;

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

        String username=username_et.getText().toString();
        String password=password_et.getText().toString();
        String re_password=re_password_et.getText().toString();

        if(!judge_password(password,re_password)) {
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("提示")
                    .setMessage("您输入的密码不一致，请重新输入!")
                    .setPositiveButton("确定",null)
                    .show();
            return;
        };
        if(!regeister()){
            Toast.makeText(getApplication(),"注册用户失败",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    /**
     * 进行用户插入操作,并且返回结果
     */
    private boolean regeister() {

        String username = username_et.getText().toString();
        String password= password_et.getText().toString();
        if(username.equals("wang")) {
            Toast.makeText(this, "该用户名已经注册，请重新填写用户名", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(username)){
            Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }
        String url= MyUrlUtil.getURL()+"register.do";

        password=password.trim();

        String params = "username="+username+"&password="+MD5Util.getMd5(password);

        new RegisiterAsyncTask().execute(url,params);
        return true;
    }

    class RegisiterAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return MyHttpUtil.post(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String pS) {

            if(TextUtils.isEmpty(pS)){
                Toast.makeText(RegisterActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("register", pS);
            pS = pS.trim();
            boolean result=pS.equals("true");
            if(result){
                Intent mIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("username", username_et.getText().toString());
                bundle.putString("password", password_et.getText().toString());
                mIntent.putExtras(bundle);
                setResult(RESULT_OK,mIntent);

                SharePreferenceUtil.putInt(RegisterActivity.this,username_et.getText().toString(),password_et.getText().toString().length());

                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                SharePreferenceUtil.putInt(RegisterActivity.this,username_et.getText().toString(),0);
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void exit_event() {
        new AlertDialog.Builder(RegisterActivity.this)
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
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();


        String username = bundle.getString("username");

        if(!TextUtils.isEmpty(username))
        {
            username_et.setText(username);
        }
    }
}
