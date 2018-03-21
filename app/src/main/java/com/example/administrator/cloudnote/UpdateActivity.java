package com.example.administrator.cloudnote;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.entity.Note;
import com.jwkj.libzxing.OnQRCodeScanCallback;
import com.jwkj.libzxing.QRCodeManager;
import com.jwkj.libzxingdemo.runtimepermissions.PermissionsManager;
import com.jwkj.libzxingdemo.runtimepermissions.PermissionsResultAction;
import com.jwkj.libzxingdemo.utils.QRCodeFileUtils;
import com.wjb.utils.IPUtils;
import com.wjb.utils.MyHttpUtil;
import com.wjb.utils.MyTimeUtils;
import com.wjb.utils.MyUrlUtil;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity  implements OnMenuItemClickListener, OnMenuItemLongClickListener {

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private UpdateFragment updateFragment;


    public final static String TAG="UpdateActivity";
    private String IP = IPUtils.getIPAddress();

    private EditText title_et;
    private EditText content_et;
    private TextView time_tv;
    private TextView auther_tv;
    private Note mNote;

    private ImageButton save_ibtn;
    private Button create_qr;
    private Button scan_qr;
    private ImageView mImageView;

    private Bitmap qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        fragmentManager = getSupportFragmentManager();
        //initToolbar();
        initMenuFragment();


        updateFragment = (UpdateFragment) fragmentManager.findFragmentById(R.id.container);
        initView();

        initData();
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject("关闭");
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("收藏");
        send.setResource(R.drawable.icn_1);

        MenuObject like = new MenuObject("点赞");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icn_2);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("分享");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("喜欢");
        addFav.setResource(R.drawable.icn_4);



        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        return menuObjects;
    }



    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

        switch (position){
            case 0:
                break;
            case 1:
                Toast.makeText(this, "已成功收藏", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "已点赞", Toast.LENGTH_SHORT).show();
                break;
            case 3:

                //Toast.makeText(this, "已分享", Toast.LENGTH_SHORT).show();
                String information="title="+title_et.getText().toString()+"&content="+content_et.getText().toString();
                Log.e(TAG, information);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.app_logo);
                qrCode= QRCodeManager.getInstance().createQRCode(information, 300, 300, bitmap);
                mImageView.setImageBitmap(qrCode);
                Log.e(TAG, information);


                break;
            case 4:
                Toast.makeText(this, "已喜欢", Toast.LENGTH_SHORT).show();
                break;

        }
//        Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_LONG).show();
    }









    private void initData() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mNote = (Note) bundle.getSerializable("note");
        String auther=bundle.getString("username");

        title_et.setText(mNote.getTitle());
        time_tv.setText(MyTimeUtils.getDate());
        content_et.setText(mNote.getContent());
        auther_tv.setText(auther);
        create_qr = (Button) findViewById(R.id.create_qr);
        scan_qr= (Button) findViewById(R.id.scan_qr);
        mImageView = (ImageView) findViewById(R.id.img_qr);

        requestPermission();


        create_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String information="title="+title_et.getText().toString()+"&content="+content_et.getText().toString();
                Log.e(TAG, information);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.app_logo);
                qrCode= QRCodeManager.getInstance().createQRCode(information, 300, 300, bitmap);
                mImageView.setImageBitmap(qrCode);
                Log.e(TAG, information);
            }
        });

        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeManager.getInstance()
                        .with(UpdateActivity.this)
                        .setReqeustType(1)
                        .scanningQRCode(new OnQRCodeScanCallback() {
                            @Override
                            public void onCompleted(String result) {
                                Log.e(TAG, "二维码扫描结果：" + result);

                                if(result.startsWith("title=")){
                                    char[] mChar = result.toCharArray();
                                    int length = result.length();
                                    int i;
                                    for(i=6;i<length;i++){
                                        if(mChar[i]=='&'){
                                            break;
                                        }
                                    }
                                    String title = result.substring(6, i);
                                    i+=9;
                                    String content = result.substring(i);

                                    title_et.setText(title);
                                    content_et.setText(content);
                                }
                                else if(result.startsWith("http")){
                                    Uri uri = Uri.parse(result);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(UpdateActivity.this, "错误的二维码！无法识别!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable errorMsg) {
                                Log.e(TAG, "二维码扫描错误：" + errorMsg);
                                Toast.makeText(UpdateActivity.this, "错误结果:"+errorMsg, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {

                                Toast.makeText(UpdateActivity.this, "扫描任务被取消", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "扫描任务被取消");
                            }
                        });
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeFileUtils.saveBitmapFile(UpdateActivity.this,qrCode);
                Log.e(TAG,"(保存)二维码成功保存至 " + QRCodeFileUtils.KEY_QR_CODE_DIR);
                Toast.makeText(UpdateActivity.this, "(保存)二维码成功保存至 \" + QRCodeFileUtils.KEY_QR_CODE_DIR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {

        title_et = (EditText) findViewById(R.id.title_et);
        content_et = (EditText) findViewById(R.id.content_et);

        time_tv = (TextView) findViewById(R.id.time_tv);
        auther_tv = (TextView) findViewById(R.id.auther_tv);

        save_ibtn = (ImageButton) findViewById(R.id.save_ibtn);
        save_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotes();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册onActivityResult
        QRCodeManager.getInstance().with(this).onActivityResult(requestCode, resultCode, data);
    }
    private void requestPermission() {
        /**
         * 请求所有必要的权限
         */
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(UpdateActivity.this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateNotes(){
        String title = title_et.getText().toString();
        String content=content_et.getText().toString();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(UpdateActivity.this, "标题不能够为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(content)){
            Toast.makeText(UpdateActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = MyUrlUtil.getURL()+"updateNotes.do";
        String params = "note_id=" + mNote.getId()
                +"&title="+title_et.getText().toString()
                +"&content="+content_et.getText().toString();
        new UpdateNoteAsyncTask().execute(url, params);
    }

    class UpdateNoteAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return MyHttpUtil.post(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String pS) {

            if(TextUtils.isEmpty(pS)){
                Toast.makeText(UpdateActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                pS = pS.trim();
                Log.d("update", pS);
                boolean result=pS.equals("true");
                if(result){

                    Log.e(TAG, "更新成功，接下来修改历史记录");
                    String url=MyUrlUtil.getURL()+"insertHistoryItems.do" ;
                    String params="notes_id="+mNote.getId()
                            +"&update_time="+MyTimeUtils.getDateTime()
                            +"&update_type="+4
                            +"&before_title="+mNote.getTitle()
                            +"&before_content="+mNote.getContent()
                            +"&after_title="+title_et.getText().toString()
                            +"&after_content="+content_et.getText().toString();
                    new MyInsertHistoryItemsAsyncTask().execute(url, params);
                }
                else{
                    Toast.makeText(UpdateActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class MyInsertHistoryItemsAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return MyHttpUtil.post(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String pS) {

            if(TextUtils.isEmpty(pS)){
                Toast.makeText(UpdateActivity.this, "修改历史记录时候网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                pS = pS.trim();
                boolean result=pS.equals("true");
                if(result){
                    //通知列表界面刷新
                    Log.e(TAG, "修改历史记录成功");
                    setResult(RESULT_OK);
                    finish();
                }
                else{
                    Log.e(TAG, "修改历史记录失败");
                    Toast.makeText(UpdateActivity.this, "修改历史记录失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
