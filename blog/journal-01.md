# 01 - 整体架构

diycode 开放 API 也有几个月的时间了，很多小伙伴都说想要试试，但是迄今为止，只有一个完整的开源客户端，看看隔壁 “干货集中营” 的客户端数量，简直不能比啊。我觉得其中很大的原因就是 diycode 的 api 数量太多，使用起来也太过于复杂了，单单权限认证这一关就能让不少跃跃欲试的小伙伴望而却步。

为了解决这一难题，我将 diycode 的开放 API 进行了二次封装，以方便想要制作客户端的小伙伴们使用，经过比较“漫长”的摸索和设计，目前其基本架构已经成型了，差不多是下面这样子：

### SDK 结构
![](https://ww2.sinaimg.cn/large/006tKfTcly1fcvoedie5vj30ea0aamxj.jpg)

当然了，里面还有一些细节没有在图中进行展示，具体的实现结构要比上图稍微复杂那么一点点。

到今天为止，其 SDK 的结构已经基本成型了，只剩下一些体力活没有弄完，预计 3 月 12 号可以放出第一个版本的 SDK。

app 的开发还正在进行中，可能要多等几天，就能看到我开发的客户端了，不过为了测试 SDK 是否真的那么好用，我做了 3 个页面用于测试，看起来是下面的样子：

![](https://diycode.b0.upaiyun.com/photo/2017/01b87d582182e34dac091269a5e8d7ba.gif)

测试结果和预期差不多，目前 demo 中每个 Activity(Fragment) 的代码量不超过 200 行，其中包括一些简单的注释，与请求数据相关的核心代码不超过 50 行，以 topic 详情页为例（一些细节尚未处理）：

![](https://diycode.b0.upaiyun.com/photo/2017/d8dfe7aa5e2c6fac788bab47196b91d5.jpeg)

在这个 Activity 里面向服务器请求了两次数据：

1. 获取 topic 详情
2. 获取 topic 回复列表

并且将请求回来的数据与相关的 View 关联，即便如此，代码量也没有超过 200 行。  

``` java
public class TopicContentActivity extends BaseActivity implements View.OnClickListener {
    public static String TOPIC = "topic";
    private TopicReplyListAdapter mAdapter;
    private Topic topic;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topic_content;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        initTopicContentPanel(holder);
        initRecyclerView(holder);
    }

    // 初始化 topic 内容面板的数据
    private void initTopicContentPanel(ViewHolder holder) {
        Intent intent = getIntent();
        topic = (Topic) intent.getSerializableExtra(TOPIC);
        if (null != topic) {
            toastShort("获取 topic 成功");
            User user = topic.getUser();
            holder.setText(R.id.username, user.getLogin() + "(" + user.getName() + ")");
            holder.setText(R.id.time, TimeUtil.computePastTime(topic.getUpdated_at()));
            holder.setText(R.id.title, topic.getTitle());
            holder.loadImage(this, user.getAvatar_url(), R.id.avatar);
            holder.setOnClickListener(this, R.id.avatar, R.id.username);

            // 发出获取 topic 详情和 topic 回复的请求
            // TODO 分页加载回复的内容(鉴于目前回复数量并不多，此处采取一次性加载)
            mDiycode.getTopic(topic.getId());
            mDiycode.getTopicRepliesList(topic.getId(), null, topic.getReplies_count());
        } else {
            toastShort("获取 topic 失败");
        }
    }

    private void initRecyclerView(ViewHolder holder) {
        mAdapter = new TopicReplyListAdapter(this) {
            @Override
            public void setListener(int position, GcsViewHolder holder, TopicReply topic) {
                super.setListener(position, holder, topic);
                // TODO 此处设置监听器
            }
        };

        RecyclerView recyclerView = holder.get(reply_list);
        RecyclerViewUtil.init(this, recyclerView, mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicDetail(GetTopicEvent event) {
        if (event.isOk()) {
            MarkdownView markdownView = mViewHolder.get(R.id.content);
            markdownView.setMarkDownText(event.getBean().getBody());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicReplysList(GetTopicRepliesListEvent event) {
        if (event.isOk()) {
            List<TopicReply> replies = event.getBean();
            mAdapter.addDatas(replies);
        } else {
            toastShort("获取回复失败");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
            case R.id.username:
                if (null != topic) {
                    openActivity(UserActivity.class, UserActivity.USER, topic.getUser());
                }
                break;
        }
    }
}

```

可以看到，其逻辑结构相当简单，当然了，这些代码都是经过我高度浓缩精炼后的，具体的浓缩精炼方法会在后续开发日志中逐渐教给大家，当然了，直接去看源码也行，本次的 diycode 开发过程是全公开的，如果想关注 diycode 的更多细节，可以到 [GitHub](https://github.com/GcsSloop/diycode) 查看。

如果想要了解我的话，可以关注我的微博和网站，详情见下方。

微博： http://weibo.com/GcsSloop  
网站： http://www.gcssloop.com

