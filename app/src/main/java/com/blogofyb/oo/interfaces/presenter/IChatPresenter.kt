package com.blogofyb.oo.interfaces.presenter

import cn.leancloud.im.v2.AVIMConversation
import cn.leancloud.im.v2.AVIMMessage
import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.interfaces.model.IChatModel
import com.blogofyb.oo.interfaces.view.IChatView

/**
 * Create by yuanbing
 * on 2019/8/19
 */
interface IChatPresenter : IBasePresenter<IChatView, IChatModel> {
    fun getMessage()
    fun sendText(text: String)
    fun sendVideo(path: String)
    fun sendAudio(path: String)
    fun sendPic(path: String)
    fun bindConversation(conversation: AVIMConversation)
    fun getMoreMessage()
}