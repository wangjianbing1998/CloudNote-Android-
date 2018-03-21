package com.com.wjb.Main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.administrator.cloudnote.R;

import java.util.ArrayList;
import java.util.List;
/**
 * 主界面
 * @author xray
 *
 */
public class Test_ViewPager extends FragmentActivity {

    private RadioGroup mNavRg;
    private ViewPager mVp;
    private RadioButton item_rb;
    private RadioButton title_rb;
    private RadioButton history_rb;
    private ItemFragment mItemFragment;
    private HistoryFragment mHistoryFragment;
    private CollectedNotesFragment mCollectedNotesFragment;
    private List<Fragment> mFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page__test);

        initFragments();
        initViews();
    }

    private void initFragments() {
        //初始化Fragment对象
        mItemFragment = new ItemFragment();
        mHistoryFragment = new HistoryFragment();
        mCollectedNotesFragment = new CollectedNotesFragment();
        //创建集合，并添加Fragment对象
        mFragments = new ArrayList<Fragment>();
        mFragments.add(mItemFragment);
        mFragments.add(mCollectedNotesFragment);
        mFragments.add(mHistoryFragment);
    }

    private void initViews() {
        mVp = (ViewPager) findViewById(R.id.content_vp);
        mNavRg = (RadioGroup) findViewById(R.id.nav_rg);
        item_rb = (RadioButton) findViewById(R.id.item_rb);
        title_rb = (RadioButton) findViewById(R.id.title_rb);
        history_rb = (RadioButton) findViewById(R.id.history_rb);
        //给ViewPager设置适配器
        mVp.setAdapter(new FragAdapter(getSupportFragmentManager()));
//        mVp.setAdapter(new MyViewPagerAdapter(mFragments));
        //给ViewPager设置滑动监听
        mVp.setOnPageChangeListener(new OnPageChangeListener() {
            //页面切换完毕
            @Override
            public void onPageSelected(int pos) {
                switch(pos){
                    case 0:
                        item_rb.setChecked(true);
//                        mFragment=mFragments.get(0);
//                        mFragment.onHiddenChanged(true);
                        break;
                    case 1:
                        title_rb.setChecked(true);
//                        mFragment=mFragments.get(1);
//                        mFragment.onHiddenChanged(true);
                        break;
                    case 2:
                        history_rb.setChecked(true);
//                        mFragment=mFragments.get(2);
//                        mFragment.onHiddenChanged(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        //设置按钮选择事件
        mNavRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //判断哪个按钮被选中

                switch(checkedId){
                    case R.id.item_rb:
                        //修改ViewPager的当前页面
//                        mFragment=mFragments.get(0);
//                        mFragment.onHiddenChanged(true);
                        mVp.setCurrentItem(0);
                        break;
                    case R.id.title_rb:
                        mVp.setCurrentItem(1);
//                        mFragment=mFragments.get(1);
//                        mFragment.onHiddenChanged(true);
                        break;
                    case R.id.history_rb:
//                        mFragment=mFragments.get(2);
//                        mFragment.onHiddenChanged(true);
                        mVp.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    /**
     * ViewPager的Fragment适配器
     */
    class FragAdapter extends FragmentPagerAdapter{

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            Fragment mFragment=mFragments.get(position);
//            mFragment.onHiddenChanged(true);
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }

}
