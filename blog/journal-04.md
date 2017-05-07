# 04 - API 的封装哲学

**本文重点：使用 Retrofit2.0 + EventBus 快速优雅的封装 API，架构搭建完成后，封装一个 api 手写代码不超过 10 行，逻辑清晰，且在一定程度上防手残。**

## 前言

在开发 diycode 项目过程中，本文涉及的这部分代码是中最先完成的，但一直到现在才放出来这篇文章，倒不是因为代码有多么复杂，而是太过于简单了。

如果你直接去看代码 [diycode-sdk](https://github.com/GcsSloop/diycode-sdk) ，只要基础不是特别差，都能看懂里面写的东西，并没有过于复杂的逻辑。

然而，我相信大部分人看完后会产生一种以为自己看懂了的感觉，但落实到具体代码上，还是不知道如何写。这主要是因为你看懂了代码的写法，但不知道为何要会是这样子。所以接下来我就简单分析一下其架构演化逻辑，如果想看结论，直接翻到最后就行了。（不过，我认为只看结论的意义并不大）

**观看本文的前置技术要求：**

1. 明白 Retrofit 的基本用法
2. 明白 EventBus 的基本用法

## 1. 确定网络请求方案

Android 实现网络请求有很多种方式，即可以使用最原始的 HttpURLConnection，封装过的 OkHttp，也能选择相对封装程度较高的 Volley、 Retrofit。

作为一个会偷懒的工程师，自然要选择封装好的，虽然 Volley 是 Google 官方的，但是看了看 GiHub 上的 Star 数量，果断选择了 Retrofit，毕竟 Retrofit 使用的人数多，就算踩坑了也好找解决方案。更何况，Retrofit 直接吧 Json 数据解析也给处理了，让我这种懒人怎么能拒绝呢？

首先看看常规的使用 Retrofit 进行网络请求是啥样子吧。

**1. 写一个实体类(自动生成)：**

```java
public class Topic implements Serializable {
    private int id;                         // 唯一 id
    private String title;                   // 标题
	// 省略若干字段 ... 
	// 省略若干方法 ...    
}
```

**2. 写一个 Service：**

```java
// ... 省略其它方法
@GET("topics.json")
Call<List<Topic>> getTopicsList(@Query("type") String type, @Query("node_id") Integer node_id, @Query("offset") Integer offset, @Query("limit") Integer limit);
// ...
```

**3. 设置 Retrofit：**

```java
Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.diycode.cc/api/v3/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build();
```

**4. 实例化 Service：**

```java
TopicService service = retrofit.create(TopicService.class);
```

**5. 请求数据：**

```java
Call<List<Topic>> call = service.getTopics(type, nodeId, offset, limit);
call.enqueue(new Callback<List<Topic>>() {
@Override
public void onResponse(Call<List<Topic>> call, retrofit2.Response<List<Topic>> response) {
  if (response.isSuccessful()) {
    // 成功
  } else {
    // 失败
  }
}
@Override public void onFailure(Call<List<Topic>> call, Throwable t) {
  // 失败
}
});
```

可以看到，Retrofit 是使用 **Callback** 异步返回数据的，最直观的想法当然是把这个 Callback 可以放在 Activity ，当作参数传递进来，像下面这样:

**实现类**

```java
public void getTolicList(Callback callback) {
    Call<List<Topic>> call = service.getTopics(type, nodeId, offset, limit);
    call.enqueue(callback);
}
```

**Activity**

```java
diycode.getTolicList(new Callback<List<Topic>>() {
    @Override
    public void onResponse(Call<List<Topic>> call, retrofit2.Response<List<Topic>> response) {
    if (response.isSuccessful()) {
      // 成功
    } else {
      // 失败
    }
    }
    @Override public void onFailure(Call<List<Topic>> call, Throwable t) {
    // 失败
    }
});。
```

这种方案可行，也很直接，但是还有一些问题。

第一、我觉得这样写，Activity中的垃圾代码数量会很多。

第二、容易产生回调地狱。

> 什么是回调地狱呢？
>
> 上面我设想的结构是两层，这样还好，但是如果我再抽取出来一个数据缓存层呢？这样就需要把这个 Callback 从上层应用传递到数据缓存层，再从数据缓存层传递到网络请求层。
>
> 期间可能还要对 callback 进行包装和转换，层数越多，越是麻烦，而且耦合性相当高，后期修改代码都要小心翼翼的。所以我称之为回调地狱。

#### 我想要的效果是这样的(伪代码)：

**Activity**

```java
网络请求.获取Topic列表();
网络请求.获取Topic详情();

void 请求结果(Topic列表 列表){
}

void 请求结果(Topic详情 详情){
}
```

这样子，代码量也少了，结构也更加清楚了。

想要实现上面这种效果要怎么办呢？当然是使用 EventBus 了，使用事件总线可以更简单方便的控制数据返回，并且分层再多也不怕了，可以有效的避免回调地狱的出现。整体结构看起来像下面这样：

确定了想要的样子，就开始写代码吧，

## 2. 代码雏形

**最初我写出来的代码是这样子的：**

下面代码省略了实体类（实体类是用工具自动生成的）

**Diycode.java**

```java
// 登录
public void login(@NonNull final String user_name, @NonNull final String password) {
    Call<Token> call = mDiycodeService.getToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE,
            user_name, password);

    call.enqueue(new Callback<Token>() {
        @Override
        public void onResponse(Call<Token> call, Response<Token> response) {
            if (response.isSuccessful()) {
                Token token = response.body();
                Log.e(TAG, "token: " + token);
                // 请求成功后数据缓存起来
                cacheUtils.saveToken(token);

                EventBus.getDefault().post(new LoginEvent(response.code(), token));
            } else {
                Log.e(TAG, "getToken state: " + response.code());
                EventBus.getDefault().post(new LoginEvent(response.code()));
            }

        }

        @Override
        public void onFailure(Call<Token> call, Throwable t) {
            Log.d(TAG, t.getMessage());
            EventBus.getDefault().post(new LoginEvent(-1));
        }
    });
}

// 测试登录获取 token 是否成功
public void hello(@Nullable Integer limit) {
    Call<Hello> call = mDiycodeService.hello(limit);

    call.enqueue(new Callback<Hello>() {
        @Override
        public void onResponse(Call<Hello> call, Response<Hello> response) {
            if (response.isSuccessful()) {
                Hello hello = response.body();
                Log.e(TAG, "hello: " + hello);
                EventBus.getDefault().post(new HelloEvent(response.code(), hello));
             } else {
                 Log.e(TAG, "hello state: " + response.code());
                 EventBus.getDefault().post(new HelloEvent(response.code()));
             }
         }

         @Override
         public void onFailure(Call<Hello> call, Throwable t) {
             Log.e(TAG, "hello onFailure: ");
             EventBus.getDefault().post(new HelloEvent(-1));
         }
     });
}
```

**LoginEvent.java**

```java
public class LoginEvent {
    private boolean ok = false;     // 是否登录成功
    private Token token;            // 令牌
    private Integer state = -1;     // 状态码
  
    public LoginEvent(@NonNull Integer state) {
        this.ok = false;
        this.token = null;
        this.state = state;
    }

    public LoginEvent(@NonNull Integer state, @Nullable Token token) {
        this.ok = true;
        this.token = token;
        this.state = state;
    }

    public boolean isOk() {
        return ok;
    }

    public Token getToken() {
        return token;
    }

    public Integer getState() {
        return state;
    }
}
```

**HelloEvent.java**

```java
public class HelloEvent {
    private boolean ok = false;     // 是否获取成功
    private Hello hello;            // Hello bean
    private Integer state = -1;     // 状态码

    public HelloEvent(Integer state) {
        this.ok = false;
        this.state = state;
    }

    public HelloEvent(Integer state, Hello hello) {
        this.ok = true;
        this.state = state;
        this.hello = hello;
    }

    public boolean isOk() {
        return ok;
    }

    public Hello getHello() {
        return hello;
    }

    public Integer getState() {
        return state;
    }
}
```

看到这么一大坨代码是不是有一种日了狗的心情，以上只是完成了两个简单的 API 请求而已，很不幸的是，如果按这种标准写法，基本每个 API 都要这么来一遍，这几十数百个 API 写下来怕不是要累吐血。

![14570791881728086](https://ww4.sinaimg.cn/large/006tNbRwly1fe9180f0bej306o06o3yg.jpg)

为了防止自己在写完 diycode 客户端之前就被累死，我决定要重构这一份代码，毕竟重构要趁早，越晚越麻烦。

## 3. 重构代码

### 3.1 重构 Event

观察分析上面代码，首先看两个 Event，发现两个 Event 长得是非常像的，里面只有一个数据字段是不同的，其余内容基本一样，所以这两个 Event 可以抽取一个公共类。

如果你区观察这两个 Event 的话，你会发现，除了存放数据实体类，我还另外添加了两个字段，分别用于判断数据获取是否成功，和网络状态吗，这个主要是为了让上层应用判断状态用的，假如获取失败了也可以通过状态码来分析失败原因，而不是瞎猜。

其实上述设计还是少了一个东西，那就是判断网络请求的识别码，这个识别码又啥用呢？**其实只有一个作用，那就是判断网络请求类型。**

> 设想一个场景，应用使用了分页加载，用户正在加载下一页数据的时候网络特别慢，等了一小会儿还是没有加载出来，于是点击了刷新，然后网络突然变好了，瞬间返回两次数据，其中一次是下拉加载的，另一次是刷新的，两者数据类型和数量一样，并且因为使用了异步返回，很难根据返回顺序来判断到底是哪次请求，此时就会陷入一种尴尬的境地，如果不予区分，都将数据加载展示，则会导致时间线错乱，如果抛弃两次数据重新请求，你咋知道后面会不会继续出现类似的情况？

**此时识别码的作用就显现出来了**，如果程序在发出请求的时候可以返回一个唯一的识别码，我们可以将这个识别码和请求类型用 Map 存储起来，当数据返回时，我们从返回的数据中同样能拿到一个识别码，通过对比这两个识别码就能轻松的判断出具体类型。根据需要进行处理即可。

**关于这个示例清看这里 [如何区分请求](https://github.com/GcsSloop/diycode-sdk#3-如何区分请求)**

所以重构后的 Event 长这样：

**BaseEvent.java**

```java
public class BaseEvent<T> {
    protected String uuid = "";         // 通用唯一识别码 (Universally Unique Identifier)
    protected boolean ok = false;       // 是否获取实体数据(T)成功
    protected Integer code = -1;        // 状态码
    protected T t;                      // 实体类

    public BaseEvent(@Nullable String uuid) {
        this.ok = false;
        this.uuid = uuid;
    }
    public BaseEvent(@Nullable String uuid, @NonNull Integer code, @Nullable T t) {
        this.ok = null != t;
        this.uuid = uuid;
        this.code = code;
        this.t = t;
    }
    public BaseEvent setEvent(@NonNull Integer code, @Nullable T t) {
        this.ok = null != t;
        this.code = code;
        this.t = t;
        return this;
    }
    public boolean isOk() {
        return ok;
    }
    public T getBean() {
        return t;
    }
    public String getUUID() {
        return uuid;
    }
    public Integer getCode() {
        return code;
    }
}
```

**HelloEvent.java**

```java
public class HelloEvent extends BaseEvent<Hello> {
  public HelloEvent(@Nullable String uuid) {
      super(uuid);
  }
  public HelloEvent(@Nullable String uuid, @NonNull Integer code, @Nullable Hello hello) {
      super(uuid, code, hello);
  }
}
```

以后所有的 Event 只用继承 BaseEvent 并指定范型，然后自动生成两个构造函数即可，手写代码一行就能完成一个 Event，再也不用复制粘贴了，岂不快哉。

### 3.2 重构 Callback

Retrofit 的 Callback 还是很有清晰简明的。

```java
 public void hello(@Nullable Integer limit) {
    Call<Hello> call = mDiycodeService.hello(limit);
    call.enqueue(new Callback<Hello>() {
        @Override
        public void onResponse(Call<Hello> call, Response<Hello> response) {
            if (response.isSuccessful()) {
                // 1. 成功
            } else {
                // 2. 失败
            }
        }

        @Override
        public void onFailure(Call<Hello> call, Throwable t) {
           // 3. 失败
        }
    });
}
```

大体可以分为 3 种状态。

第一种状态：成功，不必多说，自然是将数据通过 EventBus 传递给上一层。  
第二种状态：失败，这个多是与服务器相关，例如，没有权限，请求超时，参数错误等，都会有返回对应的状态码，此时数据肯定是没有了，所以把服务器状态码传递给上层即可。  
第三种状态：失败，这个则多是由于网络未连接，服务器返回数据和指定数据类型不符，转换失败导致的，此时没有返回的状态吗，直接将状态码赋值为 -1 即可。

当然了，前面我们提到了用于区分请求类型的唯一识别码，这个识别码也应该在这里生成并返回给上一层。

#### 所以最终重构结果是这样的：

**BaseCallback.java**

```java
public class BaseCallback<T> implements Callback<T> {
    protected BaseEvent<T> event;                   // 事件

    public <Event extends BaseEvent<T>> BaseCallback(@NonNull Event event) {
        this.event = event;
    }
    
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful())
            EventBus.getDefault().post(event.setEvent(response.code(), response.body()));
        else {
            EventBus.getDefault().post(event.setEvent(response.code(), null));
        }
    }
    
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        EventBus.getDefault().post(event.setEvent(-1, null));
    }
}
```

由于我们是需要 BaseCallback 在接收到返回事件时使用 Event 传递回去，所以需要创建的时候传递进来一个Event。使用的时候是这样：

```java
@Override
public String hello(@Nullable Integer limit) {
    final String uuid = UUIDGenerator.getUUID();
    mService.hello(limit).enqueue(new BaseCallback<>(new HelloEvent(uuid)));
    return uuid;
}
```

是不是瞬间清爽了好多，从最开始的一大坨，变成最后的几行了，到目前为止，封装已经基本完成了，关于唯一识别码，之前考虑过使用 Random 随机数，但随机数再随机也可能会撞上，要生成不会重复的随机数还要自己封装一下，于是干脆使用了系统提供了 UUID 生成器，该 UUID 能保证在同一时间，同一空间内不会重复，具体代码看 [UUIDGenerator.java](https://github.com/GcsSloop/diycode/blob/f9498b827b869f4b430d7fd608462f9587d4b900/diycode-sdk/src/main/java/com/gcssloop/diycode_sdk/utils/UUIDGenerator.java) 

**我们现在统计一下封装一个网络请求 API 需要手写多少代码：**

省略实体类的创建，实体类通过工具自动生成，不需要手写代码。

**1. 定义 Service (2行)**

```java
@GET("hello.json")
Call<Hello> hello(@Query("limit") Integer limit);
```

**2. 创建 Event (约 1 行，起名字和指定范型)**

构造函数自动生成。

```java
public class HelloEvent extends BaseEvent<Hello> {
  public HelloEvent(@Nullable String uuid) {
      super(uuid);
  }
  public HelloEvent(@Nullable String uuid, @NonNull Integer code, @Nullable Hello hello) {
      super(uuid, code, hello);
  }
}
```

**3. 创建请求代码 (3 行)**

其实我最终手写的就一行，因为 String uuid = UUIDGenerator.getUUID(); 和 return uuid; 是使用 AS 查找替换功能自动添加的，写一遍，之后 Replace All，格式化就行了，真正纯手写的就中间一行。

```java
public String hello(@Nullable Integer limit) {
    String uuid = UUIDGenerator.getUUID();
    mService.hello(limit).enqueue(new BaseCallback<>(new HelloEvent(uuid)));
    return uuid;
}
```

总共不到 10 行就完成了一个 API 的封装，不仅更加简洁了，编码效率也是大大的提高。

我在创建 BaseEvent 和 BaseCallback 的时候都使用了范型，这个范型不仅能用来提高复用性，还能一定程度防手残。**定义 Service，BaseCallback 和 Event 的范型必须一致才能通过类型检查，否则在编译阶段就会报错。** 合理使用范型能大大的提高编码效率和编码复用性。

## 4. 后记

其实关于 SDK 架构这一部分还有一个小知识：为了方便上层使用，让上层应用只持有 Diycode 一个对象即可获取所有数据，并且保证后期维护的时候查找方便，我使用了聚合类。不过这些都是雕虫小技，并不值得花费太多笔墨去描述，大家明白即可。

可以看到，上面代码都省略了实体类的创建，是因为实体类可以用工具生成，可以使用在线工具 [json2javapojo](http://www.bejson.com/json2javapojo/) 或者 AndroidStudio 插件 [GsonFormat](https://github.com/zzz40500/GsonFormat) 。

生成实体类的前提条件是我们已经能拿到服务器返回的 Json 数据了，我们可以使用 [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=zh-CN) 在写代码之前获取返回的数据。同时，它也可以方便的帮助我们测试 API。

文章中涉及的核心代码都在这里面： [diycode-sdk/base](https://github.com/GcsSloop/diycode-sdk/tree/master/sdk/src/main/java/com/gcssloop/diycode_sdk/api/base)

## 5. 总结

**下面是本次设计中个人的一些心得体会：**

1. 不必一开始就把所有东西想好，可以在制作过程中逐渐调整。
2. 如果发现了结构不合理的部分，最好立即重构代码，否则越拖越难修改。
3. 合理使用范型能大大的提高代码复用率。



如果想要了解我更多的话，可以关注我的微博和网站，详情见下方。

微博： http://weibo.com/GcsSloop  
网站： http://www.gcssloop.com