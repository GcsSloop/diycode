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
 * Last modified 2017-04-09 14:32:41
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gcssloop.diycode.fragment.base.RefreshRecyclerFragment;
import com.gcssloop.diycode.fragment.bean.SiteItem;
import com.gcssloop.diycode.fragment.bean.SitesItem;
import com.gcssloop.diycode.fragment.provider.SiteProvider;
import com.gcssloop.diycode.fragment.provider.SitesProvider;
import com.gcssloop.diycode_sdk.api.sites.bean.Sites;
import com.gcssloop.diycode_sdk.api.sites.event.GetSitesEvent;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.recyclerview.adapter.multitype.HeaderFooterAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页 sites 列表
 */
public class SitesListFragment extends RefreshRecyclerFragment<Sites, GetSitesEvent> {

    public static SitesListFragment newInstance() {
        Bundle args = new Bundle();
        SitesListFragment fragment = new SitesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void initData(HeaderFooterAdapter adapter) {
        setLoadMoreEnable(true);
        List<Serializable> sitesList = mDataCache.getSitesItems();
        if (sitesList != null) {
            Logger.e("sites : " + sitesList.size());
            mAdapter.addDatas(sitesList);
            setLoadMoreEnable(false);
        } else {
            loadMore();
        }
    }

    @Override
    protected void setAdapterRegister(Context context, RecyclerView recyclerView,
                                      HeaderFooterAdapter adapter) {
        mAdapter.register(SiteItem.class, new SiteProvider(getContext()));
        mAdapter.register(SitesItem.class, new SitesProvider(getContext()));
    }

    @NonNull @Override protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.getFullDatas().get(position) instanceof SiteItem) ? 1 : 2;
            }
        });
        return layoutManager;
    }

    @NonNull @Override protected String request(int offset, int limit) {
        return mDiycode.getSites();
    }

    @Override protected void onRefresh(GetSitesEvent event, HeaderFooterAdapter adapter) {
        toast("刷新成功");
        convertData(event.getBean());
    }

    @Override protected void onLoadMore(GetSitesEvent event, HeaderFooterAdapter adapter) {
        toast("加载成功");
        convertData(event.getBean());
    }

    @Override protected void onError(GetSitesEvent event, String postType) {
        toast("获取失败");
    }

    // 转换数据
    private void convertData(final List<Sites> sitesList) {
        ArrayList<Serializable> items = new ArrayList<>();
        for (Sites sites : sitesList) {

            items.add(new SitesItem(sites.getName()));

            for (Sites.Site site : sites.getSites()) {
                items.add(new SiteItem(site.getName(), site.getUrl(), site.getAvatar_url()));
            }

            if (sites.getSites().size() % 2 == 1) {
                items.add(new SiteItem("", "", ""));
            }
        }

        mAdapter.clearDatas();
        mAdapter.addDatas(items);
        mDataCache.saveSitesItems(items);
        setLoadMoreEnable(false);
    }
}