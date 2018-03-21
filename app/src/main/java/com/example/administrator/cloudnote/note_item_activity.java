package com.example.administrator.cloudnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class note_item_activity extends Activity {

    private TextView titleTv;
    private TextView contentTv;
    private TextView timeTv;
    private TextView autherTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_item_activity);


        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        String title=bundle.getString("title");
        String content=bundle.getString("content");
        String time=bundle.getString("time");
        String auther=bundle.getString("auther");

        titleTv = (TextView) findViewById(R.id.title_tv);
        contentTv = (TextView) findViewById(R.id.content_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        autherTv = (TextView) findViewById(R.id.auther_tv);

        titleTv.setText(title);
        contentTv.setText(content);
        timeTv.setText(time);
        autherTv.setText(auther);

    }
}
