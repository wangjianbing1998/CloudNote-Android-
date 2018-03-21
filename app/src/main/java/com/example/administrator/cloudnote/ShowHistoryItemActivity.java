package com.example.administrator.cloudnote;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.entity.UpdateItem;
import com.wjb.utils.MyHttpUtil;
import com.wjb.utils.MyTimeUtils;
import com.wjb.utils.MyUrlUtil;

public class ShowHistoryItemActivity extends Activity {


    private final static String TAG = "ShowHistoryItemActivity";
    private TextView before_title_et;
    private EditText after_title_et;
    private TextView before_content_et;
    private EditText after_content_et;
    private TextView time_tv;
    private TextView auther_tv;
    private UpdateItem mUpdateItem;

    private ImageButton save_ibtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history_item);

        initView();

        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mUpdateItem = (UpdateItem) bundle.getSerializable("update_item");
        String auther=bundle.getString("username");

        before_title_et.setText(mUpdateItem.getBefore_title());
        after_title_et.setText(mUpdateItem.getAfter_title());
        before_content_et.setText(mUpdateItem.getBefore_content());
        after_content_et.setText(mUpdateItem.getAfter_content());
        time_tv.setText("当前时间:"+ MyTimeUtils.getDate());
        auther_tv.setText(auther);

        save_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotes();
            }
        });
    }

    private void initView() {

        before_title_et = (TextView) findViewById(R.id.before_title_tv);
        after_title_et = (EditText) findViewById(R.id.after_title_et);
        before_content_et = (TextView) findViewById(R.id.before_content_tv);
        after_content_et = (EditText) findViewById(R.id.after_content_et);

        time_tv = (TextView) findViewById(R.id.time_tv);
        auther_tv = (TextView) findViewById(R.id.auther_tv);

        save_ibtn = (ImageButton) findViewById(R.id.save_ibtn);
    }

    public void updateNotes(){
        String after_title = after_title_et.getText().toString();
        String after_content=after_content_et.getText().toString();

        if(TextUtils.isEmpty(after_title)){
            Toast.makeText(ShowHistoryItemActivity.this, "标题不能够为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(after_content)){
            Toast.makeText(ShowHistoryItemActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = MyUrlUtil.getURL()+"updateHistoryItems.do";
        String params = "history_id=" + mUpdateItem.getHistory_id()
                +"&after_title="+after_title_et.getText().toString()
                +"&after_content="+after_content_et.getText().toString()
                +"&update_time=" + MyTimeUtils.getDate();
        new UpdateHistoryItemsAsyncTask().execute(url, params);
    }

    class UpdateHistoryItemsAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            return MyHttpUtil.post(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String pS) {
            if(TextUtils.isEmpty(pS)){
                Toast.makeText(ShowHistoryItemActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                pS = pS.trim();
                Log.e(TAG, pS);
                if(pS.equals("true")){
                    //通知列表界面刷新
                    setResult(RESULT_OK);
                    finish();
                }
                else{
                    Toast.makeText(ShowHistoryItemActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
