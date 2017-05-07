# 05 - 使用抽象类节省 1000 行代码

diycode v0.1.0 发布了，你可以到这里 [查看更新](http://www.gcssloop.com/diycode/) ，[查看源码](https://github.com/GcsSloop/diycode) 。

这是发布的第 5 个版本，修正了之前版本存在的问题，并添加了一点新功能，可以看到这次版本号从 v0.0.6 直接跳到了 v0.1.0，是一次比较大的变动，但是当你打开 app 会发现界面内容变化并不大。

这是因为在新版本中主要是优化了内部代码逻辑，虽然 UI 变化不大，甚至新添加了一个页面，和一些新功能，代码总量却减少了几百行，相关的几个页面代码量更是减少了近 1000 行，正是因为如此，我才特地水一篇文章，来讲讲我的优化思路。

**其实优化思路很简单，就是把 复制粘贴(Ctrl+C/V) 进行抽象处理。**

在 diycode 里面又很多页面其实是很相似的，但又存在区别。例如首页的三个页面 topic、news、sites、通知、用户话题、用户收藏等。 

![S70410-01500766-2](https://ww4.sinaimg.cn/large/006tNc79gy1fei0ezr0hfj31jk0wq46w.jpg)

虽然看起来有点不太一样，但它们都是 RecyclerView，还是有很多相似内容的。

它们有很多共同的特点：

1. 主体都是 RecyclerView。
2. 都具有下拉刷新。
3. 都具有分页加载。
4. 都需要管理状态(正常，正在刷新，正在加载，刷新成功，加载成功，数据获取失败)。

也有很多不同的特点：

1. 请求的数据类型不同。
2. 条目内容不同。
3. 布局不同。

所以我们就要想办法把它们的共性抽取出来一同处理，不同的地方让自己去实现，先看一下完全抽象处理后的一个页面代码。这个是首页的 topic fragment，它具有 下拉刷新、上拉加载、数据缓存、滚动状态保存和恢复（即从哪个位置退出的，下次进入还是这个位置）等功能：

```java
/**
 * 首页 topic 列表
 */
public class TopicListFragment extends SimpleRefreshRecyclerFragment<Topic, GetTopicsListEvent> {

    private boolean isFirstLaunch = true;

    public static TopicListFragment newInstance() {
        Bundle args = new Bundle();
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void initData(HeaderFooterAdapter adapter) {
      	// 优先从缓存中获取数据，如果是第一次加载则恢复滚动位置，如果没有缓存则从网络加载
        List<Object> topics = mDataCache.getTopicsListObj();
        if (null != topics && topics.size() > 0) {
            Logger.e("topics : " + topics.size());
            pageIndex = mConfig.getTopicListPageIndex();
            adapter.addDatas(topics);
            if (isFirstLaunch) {
                int lastPosition = mConfig.getTopicListLastPosition();
                mRecyclerView.getLayoutManager().scrollToPosition(lastPosition);
                isFirstAddFooter = false;
                isFirstLaunch = false;
            }
        } else {
            loadMore();
        }
    }

    @Override protected void setAdapterRegister(Context context, RecyclerView recyclerView,
                                                HeaderFooterAdapter adapter) {
        adapter.register(Topic.class, new TopicProvider(getContext()));
    }

    @NonNull @Override protected String request(int offset, int limit) {
        return mDiycode.getTopicsList(null, null, offset, limit);
    }

    @Override protected void onRefresh(GetTopicsListEvent event, HeaderFooterAdapter adapter) {
        super.onRefresh(event, adapter);
        mDataCache.saveTopicsListObj(adapter.getDatas());
    }

    @Override protected void onLoadMore(GetTopicsListEvent event, HeaderFooterAdapter adapter) {
        super.onLoadMore(event, adapter);
        mDataCache.saveTopicsListObj(adapter.getDatas());
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        // 存储 PageIndex
        mConfig.saveTopicListPageIndex(pageIndex);
        // 存储 RecyclerView 滚动位置
        View view = mRecyclerView.getLayoutManager().getChildAt(0);
        int lastPosition = mRecyclerView.getLayoutManager().getPosition(view);
        mConfig.saveTopicListState(lastPosition, 0);
    }
}
```

这个是首页 topic 列表的页面，经过抽象处理后，到具体实现部分就只剩下不到一百行的代码，而且哪个方法做什么都很清楚，不会混乱。

虽然最终实现看起来比较简单，但是想要写到这么简单却不容易，在这个项目中，它是基于我自己实现的 MultiTypeAdapter 和抽象的 Fragment 才能实现的。

先说 MultiTypeAdapter，在上面代码中可以看到一个名为 HeaderFooterAdapter 的东西，它本质上是一个 MultiTypeAdapter，为了方便的给 RecyclerView 添加头部和尾部信息菜实现出来的一个类。关于它的代码你可以到 [diycode/recyclerview](https://github.com/GcsSloop/diycode/tree/master/recyclerview) 查看，使用它可以保证 RecyclerView 中的内容不受类型限制。

再说抽象 Fragment，上面的 Fragment 是继承自 SimpleRefreshRecyclerFragment 的，实际上关于 Fragment 我进行了多层抽象，为了易用性和扩展性，其继承结构如下。

```
Fragment(系统)
  +- BaseFragment
 	   +- RefreshRecyclerFragment
 	   	    +- SimpleRefreshRecyclerFragment
 	   	    	+- NodeTopicListFragment(最终页面)
```

先说明一下其中各个 Fragment 的作用:

| Fragment                                 | 作用                                   |
| ---------------------------------------- | ------------------------------------ |
| [BaseFragment](https://github.com/GcsSloop/diycode/blob/3b33708ca93c336d23c659bd8046fa5fc6e7cc6b/diycode-app/src/main/java/com/gcssloop/diycode/fragment/base/BaseFragment.java) | 自定义的 Fragment 基类，主要用于 View 布局管理。     |
| [RefreshRecyclerFragment](https://github.com/GcsSloop/diycode/blob/8576907421edf65278a3e142fdb7ae216583cc59/diycode-app/src/main/java/com/gcssloop/diycode/fragment/base/RefreshRecyclerFragment.java) | 实现了下拉刷新和上拉加载功能的 Fragment，用于管理状态。     |
| [SimpleRefreshRecyclerFragment](https://github.com/GcsSloop/diycode/blob/3b33708ca93c336d23c659bd8046fa5fc6e7cc6b/diycode-app/src/main/java/com/gcssloop/diycode/fragment/base/SimpleRefreshRecyclerFragment.java) | 实现了部分 RefreshRecyclerFragment 的抽象方法。 |
| [NodeTopicListFragment](https://github.com/GcsSloop/diycode/blob/8576907421edf65278a3e142fdb7ae216583cc59/diycode-app/src/main/java/com/gcssloop/diycode/fragment/NodeTopicListFragment.java) | 实现该页面需要实现的部分，网络请求，处理条目内容等。           |

虽然继承了好几层，看起来类很多，但是具体到每一个类的功能都很明确，简单，只完成一种功能(单一职责)，每一个类的代码量都不到 300 行。

而对于其中不同的部分，在超(父)类中无法确定或者无法完成的，就用抽象方法强制子类去实现，这样可以确保子类实现的时候不会遗忘掉这些内容。具体代码实现就不贴在这里了，大家感兴趣的话可以直接点击上面表格中的名称来查看源码。

这种多层抽象有很多好处。

一、可复用性强。

不同层级实现了不同功能，按需继承即可，代码可复用性强。

如果只是一个普通的 Fragment ，那么直接继承 BaseFragment 就可以了。
如果是需要下拉刷新和上拉加载的 Fragment，继承 RefreshRecyclerFragment 可以免去状态管理的烦恼，只用处理网络请求和返回数据就行了。
如果这个 Fragment 不需要对数据进行特殊处理，使用线性布局(LinerLayoutManager)，那就更方便了，继承 SimpleRefreshRecyclerFragment 会让你爽到爆，只用手写几行代码完成这个页面的功能。

二、防手残。

毕竟我不是电脑，很容易忽略掉一些细节，而抽象类的抽象方法会强制要求子类实现，一般看到方法名称就知道该方法是做什么的了，可以防止忽略掉一些细节导致程序 bug。

三、修 bug 简单。

假如发现一个共有的 bug ，只需要在上层方法中修复一次即可，简单方便。

也许上面的代码你觉得也就那样，那么和上一个版本的代码对比一下就知道了这份代码有多么好了(代码在文章最后)。上一个版本是抽象处理前的，每个页面代码量接近 300 行，类似的页面一共有 7 个。优化代码可以让每个页面节省近 200 行，也就是说，理论上可以节省 7 x 200 = 1400 行代码。

**我觉得如果一个东西复制，粘贴了三次以上，就应该考虑一下是否需要将这一部分优化一下，抽取一个公共方法或者抽象类，否则这部分代码会在后期的修改过程中逐渐腐烂掉，很容易从 1 份代码的 copy 逐渐变成 n 份不同的代码，最后会变的自己都很难去修改。**

**最后附上 v0.0.6 版本的 topic fragment**

```java
/**
 * topic 相关的 fragment， 主要用于显示 topic 列表
 */
public class TopicListFragment extends BaseFragment {
    // 底部状态显示
    private static final String FOOTER_LOADING = "loading...";
    private static final String FOOTER_NORMAL = "-- end --";
    private static final String FOOTER_ERROR = "-- 获取失败 --";
    private TextView mFooter;

    // 请求状态 - 下拉刷新 还是 加载更多
    private static final String POST_LOAD_MORE = "load_more";
    private static final String POST_REFRESH = "refresh";
    private ArrayMap<String, String> mPostTypes = new ArrayMap<>();    // 请求类型

    // 当前状态
    private static final int STATE_NORMAL = 0;      // 正常
    private static final int STATE_NO_MORE = 1;     // 正在
    private static final int STATE_LOADING = 2;     // 加载
    private static final int STATE_REFRESH = 3;     // 刷新
    private int mState = STATE_NORMAL;

    // 分页加载
    private int pageIndex = 0;                      // 当面页码
    private int pageCount = 20;                     // 每页个数

    // 数据
    private Diycode mDiycode;                       // 在线(服务器)
    private DataCache mDataCache;                   // 缓存(本地)

    // View
    private TopicAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    NestedScrollView mScrollView;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isFirstLaunch = true;             // 是否是第一次加载

    private Config mConfig;

    public static TopicListFragment newInstance() {
        Bundle args = new Bundle();
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfig = Config.getSingleInstance();
        mDiycode = Diycode.getSingleInstance();
        mDataCache = new DataCache(getContext());
        // 预加载数据, 提前将磁盘数据读取到内存，后续读取更快速
        List<Topic> topics = mDataCache.getTopicsList();
        if (topics != null) {
            for (Topic topic : topics) {
                mDataCache.getTopicPreview(topic.getId());
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_refresh;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        long time = System.currentTimeMillis();
        Logger.e("time = " + time);
        mFooter = holder.get(R.id.footer);
        mScrollView = holder.get(R.id.scroll_view);
        initRefreshLayout(holder);
        initRecyclerView(getContext(), holder);
        initListener(holder);
        initData();
        Logger.e("initViews 耗时 = " + (System.currentTimeMillis() - time) + " ms");
    }

    // 加载数据，默认从缓存加载
    private void initData() {
        mRefreshLayout.setEnabled(true);
        List<Topic> topics = mDataCache.getTopicsList();
        if (null != topics && topics.size() > 0) {
            // 缓存模式，取出上一次的pageIndex
            pageIndex = mConfig.getTopicListPageIndex();
            mAdapter.addDatas(topics);
            mFooter.setText(FOOTER_NORMAL);
            if (isFirstLaunch) {
                final int lastScroll = mConfig.getTopicLastScroll();
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.scrollTo(0, lastScroll);
                    }
                });
                isFirstLaunch = false;
            }
        } else {
            loadMore();
            mFooter.setText(FOOTER_LOADING);
        }
    }

    private void initRefreshLayout(ViewHolder holder) {
        mRefreshLayout = holder.get(R.id.refresh_layout);
        mRefreshLayout.setProgressViewOffset(false, -20, 80);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.diy_red));
        mRefreshLayout.setEnabled(false);
    }

    private void initRecyclerView(final Context context, ViewHolder holder) {
        RecyclerView recyclerView = holder.get(R.id.recycler_view);
        mAdapter = new TopicAdapter(context, mDataCache);
        recyclerView.setAdapter(mAdapter);
        mLinearLayoutManager = new LinearLayoutManager(context);
        mLinearLayoutManager.setSmoothScrollbarEnabled(true);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        Logger.e("初始化View");
    }

    private void initListener(ViewHolder holder) {
        // 监听 RefreshLayout 下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        // 监听 scrollView 加载更多
        NestedScrollView scrollView = holder.get(R.id.scroll_view);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View childView = v.getChildAt(0);
                if (scrollY == (childView.getHeight() - v.getHeight()) && mState == STATE_NORMAL) { //滑动到底部 && 正常模式
                    loadMore();
                }
            }
        });
    }

    // 刷新
    private void refresh() {
        pageIndex = 0;
        String uuid = mDiycode.getTopicsList(null, null, pageIndex * pageCount, pageCount);
        mPostTypes.put(uuid, POST_REFRESH);
        pageIndex++;
        mState = STATE_REFRESH;
    }

    private void onRefresh(GetTopicsListEvent event) {
        mState = STATE_NORMAL;
        mRefreshLayout.setRefreshing(false);
        mAdapter.clearDatas();
        mAdapter.addDatas(event.getBean());
        mDataCache.saveTopicsList(mAdapter.getDatas());
        toast("数据刷新成功");
    }

    // 加载更多
    private void loadMore() {
        String uuid = mDiycode.getTopicsList(null, null, pageIndex * pageCount, pageCount);
        mPostTypes.put(uuid, POST_LOAD_MORE);
        pageIndex++;
        mState = STATE_LOADING;
        mFooter.setText(FOOTER_LOADING);
    }

    private void onLoadMore(GetTopicsListEvent event) {
        List<Topic> topics = event.getBean();
        if (topics.size() < pageCount) {
            mState = STATE_NO_MORE;
            mFooter.setText(FOOTER_NORMAL);
        } else {
            mState = STATE_NORMAL;
            mFooter.setText(FOOTER_NORMAL);
        }
        mAdapter.addDatas(topics);
        mDataCache.saveTopicsList(mAdapter.getDatas());
        mRefreshLayout.setEnabled(true);
    }

    // 数据加载出现异常
    private void onError(String postType) {
        mState = STATE_NORMAL;  // 状态重置为正常，以便可以重试，否则进入异常状态后无法再变为正常状态
        if (postType.equals(POST_LOAD_MORE)) {
            mFooter.setText(FOOTER_ERROR);
            toast("加载更多失败");
        } else if (postType.equals(POST_REFRESH)) {
            mRefreshLayout.setRefreshing(false);
            toast("刷新数据失败");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicList(GetTopicsListEvent event) {
        String postType = mPostTypes.get(event.getUUID());
        if (event.isOk()) {
            if (postType.equals(POST_LOAD_MORE)) {
                onLoadMore(event);
            } else if (postType.equals(POST_REFRESH)) {
                onRefresh(event);
            }
        } else {
            onError(postType);
        }
        mPostTypes.remove(event.getUUID());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        // 保存
        int lastScrollY = mScrollView.getScrollY();
        mConfig.saveTopicListScroll(lastScrollY);
        mConfig.saveTopicListPageIndex(pageIndex);
        super.onDestroyView();
    }

    public void quickToTop() {
        if (mScrollView != null) {
            mScrollView.smoothScrollTo(0, 0);
        }
    }
}
```

如果想要了解我更多的话，可以关注我的微博和网站，详情见下方。

微博： http://weibo.com/GcsSloop  
网站： http://www.gcssloop.com

