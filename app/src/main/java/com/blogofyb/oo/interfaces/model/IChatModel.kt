package com.blogofyb.oo.interfaces.model

import cn.leancloud.im.v2.AVIMConversation
import cn.leancloud.im.v2.AVIMMessage
import com.blogofyb.oo.base.mvp.IBaseModel
import com.blogofyb.oo.bean.MessageBean

/**
 * Create by yuanbing
 * on 2019/8/19
 */
interface IChatModel : IBaseModel {
    fun sendText(text: String, callback: (MessageBean) -> Unit)
    fun sendVideo(path: String, callback: (MessageBean) -> Unit)
    fun sendAudio(path: String, callback: (MessageBean) -> Unit)
    fun sendPic(path: String, callback: (MessageBean) -> Unit)
    fun getMessage(limit: Int, callback: (List<MessageBean>) -> Unit)
    fun bindConversation(conversation: AVIMConversation)
    fun getMoreMessage(callback: (List<MessageBean>) -> Unit)
}