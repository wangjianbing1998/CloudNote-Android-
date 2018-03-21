package com.example.administrator.cloudnote;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wjb.utils.*;
import com.wjb.utils.MyHttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddLoveActivity extends Fragment {
//    private String IP = IPUtils.getIPAddress();
//    private ListView notes_lv;
//    private List<com.entity.Note> dataSourse;
//    private LayoutInflater inflater;
//    private MyAdapter myAdapter;
//    private int userId;
//    private String username;
//    private AdapterView.OnItemClickListener list_view_listener;
//    private int currentPosition;
//    private int notes_id;
//    private SearchView searchView;

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initView(view);
//        initData();
//    }
//
//    private void initData() {
//
//        Intent intent = getActivity().getIntent();
//        Bundle bundle = intent.getExtras();
//        userId = bundle.getInt("userId");
//        username = bundle.getString("username");
//
//        Toast.makeText(getActivity(), username, Toast.LENGTH_SHORT).show();
//        refreash();
//    }
//    //
//    private void initView(View view) {
//
//        searchView = (SearchView) view.findViewById(R.id.search_view);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            //提示搜索内容,当点击搜索按钮的时候就会进行搜索的操作
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                Toast.makeText(getActivity().getApplicationContext(), query, Toast.LENGTH_SHORT).show();
//                String url = "http://" + IP + ":8080/MyCloudNote/searchNotes.do";
//                String params = "userId=" + userId+"&key_words="+query;
//                new MyAsyncTask().execute(url, params);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if(TextUtils.isEmpty(newText)){
//                    refreash();
//                }
//                return false;
//            }
//        });
//
//        notes_lv = (ListView) view.findViewById(R.id.show_lv);
//
//        notes_lv.setOnItemClickListener(list_view_listener);
//
//        dataSourse = new ArrayList<Note>();
//
//        inflater = LayoutInflater.from(getActivity().getApplicationContext());
//
//        myAdapter = new MyAdapter();
//        notes_lv.setAdapter(myAdapter);
//        notes_lv.setOnItemClickListener(list_view_listener);
//    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        list_view_listener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                Toast.makeText(getActivity().getApplicationContext(), "点击了列表listView", Toast.LENGTH_SHORT).show();
//
//                new AlertDialog.Builder(getActivity())
//                        .setTitle("提示")
//                        .setMessage("确认要收藏这条笔记吗？")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Set<String> data=SharePreferenceUtil.getStringSet(getActivity(),""+userId);
//                                data.add(""+dataSourse.get(position).getNotes_id());
//                                SharePreferenceUtil.putStringSet(getActivity(),""+userId,data);
//                            }
//                        })
//                        .setNegativeButton("取消",null)
//                        .show();
//            }
//        };
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode==getActivity().RESULT_OK){
//            Log.d("refresh", "refresh_ok");
//            refreash();
//        }
//    }
//
//    private void refreash() {
//        String url = MyUrlUtil.getURL()+"findNotes.do";
//        String params = "userId=" + userId;
//        new MyAsyncTask().execute(url, params);
//    }
//    class MyAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return dataSourse.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return dataSourse.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return dataSourse.get(position).getId();
//        }
//
//        @Override
//        public View getView(final int position, View convertView, final ViewGroup parent) {
//
//            notes_lv.setOnItemClickListener(list_view_listener);
//            MyAdapter.ViewHolder mViewHolder = null;
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.note_itm, parent, false);
//
//                TextView title = (TextView) convertView.findViewById(R.id.title_tv);
//                TextView content = (TextView) convertView.findViewById(R.id.content_tv);
//                TextView creating_date = (TextView) convertView.findViewById(R.id.time_tv);
//                ImageButton delete_btn = (ImageButton) convertView.findViewById(R.id.delete_btn);
//
//                mViewHolder = new MyAdapter.ViewHolder(title, content, creating_date, delete_btn);
//
//                convertView.setTag(mViewHolder);
//            } else {
//                mViewHolder = (MyAdapter.ViewHolder) convertView.getTag();
//            }
//            final com.entity.Note note = dataSourse.get(position);
//            mViewHolder.id=note.getId();
//            mViewHolder.title.setText(note.getTitle());
//            mViewHolder.content.setText(note.getContent());
//            mViewHolder.creating_date.setText(note.getCreating_date());
//            mViewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(getActivity().getApplicationContext())
//                            .setTitle("提示")
//                            .setMessage("确定要删除这项笔记吗？")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    String url = MyUrlUtil.getURL()+"deleteNotes.do";
////                                    Toast.makeText(getActivity().getApplicationContext(), ""+note.getId(), Toast.LENGTH_SHORT).show();
//                                    String params = "note_id=" +note.getId();
//                                    DeleteNoteAsyncTask mDeleteNotesAsyncTask = new DeleteNoteAsyncTask();
//                                    mDeleteNotesAsyncTask.execute(url, params);
//                                }
//                            })
//                            .setNegativeButton("取消", null)
//                            .show();
//                }
//
//            });
//            return convertView;
//        }
//        class ViewHolder {
//            public int id;
//            public TextView title;
//            public TextView content;
//            public TextView creating_date;
//            public ImageButton delete_btn;
//            public ViewHolder( TextView title, TextView content, TextView creating_date, ImageButton delete_btn) {
//                this.title = title;
//                this.content = content;
//                this.creating_date = creating_date;
//                this.delete_btn = delete_btn;
//            }
//        }
//    }
//
//    /**
//     * 删除某一项记录的异步任务
//     */
//    class DeleteNoteAsyncTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            return MyHttpUtil.post(params[0], params[1]);
//        }
//
//        @Override
//        protected void onPostExecute(String ps) {
//            ps = ps.trim();
//            Log.d("delete", ps);
//            Toast.makeText(getActivity().getApplicationContext(), ps, Toast.LENGTH_SHORT).show();
//            if (!TextUtils.isEmpty(ps)) {
//                boolean result = ps.equals("true");
//                if (result) {
//                    dataSourse.remove(currentPosition);
//                    myAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getActivity().getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getActivity().getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    /***
//     * 查询user_id的所有笔记的异步任务
//     */
//    class MyAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            return "123";
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            result = result.trim();
//            Log.d("main",result);
//            if (!TextUtils.isEmpty(result)) {
//                List<com.entity.Note> data = JSON.parseArray(result, com.entity.Note.class);
//                dataSourse.clear();
//                dataSourse.addAll(data);
//                myAdapter.notifyDataSetChanged();
//            }
//        }
//
//
//    }
//
    public AddLoveActivity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_item, container, false);
    }
//

}
