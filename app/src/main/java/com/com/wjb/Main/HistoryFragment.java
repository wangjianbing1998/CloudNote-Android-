package com.com.wjb.Main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.entity.UpdateItem;
import com.example.administrator.cloudnote.R;
import com.example.administrator.cloudnote.ShowHistoryItemActivity;
import com.wjb.utils.MyHttpUtil;
import com.wjb.utils.MyUrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    public final static String TAG = "HistoryFragment";
    private ListView notes_lv;
    private List<UpdateItem> dataSourse;
    private LayoutInflater inflater;
    private MyAdapter myAdapter;
    private int userId;
    private String username;
    private AdapterView.OnItemClickListener list_view_listener;
    private SearchView searchView;
    private Button delete_all;

    private View view;
    private LocalBroadcastManager broadcastManager;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }
    //
    private void initData() {

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        userId = bundle.getInt("userId");
        username = bundle.getString("username");

        refreash();
    }
    private void initView(View view) {

        final boolean[] hasSubmit = {false};
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //提示搜索内容,当点击搜索按钮的时候就会进行搜索的操作
            @Override
            public boolean onQueryTextSubmit(String query) {
                hasSubmit[0] =true;
//                Toast.makeText(getActivity().getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                String url = MyUrlUtil.getURL()+"searchHistoryItems.do";
                String params = "userId=" + userId+"&key_words="+query;
                new MyAsyncTask().execute(url, params);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    if(hasSubmit[0]){
                        refreash();
                    }
                }
                return false;
            }
        });
        delete_all= (Button) view.findViewById(R.id.delete_all_history_items);
        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=MyUrlUtil.getURL()+"deleteAllHistoryItems.do";
                String params="user_id="+userId;
                new MyDeleteAllHistoryItemsAsyncTask().execute(url,params);
            }
        });

        notes_lv = (ListView) view.findViewById(R.id.show_lv);

        notes_lv.setOnItemClickListener(list_view_listener);
        notes_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("提醒")
                        .setNegativeButton("取消",null)
                        .setMessage("确定要删除这条记录吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e(TAG,"删除历史记录"+dataSourse.get(position));
                                String url = MyUrlUtil.getURL()+"deleteHistoryItems.do";
                                String params = "history_id=" +dataSourse.get(position).getHistory_id();
                                MyDeleteHistoryItemsAsyncTask mDeleteHistoryItemsAsyncTask = new MyDeleteHistoryItemsAsyncTask();
                                mDeleteHistoryItemsAsyncTask.execute(url, params);
                            }
                        })
                        .show();
                return true;
            }});


        inflater = LayoutInflater.from(getActivity().getApplicationContext());

        notes_lv.setAdapter(myAdapter);
    }

    class MyDeleteAllHistoryItemsAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return MyHttpUtil.post(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String pS) {
            if(!TextUtils.isDigitsOnly(pS)){
                Log.e(TAG,"DeleteAllHistoryItemsAsyncTask:"+pS);
                if(pS.trim().equals("true")){
                    Toast.makeText(getActivity(), "全部删除成功", Toast.LENGTH_SHORT).show();
                    dataSourse.clear();
                    myAdapter.notifyDataSetChanged();
                }
                else if(pS.trim().equals("false")){
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "网络连接错误请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list_view_listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getActivity(),ShowHistoryItemActivity.class);
                Bundle bundle=new Bundle();

                bundle.putSerializable("update_item", (UpdateItem) dataSourse.get(position));
                bundle.putString("username",username);

                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        };

    }



    class MyDeleteHistoryItemsAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            return MyHttpUtil.post(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String pS) {
            Log.e(TAG, "删除历史记录结果:" + pS);
            if(TextUtils.isEmpty(pS)){
                Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
            }
            else if(pS.trim().equals("true")){
                Toast.makeText(getActivity(), "删除历史记录成功", Toast.LENGTH_SHORT).show();
                refreash();
            }
            else{
                Toast.makeText(getActivity(), "删除历史记录失败,请稍后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==getActivity().RESULT_OK){
            Log.d(TAG, "refresh_ok");
            refreash();
        }
    }
    public void refreash() {
        Log.e(TAG, "refreash()");
        String url = MyUrlUtil.getURL()+"findHistoryItems.do";
        String params = "userId=" + userId;
        Log.e(TAG, "查询历史记录" + url + params);
        new MyAsyncTask().execute(url, params);
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataSourse.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSourse.get(position);
        }

        @Override
        public long getItemId(int position) {
            return dataSourse.get(position).getHistory_id();
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            notes_lv.setOnItemClickListener(list_view_listener);
            ViewHolder mViewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.history_itm, parent, false);

                TextView title = (TextView) convertView.findViewById(R.id.title_tv);
                TextView content = (TextView) convertView.findViewById(R.id.content_tv);
                TextView creating_date = (TextView) convertView.findViewById(R.id.time_tv);
                ImageButton delete_btn = (ImageButton) convertView.findViewById(R.id.delete_btn);
                TextView update_type = (TextView) convertView.findViewById(R.id.update_type);

                mViewHolder = new ViewHolder(title, content, creating_date, delete_btn,update_type);

                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            final UpdateItem mUpdateItem = dataSourse.get(position);
            mViewHolder.id=mUpdateItem.getHistory_id();
            mViewHolder.title.setText(mUpdateItem.getAfter_title()==null?mUpdateItem.getBefore_title():mUpdateItem.getAfter_title());
            mViewHolder.content.setText(mUpdateItem.getAfter_content()==null?mUpdateItem.getBefore_content():mUpdateItem.getAfter_content());
            mViewHolder.creating_date.setText(mUpdateItem.getUpdate_time());
            String update_type = "null";
            switch (mUpdateItem.getUpdate_type()){
                case 1:update_type="增加";break;
                case 2:update_type="删除";break;
                case 3:update_type="查询";break;
                case 4:update_type="修改";break;
            }
            mViewHolder.update_type.setText(update_type);

            mViewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("确定要删除这项历史记录吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String url = MyUrlUtil.getURL()+"deleteHistoryItems.do";
                                    String params = "history_id=" +dataSourse.get(position).getHistory_id();
                                    MyDeleteHistoryItemsAsyncTask mDeleteNotesAsyncTask =new MyDeleteHistoryItemsAsyncTask();
                                    mDeleteNotesAsyncTask.execute(url, params);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }

            });
            return convertView;
        }
        class ViewHolder {
            public int id;
            public TextView title;
            public TextView content;
            public TextView update_type;
            public TextView creating_date;
            public ImageButton delete_btn;
            public ViewHolder( TextView title, TextView content, TextView creating_date, ImageButton delete_btn,TextView update_type) {
                this.title = title;
                this.content = content;
                this.creating_date = creating_date;
                this.delete_btn = delete_btn;
                this.update_type = update_type;
            }
        }
    }
    /***
     * 查询user_id的所有笔记的异步任务
     */
    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return MyHttpUtil.post(params[0], params[1]);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG,result);
            if (!TextUtils.isEmpty(result)) {
                List<UpdateItem> data = JSON.parseArray(result.trim(), UpdateItem.class);
                dataSourse.clear();
                dataSourse.addAll(data);
                myAdapter.notifyDataSetChanged();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public HistoryFragment() {
        // Required empty public constructor
        dataSourse = new ArrayList<UpdateItem>();
        myAdapter = new MyAdapter();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_history, container, false);
        }

        registerReceiver();
        return view;
    }



    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action=intent.getAction();

            Log.e(TAG, "接受广播"+  action);

            if (action.equals(TAG+"_findHistoryItems")) {
// 这地方只能在主线程中刷新UI,子线程中无效，因此用Handler来实现
                new Handler().post(new Runnable(){
                    @Override
                    public void run() {
                        refreash();
                        Log.e(TAG, "findHistoryItems success");
                    }
                });
            }
        }
    };

    private void registerReceiver(){
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TAG+"_findHistoryItems");
        broadcastManager.registerReceiver(mAdDownLoadReceiver, intentFilter);
        Log.e(TAG, "register Broadcast success");
    }
    /**
     * 注销广播
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
    }

}
