package com.example.administrator.cloudnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.com.wjb.Main.Test_ViewPager;
import com.wjb.utils.MD5Util;
import com.wjb.utils.MyHttpUtil;
import com.wjb.utils.MyUrlUtil;
import com.wjb.utils.SharePreferenceUtil;

/**
 * 登录界面
 */
public class LoginActivity extends Activity {


    final String TAG = "LoginActivity";
    private final String PWD_LENGTH = "pwd_length";
    private final String HAS_MD5 = "has_md5_";
    private final String HAS_SAVE_PASSWORD = "has_save_password";
    private final String LOGIN_AUTO = "has_login_auto";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private String org_password;

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private Button mLoginBt;
    private Button mRegisterBt;

    private CheckBox login_auto_cb;
    private CheckBox save_pwd_cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        initViews();
    }
    /**
     * 初始化控件
     */
    private void initViews() {
        //在布局文件中找到对应ID的控件对象
        mUsernameEt = (EditText) findViewById(R.id.username_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);

        mLoginBt = (Button) findViewById(R.id.login_btn);
        mRegisterBt = (Button) findViewById(R.id.regeister_btn);
        //创建监听器对象
        MyOnClickListener listener = new MyOnClickListener();
        //给按钮对象设置监听器
        mLoginBt.setOnClickListener(listener);
        mRegisterBt.setOnClickListener(listener);

        login_auto_cb = (CheckBox) findViewById(R.id.login_auto_cb);
        save_pwd_cb = (CheckBox) findViewById(R.id.save_pwd_cb);

        org_password = SharePreferenceUtil.getString(LoginActivity.this, PASSWORD);

        //如果上一次已经勾选了保存密码，那么直接把已经加过密的密码按照上次成功登陆后的密码位数 在用户面前显示出相同位数的但是已经被加过密的密码
        //让用户以为是他输入的密码，其实不是的，而是已经被加过密的密码
        boolean has_save_pwd = SharePreferenceUtil.getBoolean(LoginActivity.this, HAS_SAVE_PASSWORD);
        boolean has_login_auto = SharePreferenceUtil.getBoolean(LoginActivity.this, LOGIN_AUTO);
        if (has_save_pwd && has_login_auto) {
//            Toast.makeText(LoginActivity.this, "11111111111+1111111111", Toast.LENGTH_LONG).show();
            login_auto_event();
        } else if (has_save_pwd && !has_login_auto) {
//            Toast.makeText(LoginActivity.this, "11111111111+000000000", Toast.LENGTH_LONG).show();
            save_pwd_event();
        } else if (!has_save_pwd && has_login_auto) {
//            Toast.makeText(LoginActivity.this, "000000000+11111111111", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(LoginActivity.this, "00000000000+00000000000", Toast.LENGTH_LONG).show();
        }
        login_auto_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //如果自动登录被选择，那么保存密码也要被选择
                if (login_auto_cb.isChecked()) {
                    save_pwd_cb.setChecked(true);
                }
            }
        });
        save_pwd_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!save_pwd_cb.isChecked()){
                    login_auto_cb.setChecked(false);
                }
            }
        });
    }
    private void login_auto_event() {
        login_auto_cb.setChecked(true);
        save_pwd_event();
        login();
    }
    private void save_pwd_event() {

        String username = SharePreferenceUtil.getString(LoginActivity.this, USERNAME);
        mUsernameEt.setText(username);
        int pwd_length = SharePreferenceUtil.getInt(LoginActivity.this, mUsernameEt.getText().toString());

        org_password = SharePreferenceUtil.getString(LoginActivity.this, PASSWORD);
        String password = org_password.substring(0, pwd_length);

//        Toast.makeText(this, pwd_length + "------>" + password, Toast.LENGTH_SHORT).show();
        Log.d("pwd", pwd_length + "------>" + password);

        mPasswordEt.setText(password);

        save_pwd_cb.setChecked(true);
    }

    /**
     * 登录方法
     */
    private void login() {
        //获得用户输入的内容
//        Toast.makeText(this, mPasswordEt.getText().toString(), Toast.LENGTH_SHORT).show();
        String username = mUsernameEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        if (!"".equals(SharePreferenceUtil.getString(LoginActivity.this, "password"))) {
            password = org_password = SharePreferenceUtil.getString(LoginActivity.this, "password");
        };

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!save_pwd_cb.isChecked() && login_auto_cb.isChecked()) {

            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("当勾选自动保存的时候，必须勾选上记住密码")
                    .setPositiveButton("确定", null)
                    .show();
            return;
        }
        //创建登录服务器的URL
        String url = MyUrlUtil.getURL() + "login.do";
        password = password.trim();
        //只有没有加密过的密码才可以被加密
        password = MD5Util.getMd5(password);
        Log.d("login_pwd", username + ":" + password);
        String params = "username=" + username + "&password=" + password;
        Log.e(TAG, username + "/" + password);
        //执行异步任务
        new LoginTask().execute(url, params);
    }
    /**
     * 跳转到注册界面
     */
    private void intentRegister() {
        //创建Intent对象
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

        Bundle mBundle = new Bundle();
        mBundle.putString("username", mUsernameEt.getText().toString());
        mBundle.putString("password", mPasswordEt.getText().toString());

        intent.putExtras(mBundle);
        //启动Activity
        startActivityForResult(intent,0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            mUsernameEt.setText(username);
            mPasswordEt.setText(password);
        }
    }
    /**
     * 自定义监听器
     */
    class MyOnClickListener implements OnClickListener {

        //用户点击后执行的方法，view就是被点击的控件对象
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_btn:
                    login();
                    break;
                case R.id.regeister_btn:
                    intentRegister();
                    break;
            }
        }
    }

    /**
     * 实现登陆服务器的异步任务
     */
    class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //连接服务器，并返回数据
