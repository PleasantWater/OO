# <center>OO</center>

这个APP采用了`最强`的IMSDK`LeanCloud`，正因为有这样的SDK我才写出了如下功能

* 登录注册（没有密码重置）
* 添加好友（受制于SDK的未读消息数不清空，所以一方添加好友之后，双方就都是好友关系了）
* 动态（SDK和文档有出入，没有实现下拉加载）
* 单聊（图片消息会造成聊天界面刚打开时，消息记录无法自动滑到底）
* 资料修改

*中途降了一次SDK，可能个别地方会出现网络请求在主线程的问题*

[APK](./app/debug/app-debug.apk)

![又不是不能用](https://img3.doubanio.com/view/thing_review/l/public/2189725.webp)