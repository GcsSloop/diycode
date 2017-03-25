/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.activity;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.fragment.NewsListFragment;
import com.gcssloop.diycode.fragment.TextFragment;
import com.gcssloop.diycode.fragment.TopicListFragment;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode_sdk.api.login.event.LogoutEvent;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.api.user.event.GetMeEvent;
import com.gcssloop.diycode_sdk.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DataCache mCache;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(ViewHolder holder, View root) {
        EventBus.getDefault().register(this);
        mCache = new DataCache(this);
        initMenu(holder);
        initViewPager(holder);
    }

    //--- viewpager adapter ------------------------------------------------------------------------

    private void initViewPager(ViewHolder holder) {
        // TODO 此处使用的是测试账号，登录页面完成后移除
        ViewPager mViewPager = holder.get(R.id.view_pager);
        TabLayout mTabLayout = holder.get(R.id.tab_layout);
        mViewPager.setOffscreenPageLimit(2); // 防止滑动到第三个页面时，第一个页面被销毁
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] types = {"Topics", "News", "Sites"};

            @Override
            public Fragment getItem(int position) {
                if (position == 0)
                    return TopicListFragment.newInstance();
                if (position == 1)
                    return NewsListFragment.newInstance();
                return TextFragment.newInstance(types[position]);
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return types[position];
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    // 如果收到此状态说明用户已经登录成功了
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(GetMeEvent event) {
        if (event.isOk()) {
            UserDetail me = event.getBean();
            mCache.saveMe(me);
            loadMenuData(); // 加载菜单数据
        }
    }

    // 如果收到此状态说明用户登出了
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(LogoutEvent event) {
        loadMenuData(); // 加载菜单数据
    }

    //--- menu -------------------------------------------------------------------------------------

    // 初始化菜单(包括侧边栏菜单和顶部菜单选项)
    private void initMenu(ViewHolder holder) {
        Toolbar toolbar = holder.get(R.id.toolbar);
        toolbar.setLogo(R.mipmap.logo_actionbar);
        toolbar.setTitle("");
        DrawerLayout drawer = holder.get(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Diycode", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }, R.id.fab);

        loadMenuData();
    }

    // 加载侧边栏菜单数据(与用户相关的)
    private void loadMenuData() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView avatar = (ImageView) headerView.findViewById(R.id.nav_header_image);
        TextView username = (TextView) headerView.findViewById(R.id.nav_header_name);
        TextView tagline = (TextView) headerView.findViewById(R.id.nav_header_tagline);

        if (mDiycode.isLogin()) {
            UserDetail me = mCache.getMe();
            if (me == null) {
                Logger.e("获取自己缓存失效");
                mDiycode.getMe();   // 重新加载
                return;
            }

            username.setText(me.getLogin());
            tagline.setText(me.getTagline());
            Glide.with(this).load(me.getAvatar_url()).into(avatar);
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 打开自己详情页
                }
            });
        } else {
            mCache.removeMe();
            username.setText("(未登录)");
            tagline.setText("点击头像登录");
            avatar.setImageResource(R.mipmap.ic_launcher);
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openActivity(LoginActivity.class);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_post) {
            if (!mDiycode.isLogin()) {
                openActivity(LoginActivity.class);
                return true;
            }
            MyTopicActivity.newInstance(this, MyTopicActivity.InfoType.MY_TOPIC);
        } else if (id == R.id.nav_collect) {
            if (!mDiycode.isLogin()) {
                openActivity(LoginActivity.class);
                return true;
            }
            MyTopicActivity.newInstance(this, MyTopicActivity.InfoType.MY_COLLECT);
        } else if (id == R.id.nav_about) {
                openActivity(AboutActivity.class);
        } else if (id == R.id.nav_setting) {
                openActivity(SettingActivity.class);
        } else if (id == R.id.nav_logout) {
            mDiycode.logout();  // 用户登出
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
