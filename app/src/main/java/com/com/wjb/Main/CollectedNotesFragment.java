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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.entity.Note;
import com.example.administrator.cloudnote.AddActivity;
import com.example.administrator.cloudnote.R;
import com.example.administrator.cloudnote.UpdateActivity;
import com.wjb.utils.IPUtils;
import com.wjb.utils.MyHttpUtil;
import com.wjb.utils.MyUrlUtil;

import java.util.ArrayList;
import java.util.List;

public class CollectedNotesFragment extends Fragment {

    public final static String TAG = "CollectedNotesFragment";
    private String IP = IPUtils.getIPAddress();
    private ListView notes_lv;
    private List<Note> dataSourse;
    private LayoutInflater inflater;
    private MyAdapter myAdapter;
    private int userId;
    private String username;
    private AdapterView.OnItemClickListener list_view_listener;
    private int currentPosition;
    private int notes_id;
    private SearchView searchView;
    private ImageButton addNote_ib;
    private SearchView mSearchView;

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
                String url = MyUrlUtil.getURL()+"searchCollectedNotes.do";
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
        addNote_ib= (ImageButton) view.findViewById(R.id.insert_note_ibtn);
        addNote_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity().getApplicationContext(),AddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                bundle.putString("username", username);
                mIntent.putExtras(bundle);
                //用于返回结果的方法
                startActivityForResult(mIntent,0);
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
                        .setMessage("确定要取消收藏这条笔记吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e(TAG,"取消收藏笔记"+dataSourse.get(position));
                                String url = MyUrlUtil.getURL()+"deleteCollectedNotes.do";
                                String params = "note_id=" +dataSourse.get(position).getId();
                                DeleteCollectedNoteAsyncTask mDeleteNotesAsyncTask = new DeleteCollectedNoteAsyncTask();
                                mDeleteNotesAsyncTask.execute(url, params);
                            }
                        })
                        .show();
                return true;
            }});


        inflater = LayoutInflater.from(getActivity().getApplicationContext());

        notes_lv.setAdapter(myAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list_view_listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getActivity(),UpdateActivity.class);
                Bundle bundle=new Bundle();

                bundle.putSerializable("note", (Note) dataSourse.get(position));
                bundle.putString("username",username);

                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        };

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
        String url = MyUrlUtil.getURL()+"findCollectedNotes.do";
        String params = "userId=" + userId;
        Log.e(TAG, "查询收藏笔记" + url + params);
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
            return dataSourse.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            notes_lv.setOnItemClickListener(list_view_listener);
            MyAdapter.ViewHolder mViewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.note_itm, parent, false);

                TextView title = (TextView) convertView.findViewById(R.id.title_tv);
                TextView content = (TextView) convertView.findViewById(R.id.content_tv);
                TextView creating_date = (TextView) convertView.findViewById(R.id.time_tv);
                ImageButton delete_btn = (ImageButton) convertView.findViewById(R.id.delete_btn);

                mViewHolder = new MyAdapter.ViewHolder(title, content, creating_date, delete_btn);

                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyAdapter.ViewHolder) convertView.getTag();
            }
            final Note note = dataSourse.get(position);
            mViewHolder.id=note.getId();
            mViewHolder.title.setText(note.getTitle());
            mViewHolder.content.setText(note.getContent());
            mViewHolder.creating_date.setText(note.getCreating_date());
            mViewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("确定要删除这项笔记吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent mIntent = new Intent(ItemFragment.TAG+"_deleteNotes");
                                    Bundle mBundle = new Bundle();
                                    mBundle.putInt("note_id", note.getId());
                                    mIntent.putExtras(mBundle);
                                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mIntent);

                                    String url = MyUrlUtil.getURL()+"deleteCollectedNotes.do";
                                    String params = "note_id=" +dataSourse.get(position).getId();
                                    DeleteCollectedNoteAsyncTask mDeleteNotesAsyncTask = new DeleteCollectedNoteAsyncTask();
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
            public TextView creating_date;
            public ImageButton delete_btn;
            public ViewHolder( TextView title, TextView content, TextView creating_date, ImageButton delete_btn) {
                this.title = title;
                this.content = content;
                this.creating_date = creating_date;
                this.delete_btn = delete_btn;
            }
        }
    }

    /**
     * 删除某一项记录的异步任务
     */
    class DeleteCollectedNoteAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return MyHttpUtil.post(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String ps) {
            Log.d(TAG, "取消收藏笔记结果"+ps);
//            Toast.makeText(getActivity().getApplicationContext(), ps, Toast.LENGTH_SHORT).show();
            if (!TextUtils.isEmpty(ps)) {
                boolean result = ps.trim().equals("true");
                if (result) {
                    refreash();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "取消收藏笔记失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
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
                List<Note> data = JSON.parseArray(result.trim(), Note.class);
                dataSourse.clear();
                dataSourse.addAll(data);
                myAdapter.notifyDataSetChanged();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public CollectedNotesFragment() {
        // Required empty public constructor
        dataSourse = new ArrayList<Note>();
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
            view = inflater.inflate(R.layout.fragment_title, container, false);
        }

        registerReceiver();
        return view;
    }



    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action=intent.getAction();

            Log.e(TAG, "接受广播"+  action);

            if (action.equals(TAG+"_findCollectedNotes")) {
// 这地方只能在主线程中刷新UI,子线程中无效，因此用Handler来实现
            new Handler().post(new Runnable(){
                     @Override
                    public void run() {
                         refreash();
                         Log.e(TAG, "findCollectedNotes success");
                    }
                });
            }
        }
    };

    private void registerReceiver(){
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TAG+"_findCollectedNotes");
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
