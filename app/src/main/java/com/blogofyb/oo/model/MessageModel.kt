package com.blogofyb.oo.model

import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.ConversationBean
import com.blogofyb.oo.interfaces.model.IMessageModel
import com.blogofyb.oo.util.GlobalMessageManager
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by yuanbing
 * on 2019/8/19
 */
class MessageModel : BaseModel(), IMessageModel {
    override fun getMessage(callback: (List<ConversationBean>) -> Unit) {
        Observable.just(GlobalMessageManager.CONVERSATIONS.values)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { conversations ->
                conversations.toList().sortedBy { conversationBean ->
                    conversationBean.time
                }.reversed()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .safeSubscribeBy { callback(it) }
    }
}