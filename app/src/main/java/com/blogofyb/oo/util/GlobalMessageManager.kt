package com.blogofyb.oo.util

import android.app.*
import android.util.Log
//import cn.leancloud.AVUser
//import cn.leancloud.im.v2.*
//import cn.leancloud.im.v2.callback.AVIMConversationCreatedCallback
//import cn.leancloud.im.v2.messages.AVIMTextMessage
import android.content.Intent
import android.os.Build
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
//import cn.leancloud.im.v2.messages.AVIMImageMessage
import com.blogofyb.oo.BaseApp.Companion.context
import com.blogofyb.oo.R
import com.blogofyb.oo.bean.ConversationBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.util.extensions.editor
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import com.blogofyb.oo.util.extensions.sharedPreferences
import com.blogofyb.oo.view.activity.MainActivity
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet


/**
 * Create by yuanbing
 * on 2019/8/17
 */
object GlobalMessageManager {
    val CONVERSATIONS = TreeMap<String, ConversationBean>()
    lateinit var mClient: AVIMClient  // 突然意识到没网的情况下app就不能用了，就先这样吧
    val mObservers: HashMap<String, MessageObserver> = HashMap()
    private val mNotificationManager = context.getSystemService(Activity.NOTIFICATION_SERVICE)
            as NotificationManager
    private val mSimpleDateFormat = SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA)
    private val mOOMessageHandler = object : AVIMMessageHandler() {
        override fun onMessage(
            message: AVIMMessage?,
            conversation: AVIMConversation?,
            client: AVIMClient?
        ) {
            if (message == null || conversation == null) return
            val conversationId = message.from
            val observer1 = mObservers[conversationId]
            val observer2 = mObservers[context.packageName]
            if (observer1 == null && observer2 == null) {
                // 弹出通知
                AVUser.getQuery().whereEqualTo(KEY_USERNAME, conversationId)
                    .getFirstInBackground(
                        object : GetCallback<AVUser>() {
                            override fun done(
                                avObjects: AVUser?,
                                avException: AVException?
                            ) {
                                if (avObjects == null) return
                                sendNotification(message, avObjects.getString(KEY_NICKNAME))
                            }
                        }
                    )
            } else {
                observer1?.onMessage(message, conversation)
                conversation.read()
            }

            Observable.create<ConversationBean> {
                val user = AVUser.getQuery().whereEqualTo(KEY_USERNAME, message.from).first
                val conversationBean = ConversationBean(
                    conversationId,
                    parseTimeStamp(message.timestamp),
                    when (message) {
                        is AVIMTextMessage -> message.text
                        is AVIMImageMessage -> "[图片]"
                        else -> "[未知消息]"
                    },
                    user.getString(KEY_NICKNAME),
                    conversation.unreadMessagesCount,
                    user.getString(KEY_USER_HEADER),
                    conversationId
                )
                conversation.read()
                Log.d("unreadCount", conversation.unreadMessagesCount.toString())
                it.onNext(conversationBean)
                it.onComplete()
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribeBy {
                    CONVERSATIONS[conversationId] = it
                    observer2?.onMessage(message, conversation)
                }


            Log.d(conversationId, message.toString())
        }
    }

    init {
        AVIMMessageManager.registerDefaultMessageHandler(mOOMessageHandler)
    }

    fun subscribe(conversationId: String = context.packageName, observer: MessageObserver) {
        if (conversationId != context.packageName) {
            CONVERSATIONS[conversationId]?.unreadCount = 0
        }
        mObservers[conversationId] = observer
        Log.d(conversationId, "subscribe")
    }

    fun unSubscribe(conversationId: String = context.packageName) {
        mObservers.remove(conversationId)
        Log.d(conversationId, "unSubscribe")
    }

    fun getConversation(member: String?, callback: (AVIMConversation) -> Unit) {
        mClient.createConversation(
            listOf(member),
            "$member & ${AVUser.getCurrentUser()?.username}",
            null,
            false,
            true,
            object : AVIMConversationCreatedCallback() {
                override fun done(conversation: AVIMConversation?, e: AVIMException?) {
                    if (conversation == null) return
                    callback(conversation)
                }
            }
        )
    }

    private fun sendNotification(message: AVIMMessage, nickname: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent, 0)
        if (Build.VERSION.SDK_INT >= 26) {
            // 不知道为什么直接跳转聊天界面就抛异常
            val channelId = "message"
            val channelName = "消息"
            mNotificationManager.createNotificationChannel(NotificationChannel(channelId, channelName,
                NotificationManager.IMPORTANCE_HIGH))
            val nb = Notification.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(nickname)
                .setContentText(
                    when (message) {
                        is AVIMTextMessage -> message.text
                        is AVIMImageMessage -> "[图片]"
                        else -> message.content
                    }
                )
                .setAutoCancel(true)
                .setShowWhen(true)
                .setContentIntent(pi)
             mNotificationManager.notify(1, nb.build())
        } else {
            mNotificationManager.notify(
                1,
                Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(nickname)
                    .setContentText(if (message is AVIMTextMessage) message.text else message.content)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setContentIntent(pi)
                    .build()
            )
        }
    }

    fun notifyMessageChanged(message: AVIMMessage, conversation: AVIMConversation) {
        val observer = mObservers[context.packageName]
        observer?.onMessage(message, conversation)
    }

    fun parseTimeStamp(timeStamp: Long): String {
        val date = Date(timeStamp)
        return mSimpleDateFormat.format(date)
    }

    fun asyncMessage() {
        Observable.just(AVUser.getCurrentUser().username)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .safeSubscribeBy { username ->
                AVQuery<AVObject>(KEY_OBJECT_CONVERSATION).whereContains(KEY_OBJECT_MEMBER, username)
                    .findInBackground(
                        object : FindCallback<AVObject>() {
                            override fun done(
                                avObjects: MutableList<AVObject>?,
                                avException: AVException?
                            ) {
                                if (avException != null) return
                                Observable.just(avObjects)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .safeSubscribeBy {
                                        it?.forEach { avConversation ->
                                            val conversation = mClient.getConversation(avConversation.objectId)
                                            val conversationId = avConversation.getList(
                                                KEY_OBJECT_MEMBER).first { it != username }.toString()
                                            val unreadCount = conversation.unreadMessagesCount
                                            val user = AVUser.getQuery().whereEqualTo(KEY_USERNAME, conversationId).first
                                            conversation.queryMessages(
                                                1,
                                                object : AVIMMessagesQueryCallback() {
                                                    override fun done(
                                                        msgs: MutableList<AVIMMessage>?,
                                                        e: AVIMException?
                                                    ) {
                                                        if (e != null || msgs.isNullOrEmpty()) return
                                                        val msg = msgs.first()
                                                        CONVERSATIONS[conversationId] = ConversationBean(
                                                            conversationId,
                                                            parseTimeStamp(msg.timestamp),
                                                            when (msg) {
                                                                is AVIMTextMessage -> msg.text
                                                                is AVIMImageMessage -> "[图片]"
                                                                else -> "[未知消息]"
                                                            },
                                                            user.getString(KEY_NICKNAME),
                                                            unreadCount,
                                                            user.getString(KEY_USER_HEADER),
                                                            conversationId
                                                        )
                                                        mObservers[context.packageName]?.onMessage(msg, conversation)
                                                    }
                                                }
                                            )
                                        }
                                    }
                            }
                        }
                    )
            }
    }

    interface MessageObserver {
        fun onMessage(message: AVIMMessage, conversation: AVIMConversation)
    }
}