package com.blogofyb.oo.util.extensions
//
//import cn.leancloud.AVUser
//import cn.leancloud.im.v2.AVIMConversation
//import cn.leancloud.im.v2.AVIMMessage
//import cn.leancloud.im.v2.AVIMMessageOption
//import cn.leancloud.im.v2.callback.AVIMConversationCallback
//import cn.leancloud.im.v2.messages.AVIMTextMessage
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.GetCallback
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMMessage
import com.avos.avoscloud.im.v2.AVIMMessageOption
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.blogofyb.oo.bean.ConversationBean
import com.blogofyb.oo.config.KEY_NICKNAME
import com.blogofyb.oo.config.KEY_USERNAME
import com.blogofyb.oo.config.KEY_USER_HEADER
import com.blogofyb.oo.util.GlobalMessageManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by yuanbing
 * on 2019/8/20
 */
fun AVIMConversation.sendMessageByCustom(
    message: AVIMMessage,
    callback: AVIMConversationCallback,
    option: AVIMMessageOption? = null
) {
    if (option == null) {
        sendMessage(message, callback)
    } else {
        sendMessage(message, option, callback)
    }
    val conversationId = members.first { it != message.from }
    val conversationBean = GlobalMessageManager.CONVERSATIONS[conversationId]
    if (conversationBean != null) {
        conversationBean.message = if (message is AVIMTextMessage) message.text else message.content
        conversationBean.time = GlobalMessageManager.parseTimeStamp(message.timestamp)
    } else {
        AVUser.getQuery().whereEqualTo(KEY_USERNAME, conversationId)
            .getFirstInBackground(
                object : GetCallback<AVUser>() {
                    override fun done(`object`: AVUser?, e: AVException?) {
                        if (`object` == null) return
                        Observable.just(`object`)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .map { user ->
                                val bean = ConversationBean()
                                bean.header = user.getString(KEY_USER_HEADER)
                                bean.nickname = user.getString(KEY_NICKNAME)
                                bean.message = when (message) {
                                    is AVIMTextMessage -> message.text
                                    is AVIMImageMessage -> "图片"
                                    else -> "未知消息"
                                }
                                bean.time = GlobalMessageManager.parseTimeStamp(message.timestamp)
                                bean.username = conversationId
                                bean
                            }
                            .observeOn(AndroidSchedulers.mainThread())
                            .safeSubscribeBy {
                                GlobalMessageManager.CONVERSATIONS[conversationId] = it
                            }
                    }
                }
            )

    }
    GlobalMessageManager.notifyMessageChanged(message, this)
}
