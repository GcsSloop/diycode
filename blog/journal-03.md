# 03 - 点击网页图片查看大图

### 前言

在写文章的时候，经常会用到图片，图片可以更加直观的表达一些文字难以表述的问题，也更加吸引人们的注意力。在开发 diycode 客户端的时候也考虑到了文章图片的问题，由于 Android 设备屏幕很小，对于某些高清大图来说，放在这么小的屏幕上很难看清楚，所以我就考虑点击图片的时候给用户展示一个可以缩放查看的大图。

为了比较完美的实现这一个功能，我进行了很多的尝试，最终得到了一个相对比较满意的结果，如下：

![](https://ww1.sinaimg.cn/large/006tNbRwly1fdqzpg6zdgg308c0ethdu.gif)

### 解决方案

上图效果是，点击的时候显示当前图片的大图，并且可以左右滑动查看文章中的所有图片，为了完成上图所示的效果，有两点内容比较重要：

1. 找到网页中所有图片
2. 监听当前被点击的图片

由于试验过程中坑比较多，所以先放解决方案了。

#### 1. 注入 JS 代码

为了完成这一功能，我给 WebView 注入了一段 JS 代码，如下：

```java
@SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
private <Web extends WebView> void addImageClickListener(Web webView) {
    // 遍历所有图片，并添加 onclick 函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
    webView.loadUrl("javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "for(var i=0;i<objs.length;i++)  " +
            "{" +
            "  window.listener.collectImage(objs[i].src); " +
            "  objs[i].onclick=function()  " +
            "  {  " +
            "    window.listener.onImageClicked(this.src);  " +
            "  }  " +
            "}" +
            "})()");
}
```

其中关键的地方有两处：

```java
window.listener.collectImage(objs[i].src);	// 搜集文章中的图片

window.listener.onImageClicked(this.src);	// 添加图片点击回调
```

在 WebViewClient 的 `onPageFinished` 中调用该方法即可，过早调用会因为元素还没有加载而出现问题。

注入代码后如何才能接收到这些事件呢？

#### 2. 创建监听器

```java
public class WebImageListener {
    // 收集图片(当发现图片时会调用该方法)
    @JavascriptInterface
    public void collectImage(final String url) {
    }

    // 图片被点击(图片被点击时调用该方法)
    @JavascriptInterface
    public void onImageClicked(String url) {
    }
}
```

**注意：**

1. 方法命名要和注入的 JS 代码一致
2. 要添加 `@JavascriptInterface` 注解

#### 3. 设置监听器

```java
WebImageListener listener = new WebImageListener();
webView.addJavascriptInterface(listener, "listener");
```

使用 WebView 的 `addJavascriptInterface` 来添加监听器，注意别名 `listener` 也要和注入的 JS 代码保持一致，设置监听器位置要在注入 JS 代码的前面。

#### 5. 注意事项

由于注入了 JS 代码，并且进行了 DOM 操作，所以必须开启以下两项设置：

```java
WebSettings settings = webView.getSettings();
settings.setJavaScriptEnabled(true);	// 允许执行 JS 代码
settings.setDomStorageEnabled(true);	// 存储 DOM
```

通过以上步骤收集到了网页中的所有图片，并且为图片设置了点击监听，之后想要怎么操作就随你们啦。

如果想参考我的处理方案可以看 [BaseImageActivity.java](https://github.com/GcsSloop/diycode/blob/master/diycode-app/src/main/java/com/gcssloop/diycode/base/app/BaseImageActivity.java) 和 [ImageActivity.java](https://github.com/GcsSloop/diycode/blob/master/diycode-app/src/main/java/com/gcssloop/diycode/activity/ImageActivity.java) 。

### 踩过的坑

上述方案比较是一种比较容易理解的通用处理方案，如果你有兴趣去到 [GcsSloop/diycode](https://github.com/GcsSloop/diycode) 查看源码的话，你会发现和上述内容少有不同。不过这些都是根据现有情况定制的结果，里面掺杂了图片缓存等其他逻辑，相对来说可能会更加难懂一些。

#### 坑一：不带后缀的图库

其实关于点击查看大图最早的实现方案是没有注入 JS 代码的，仅通过 WebViewClient(以下简称 Client) 提供的相关机制来实现。

文章中的所有图片则通过 Client 的 `shouldInterceptRequest` 来收集，由于图片也是资源的一种，WebView 想要获取图片就一定会调用 `shouldInterceptRequest` 在这里可以将图片链接过滤出来。

那么如何监听图片点击呢？在设置文章内容之前先对文章进行扫描，为所有的图片包裹上一层超链接，即 html 中的 `<a>`  标签，当点击图片等时候会触发 Client 的 `shouldOverrideUrlLoading` 方法，在这了获取到链接，并且判断是不是图片，如果是图片则拦截下来，跳转到对应的显示图片的 Activity。

**那么问题来了，我们如何判断一个 url 请求是不是图片呢？**

最简单的办法就是判断后缀，通常的图片链接都是下面这样的：

```
http://xxxxx.xxx/xxxxx.png
```

通过后缀可以很容易判断，但是当我测试的时候发现一篇文章的图片并不会被识别出来，于是我用 log 打印出来图片链接发现居然是这样子的：

```
https://user-gold-cdn.xitu.io/2017/3/16/16009283e2dc665b39f99d36d41d0ce7 (这是个 gif)
https://user-gold-cdn.xitu.io/2017/3/16/4e820744d74e0c6e9f5076638d122d29 (这是个 jpg)
```

当时我的内心一万匹羊驼飞奔而过，WTF，居然有图床不带后缀的，这对于网页当然不是问题，不仅有 `<img>` 标签，请求的时候还会返回一个 ContentType 来确定类型。

而对于我这种方案来说就是晴天霹雳了，于是仅通过 url 过滤图片计划就这样失败了。

#### 坑二：图片超链接

由于第一种方案失败了，接连引发了第二个坑，就是文章中某些图片本身是带有超链接的，这些图片本身作用就是一种跳转提示，像一些 GitHub 的标签等，作用相当于一个 Button，在我使用的第一种方案中，是不会修改这些本身就已经有超链接的图片的，所以并不会影响，但是如果是使用注入 JS 的方式，当图片本身存在超链接时，点击图片则会触发两次跳转，第一次跳转是跳转到超链，第二次是图片的点击事件。所以会打开两个 Activity。这显然是不符合逻辑的。

我想要的效果时，如果图片本身不带超链接则打开大图，带有超链接则默认跳转，所以我又思索了好久对逻辑进行了修改。

在加载文章之前，为所有不带超链接的图片添加一个标记 `class="gcs-img-sign"` 之后根据这个标记来为图片添加点击事件，这样带有超链接的图片就不会受到影响了。

当然了，这种方案的前提是能拿到网页需要加载内容的源码，如果仅仅是一个 url 的话就会比较麻烦了。

#### 坑三：WebView 问题

最初在测试注入 JS 代码的时候一直不能成功，总是报出一些异常，经过调查发现，WebView 早期是有 bug 的，使用 JS 代码可以通过反射等方式获取到内存卡上的文件，后来修复了这一问题，同时也添加上了一些规则。

1. 设置 WebView 允许执行 JavaScript 代码，`settings.setJavaScriptEnabled(true);` 。
2. JS 调用 Java 时，被调用的方法必须用 `@JavascriptInterface` 标注。

但是设置了之后还是不行，经过查询后发现 WebView 默认是不保存 DOM 的，所以当我注入的 JS 代码遍历时就会出问题，还需要添加一项设置。

3. 设置 `settings.setDomStorageEnabled(true);` 。

最终经过重重磨难，终于比较完美的实现了我想要的效果。



如果想要了解我的话，可以关注我的微博和网站，详情见下方。

微博： http://weibo.com/GcsSloop  
网站： http://www.gcssloop.com