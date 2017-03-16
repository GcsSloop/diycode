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
 * Last modified 2017-03-15 23:45:17
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.app.BaseImageActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.webview.DiskImageCache;
import com.gcssloop.diycode_sdk.log.Logger;

import java.util.ArrayList;

public class ImageActivity extends BaseImageActivity {

    private ArrayList<View> mItemViews = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image;
    }

    /**
     * 初始化 View， 调用位置在 initDatas 之后
     *
     * @param holder
     * @param root
     */
    @Override
    protected void initViews(ViewHolder holder, View root) {
        if (mCurrentMode == MODE_ERROR) {
            //TODO 显示错误视图
            return;
        }
        // 显示正常视图
        ViewPager viewPager = holder.get(R.id.view_pager);
        DiskImageCache mCache = new DiskImageCache(this);

        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < images.size(); i++) {
            View item = inflater.inflate(R.layout.item_image, null);
            PhotoView photoView = (PhotoView) item.findViewById(R.id.photo_view);
            photoView.enable();
            String url = images.get(i);
            if (null == url || url.isEmpty()) {
            } else {

                String image_cache_path = mCache.getDiskPath(url);
                if (image_cache_path == null || image_cache_path.isEmpty()) {
                    loadImage(url, photoView);
                    Logger.e("从网络加载:" + url);
                } else {
                    loadImage(image_cache_path, photoView);
                    Logger.e("从本地加载:" + image_cache_path);
                }

                mItemViews.add(item);
            }
        }

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mItemViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mItemViews.get(position));
                return mItemViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mItemViews.get(position));
            }
        });

        viewPager.setCurrentItem(current_image_position);
    }

    public void loadImage(String url, PhotoView photoView) {
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(photoView);
    }


}
