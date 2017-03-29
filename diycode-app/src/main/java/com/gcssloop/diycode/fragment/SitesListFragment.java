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
 * Last modified 2017-03-28 03:10:13
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.adapter.sites.SiteItem;
import com.gcssloop.diycode.adapter.sites.SiteProvider;
import com.gcssloop.diycode.adapter.sites.SitesItem;
import com.gcssloop.diycode.adapter.sites.SitesProvider;
import com.gcssloop.diycode.base.app.BaseFragment;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.multitype.MultiTypeAdapter;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.sites.bean.Sites;
import com.gcssloop.diycode_sdk.api.sites.event.GetSitesEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * topic 相关的 fragment， 主要用于显示 topic 列表
 */
public class SitesListFragment extends BaseFragment {
    // 底部状态显示
    private static final String FOOTER_LOADING = "loading...";
    private static final String FOOTER_NORMAL = "-- end --";
    private static final String FOOTER_ERROR = "-- 获取失败 --";
    private TextView mFooter;

    // 数据
    private Diycode mDiycode;                       // 在线(服务器)
    private DataCache mDataCache;                   // 缓存(本地)

    // View
    private MultiTypeAdapter mAdapter;


    public static SitesListFragment newInstance() {
        Bundle args = new Bundle();
        SitesListFragment fragment = new SitesListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDiycode = Diycode.getSingleInstance();
        mDataCache = new DataCache(getContext());
        mDataCache.getSitesItems();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sites;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        mFooter = holder.get(R.id.footer);
        initRecyclerView(getContext(), holder);
        loadData();
    }

    private void initRecyclerView(final Context context, ViewHolder holder) {
        RecyclerView recyclerView = holder.get(R.id.recycler_view);

        mAdapter = new MultiTypeAdapter();
        mAdapter.register(SiteItem.class, new SiteProvider(getContext()));
        mAdapter.register(SitesItem.class, new SitesProvider(getContext()));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.getDatas().get(position) instanceof SitesItem) ? 2 : 1;
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager.setSmoothScrollbarEnabled(true);
    }


    // 加载数据
    private void loadData() {
        List<Object> sitesList = mDataCache.getSitesItems();
        if (sitesList != null) {
            mAdapter.addDatas(sitesList);
            mFooter.setText(FOOTER_NORMAL);
        } else {
            mDiycode.getSites();
            mFooter.setText(FOOTER_LOADING);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSitesList(GetSitesEvent event) {
        //TODO 处理数据
        if (event.isOk()) {
            List<Sites> sitesList = event.getBean();
            convertData(sitesList);
            // mDataCache.saveSites(event.getBean());
        } else {
            toast("获取 sites 失败");
            mFooter.setText(FOOTER_ERROR);
        }

        mFooter.setText(FOOTER_NORMAL);
    }

    // 转换数据
    private void convertData(List<Sites> sitesList) {
        List<Object> items = new ArrayList<>();
        for (Sites sites : sitesList) {

            items.add(new SitesItem(sites.getName()));

            for (Sites.Site site : sites.getSites()) {
                items.add(new SiteItem(site.getName(), site.getUrl(), site.getAvatar_url()));
            }

            if (sites.getSites().size() % 2 == 1) {
                items.add(new SiteItem("", "", ""));
            }
        }
        mAdapter.addDatas(items);
        mDataCache.saveSitesItems(mAdapter.getDatas());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
