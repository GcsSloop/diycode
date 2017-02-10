# diycode
Diycode 开发计划，围绕 Diycode Android 客户端进行的开发计划，该计划包括：

### diycode-sdk 的开发

该 sdk 主要是针对 diycode 的 api 进行封装，屏蔽掉一些细节问题，让上层应用只关注显示上层逻辑，而不用去关注具体的细节。

> 例如：当用户登录的时候会获取到一个 token，根据这个 token 才能请求到某些信息，当请求这些信息时，未登录会抛出一个异常状态提醒用户登录，登录后则会自动获取缓存的 token 来获取这些信息，上层应用不需要知道 token 存在。

### discoed-test 的开发

这个主要是针对 sdk 进行全方位的测试，会尽量覆盖所有 API 以及所有情况。

同时也是一个 sdk 使用方法的 demo，由于会针对特定情况分成若干 Activity 进行测试，所以每个 Activity 的代码不会太多，逻辑也比较简单，方便查阅参考。

### diycode-app 的开发

Diycode Android 客户端本体。



