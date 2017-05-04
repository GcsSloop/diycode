<h1 align="center">diycode</h1>

版本: v0.1.0 (build 5) | 大小：2.7M | 2017-04-10

 [Diycode](https://www.diycode.cc/) 社区客户端，可以更方便的在手机上查看社区信息。应用采用了数据多级缓存，并且实现了离线浏览(访问过一次的页面会被缓存下来，没有网络也可查看)，相比于网页版，在一定程度上可以减少在手机上访问的流量消耗。由于目前功能尚未完善，还存在一些已知或未知的bug，所以当前版本仅为 beta 测试版。

本客户端开发过程是完全开放的，如果你想要改进客户端，或者发现了问题，可以到 [这里提交 Issues](https://github.com/GcsSloop/diycode/issues) 。当然了，你也可以基于该代码修改并开发出属于自己的客户端。

如果你有兴趣亲自开发一个 diycode 客户端的话，可以试试看 [diycode-sdk](https://github.com/GcsSloop/diycode-sdk)，它可以让你方便的拿到 diycode 社区的数据以及与 diycode 服务器交互，所有信息都会立即同步到社区上。

<p align="center">
<img src="https://ww2.sinaimg.cn/large/006tNbRwly1fe69n67gzfj30e80e8aa6.jpg" width="100" />
<br/>Diycode 社区客户端，可以离线查看 topic，更方便，更省流量。 <br/><br/>
<a href="http://www.gcssloop.com/diycode_data/diycode-app-release.apk" ><img src="https://ww2.sinaimg.cn/large/006tNbRwly1fe69o6avahj308c01omx2.jpg" width="168" /></a>
<br/>适用于 Android 设备<br/><br/>
或者用手机扫描下面二维码安装。<br/>
<img src="https://ww1.sinaimg.cn/large/006tNbRwly1fe69oo2ektj30bo0bo0ss.jpg" width="200"/><br/>
(如果是从微信，微博等打开，请点击右上角在浏览器中打开，之后点击安装)<br/>
</p>

### 应用截图

<p align="center">

<img src="https://ww3.sinaimg.cn/large/006tNbRwly1fe6av8luuuj31ay27dx18.jpg" width="260"/>

<img src="https://ww1.sinaimg.cn/large/006tNbRwly1fe6avzayilj31ay27d1ar.jpg" width="260"/>

<img src="https://ww3.sinaimg.cn/large/006tNbRwgy1fe6awl66snj31ay27dnne.jpg" width="260"/>

</p>


### 更新说明：

1. 修复首页数据过多时加载数据卡顿问题。
2. 首页界面调整
3. 添加分类查看，点击节点名称可以查看该节点下所有话题(Topic)
4. 使用 CromeCustomTabs 替换 WebView
5. Sites 页面支持手动刷新。
6. 通知、我的帖子、我的收藏 均支持下拉刷新和分页加载。
7. 重构 RecyclerView 相关代码，使下拉刷新和上拉加载代码更加简洁
8. 将部分 Activity 内容转移到 Fragment

### 历史版本

#### [diycode-v0.0.6 (2017-03-31)](http://www.gcssloop.com/diycode_data/diycode-v0.0.6.apk)

1. 修正重复打开链接导致程序异常退出

#### [diycode-v0.0.5 (2017-03-31)](http://www.gcssloop.com/diycode_data/diycode-v0.0.5.apk)

1. 修复 News 链接打开方式
2. 调整 Sites 页面条目间距和点击区域大小
3. 修正评论区图片大小

#### [diycode-v0.0.3 (2017-03-31)](http://www.gcssloop.com/diycode_data/diycode-v0.0.3.apk)

1. 修复首页滑动卡顿问题
2. 修复登录页面崩溃问题
3. 移除顶部菜单快速返回按钮
4. 添加点击FAB快速返回顶部
5. 添加双击Actionbar(Toolbar)快速返回顶部

#### [diycode-v0.0.1 (2017-03-29)](www.gcssloop.com/diycode_data/diycode-v0.0.1.apk)

1. 查看 topic 列表、详情、评论
2. 回复 topic 评论
3. 查看 news 
4. 查看 sites
5. 登录后根据用户设置获取个人 topic 列表
6. 查看用户创建的 topic
7. 查看用户收藏的 topic
8. 查看通知
9. 文章链接打开方式可选(默认使用内置浏览器)
10. topic 缓存，可离线查看
11. 缓存默认为 1 周，过期自动清除，也可手动清除

## 相关日志

日志是这个应用相关的一些设计思想，可以帮助你你更清楚代码为什么是这样子。

- [01 - 整体架构](blog/journal-01.md)
- [02 - 缓存 WebView 中的图片](blog/journal-02.md)
- [03 - 点击网页图片查看大图](blog/journal-03.md)
- [04 - API 的封装哲学](blog/journal-04.md)
- [05 - 使用抽象类节省 1000 行代码](blog/journal-05.md)

## 作者简介

#### 作者微博: [@GcsSloop](http://weibo.com/GcsSloop)

#### 个人网站: http://www.gcssloop.com

<a href="http://www.gcssloop.com/info/about/" target="_blank"> <img src="http://ww4.sinaimg.cn/large/005Xtdi2gw1f1qn89ihu3j315o0dwwjc.jpg" width="300"/> </a>



## 版权信息

```
Copyright (c) 2017 GcsSloop

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