//			return HttpUtil.get(params[0]);
            return MyHttpUtil.post(params[0], params[1]);
        }
        @Override
        protected void onPostExecute(String result) {
            //是否登录成功
            if (TextUtils.isEmpty(result)) {
                Log.e(TAG, "网络连接出错");
                Toast.makeText(LoginActivity.this, "网络连接出错", Toast.LENGTH_LONG).show();
            } else {
                result = result.trim();
                Log.e(TAG, result);
                int userId = Integer.parseInt(result);
                if (userId == 0) {
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
                } else {
                    //跳转到主界面
                    Intent intent = new Intent(LoginActivity.this,Test_ViewPager.class);

                    Bundle mBundle = new Bundle();
                    mBundle.putInt("userId", userId);
                    mBundle.putString(USERNAME, mUsernameEt.getText().toString());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    //保存账号和密码
                    if (save_pwd_cb.isChecked()) {
                        SharePreferenceUtil.putString(LoginActivity.this, USERNAME, mUsernameEt.getText().toString());
                        if ("".equals(org_password)) {
                            org_password = MD5Util.getMd5(mPasswordEt.getText().toString());
                        }
                        SharePreferenceUtil.putString(LoginActivity.this, PASSWORD, MD5Util.getMd5(org_password));

                        SharePreferenceUtil.putBoolean(LoginActivity.this, HAS_SAVE_PASSWORD, true);
                    } else {
                        SharePreferenceUtil.putString(LoginActivity.this, USERNAME, "");
                        SharePreferenceUtil.putString(LoginActivity.this, PASSWORD, "");
                        SharePreferenceUtil.putBoolean(LoginActivity.this, HAS_SAVE_PASSWORD, false);
                    }
                    //保存自动登录的状态
                    if (login_auto_cb.isChecked()) {
                        SharePreferenceUtil.putBoolean(LoginActivity.this, LOGIN_AUTO, true);
                    } else {
                        SharePreferenceUtil.putBoolean(LoginActivity.this, LOGIN_AUTO, false);
                    }
                    SharePreferenceUtil.putInt(LoginActivity.this, mUsernameEt.getText().toString(), mPasswordEt.getText().toString().length());
                    Log.d("length", "" + mPasswordEt.getText().toString().length());
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_menu:
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("说明")
                        .setMessage("我们是最好的，永远是bb哒")
                        .setPositiveButton("确定",null)
                        .show();
                return true;
            case R.id.exit_btn:
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("提示")
                        .setMessage("确定要退出吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                    return true;
            default :return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return true;
    }
}