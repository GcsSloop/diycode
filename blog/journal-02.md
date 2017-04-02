# 02 - 缓存 WebView 中的图片

在开发 diycode 客户端过程中，为了节省流量，我在很多地方都使用了缓存，请求的 json 数据可以缓存，用户头像可以缓存，文章当然也是可以缓存的。

唯一有问题的一点是文章中的图片，文章是使用 WebView 进行加载的，所有图片都是是超链接，加载时会从网络上请求数据，而图片却是整个过程中最耗费流量的，一张大图的耗费的流量都够加载好几篇文章了。

其余地方的图片倒是好处理，使用 Glide 加载就行了，Glide自己有缓存策略，只需要配置一下就行了，但是遇到 WebView 时 Glide 就没用了，虽然 WebView 有缓存策略，但如果统一设置优先使用缓存可能会导致一些数据的不同步，使用默认缓存的话，每次退出后再进来就要重新请求图片资源，会浪费掉很多的流量。

于是我想，有没有办法让图片第一次加载时缓存下来，后续用到的时候就不用再去网络请求了。这里涉及到两个主要的问题：

**1. 如何获取到 WebView 所加载的图片**

**2. 如何使用本地图片替换 WebView 中需要从网络上请求的图片。**

关于第一个比较好解决，我们知道 WebView 有一个 WebViewClient， 其中有一个 `onLoadResource` 方法可以监听 WebView 加载了哪些资源。我的思路非常简单，伪代码如下：

```java
@Override
public void onLoadResource(WebView view, final String url) {
	if(url是图片类型){
      图像 = 下载(url);
      存储(图像);
	}
}
```

使用这种方式实际上是请求了两次数据，WebView 下载了 1 次，我们也下载了 1 次，所以在首次加载的时候重复下载了 2 次图片。有点浪费，不过相对于每次都重新获取要好一点。

> 其实还有另一种方案，就是在 WebView 实际加载资源的时候，先拦截下来，我们先下载好图片，之后再将结果给 WebView 这样最终就只请求了一次，但是由于这种方式会明显延长 WebView 的加载时间，用户体验会变差，而且数据的来回转换也比较难以控制，如果以后找到解决方案再来更新。

我们将图片缓存下来之后，下一步就是让下一次加载数据的时候不去网上请求图片，而是使用本地内容，关于这个我想到了两种方案。

#### 第一种，直接用 base64 数据替换

这一种方法简单粗暴，下载完图片之后，将图片转化为 base64 格式缓存起来，使用图片等 url 作为索引，当下一次设置加载文章内容之前，先对文章扫描一遍，如果扫描到图片的 url 在本地有缓存数据，就用 base64 格式的数据直接替换掉原文链接。像下面这样：

```
![](https://diycode.b0.upaiyun.com/photo/2017/73a8485c12b51e7a9e61da4fdab94feb.png)
```

将上面的链接替换成为下面的数据(数据只截取了一段，作为示例，里面省略了很长，很长，很长 的内容)。

```
![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABQAAAAJACAIAAABdeVxCAAAAA3NCSVQFBgUzC42AAAAgAElEQVR4nOy9X4hkWZrY95vcE73f3Y2UzmWzRFxRbTqkadgc3IurYYy68T7MmDXsGBm8+6Z98IPBD5bRkx/8LIzxw4KxsWSEWBmtGAvt0jOww1Q/NKoGtZ0p6CFjvDVUtLYbReFOKwJXsveweZn7qeMo8MO5N+JGZlZ19XRlZmTE96OpjrxxI+ReF......)
```

这样方法简单粗暴，也能实现我想要的效果，但是缺陷也很大。

1. 耗费内存，需要将文章数据+图片数据一次性全部加载进内存，如果遇上一张大图片，很可能就直接 OOM 异常了。
2. 耗费时间，数据转换时间太长，使用了这种缓存机制后，明显感觉文章加载速度变慢了，比直接使用网络请求数据还慢。

#### 第二种，拦截资源请求

这一种方案比较靠谱，具体思路是监听 WebView 的网络请求，当发现请求是图片链接的话，拦截该请求，检查本地是否有缓存文件，如果有的话，就创建一个资源指向缓存文件，并直接返回该资源，不再去请求。如果没有的话，就执行默认设置，去请求网络资源。示例代码是这样：

```java
setWebViewClient(new WebViewClient() {
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Logger.e("拦截请求");
        try {
            // 如果是图片且本地有缓存
            if (isImageSuffixCheck(url) || isGifSuffixCheck(url)) {
                File file = new File("/sdcard/a.jpeg");
                InputStream inputStream = new FileInputStream(file);
                WebResourceResponse response = new WebResourceResponse("image/jpeg", "base64", inputStream);
                return response;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Logger.e("获取默认");
        return super.shouldInterceptRequest(view, url);
    }
});
```

结果像这样：

[下图对应的文章地址](https://www.diycode.cc/topics/650)

![tiny_Screenshot_2017-03-11-06-50-05](https://ww3.sinaimg.cn/large/006tKfTcly1fdikr4ya6fj308c0etaax.jpg)

可以看到，图片被成功的替换掉了。使用这种方式所耗费的资源要比上一种方式少很多，目前来说，是一种不错的方法。



如果想要了解我的话，可以关注我的微博和网站，详情见下方。

微博： http://weibo.com/GcsSloop  
网站： http://www.gcssloop.com