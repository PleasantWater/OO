package com.blogofyb.oo.model

import android.annotation.SuppressLint
import android.util.Log
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage
//import cn.leancloud.AVFile
//import cn.leancloud.AVObject
//import cn.leancloud.AVQuery
//import cn.leancloud.AVUser
//import cn.leancloud.im.v2.*
//import cn.leancloud.im.v2.callback.AVIMConversationCallback
//import cn.leancloud.im.v2.callback.AVIMMessagesQueryCallback
//import cn.leancloud.im.v2.messages.AVIMAudioMessage
//import cn.leancloud.im.v2.messages.AVIMImageMessage
//import cn.leancloud.im.v2.messages.AVIMTextMessage
//import cn.leancloud.im.v2.messages.AVIMVideoMessage
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.MessageBean
import com.blogofyb.oo.config.KEY_USER_HEADER
import com.blogofyb.oo.interfaces.model.IChatModel
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import com.blogofyb.oo.util.extensions.sendMessageByCustom
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Create by yuanbing
 * on 2019/8/19
 */
class ChatModel : BaseModel(), IChatModel {
    private lateinit var conversation: AVIMConversation
    private var oldMessage: AVIMMessage? = null

    override fun sendText(text: String, callback: (MessageBean) -> Unit) {
        val message = AVIMTextMessage()
        message.text = text
        conversation.sendMessageByCustom(
            message,
            object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    callback(MessageBean(
                        isSend = true,
                        text = text,
                        header = AVUser.getCurrentUser().getString(KEY_USER_HEADER))
                    )
                }
            }
        )
    }

    override fun sendVideo(path: String, callback: (MessageBean) -> Unit) {
    }

    override fun sendAudio(path: String, callback: (MessageBean) -> Unit) {
    }

    override fun sendPic(path: String, callback: (MessageBean) -> Unit) {
        val pic = AVFile.withAbsoluteLocalPath("${conversation.conversationId}_${System.currentTimeMillis()}.png", path)
        val message = AVIMImageMessage(pic)
        conversation.sendMessageByCustom(
            message,
            object  : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        callback(MessageBean(
                            isSend = true,
                            pic = pic.url,
<<<<<<< HEAD
                            header = AVUser.getCurrentUser().getString(KEY_USER_HEADER))
=======
                            header = AVUser.currentUser().getString(KEY_USER_HEADER))
>>>>>>> 30e3e0527606646cad50545fe2a261ff49438482
                        )
                    } else {
                        Log.e("senPic", e.message ?: "Exception")
                    }
                }
            }
        )
    }

    override fun getMessage(limit: Int, callback: (List<MessageBean>) -> Unit) {
        Log.e("unreadMessagesCount", conversation.unreadMessagesCount.toString())
        conversation.queryMessages(
            object : AVIMMessagesQueryCallback() {
                @SuppressLint("CheckResult")
                override fun done(messages: MutableList<AVIMMessage>?, e: AVIMException?) {
                    if (messages.isNullOrEmpty()) return
                    dealMessages(messages, callback)
                }
            }
        )
    }

    override fun bindConversation(conversation: AVIMConversation) {
        this.conversation = conversation
    }

    override fun getMoreMessage(callback: (List<MessageBean>) -> Unit) {
        val start = AVIMMessageInterval.createBound(oldMessage?.messageId, oldMessage?.timestamp ?: 0, false)
        val interval = AVIMMessageInterval(start, null)
        val direction = AVIMMessageQueryDirection.AVIMMessageQueryDirectionFromNewToOld
        conversation.queryMessages(
            interval,
            direction,
            20,
            object : AVIMMessagesQueryCallback() {
                override fun done(messages: MutableList<AVIMMessage>?, e: AVIMException?) {
                    if (messages.isNullOrEmpty()) return
                    dealMessages(messages, callback)
                }
            }
        )
    }

    @SuppressLint("CheckResult")
    private fun dealMessages(messages: MutableList<AVIMMessage>, callback: (List<MessageBean>) -> Unit) {
        val message: MutableList<MessageBean> = mutableListOf()
        Observable.just(messages)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map {
                oldMessage = it.first()
                it.forEach { avimMessage ->
                    message.add(MessageBean(
                        if (avimMessage is AVIMTextMessage) avimMessage.text else "",
                        if (avimMessage is AVIMImageMessage) {
                            AVQuery.getQuery<AVObject>("_File").whereEqualTo(
                                "objectId",
                                avimMessage
                            ).first.getString("url")
                            Log.d("objId", avimMessage.avFile.objectId)
                            ""
                        } else { "" },
                        if (avimMessage is AVIMAudioMessage) avimMessage.fileUrl else "",
                        if (avimMessage is AVIMVideoMessage) avimMessage.fileUrl else "",
                        "",
                        avimMessage.from == AVUser.getCurrentUser().username
                    ))
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {}, {
                callback(message)
            })
    }
}