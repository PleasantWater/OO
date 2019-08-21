package com.blogofyb.oo.presenter

import cn.leancloud.im.v2.AVIMConversation
import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.interfaces.model.IChatModel
import com.blogofyb.oo.interfaces.presenter.IChatPresenter
import com.blogofyb.oo.interfaces.view.IChatView
import com.blogofyb.oo.model.ChatModel

/**
 * Create by yuanbing
 * on 2019/8/19
 */
class ChatPresenter : BasePresenter<IChatView, IChatModel>(), IChatPresenter {
    override fun attachModel() = ChatModel()

    override fun getMessage() {
        model?.getMessage(10) { view?.showMessage(it) }
    }

    override fun sendText(text: String) {
        model?.sendText(text) { view?.sendMessageSuccess(it) }
    }

    override fun sendVideo(path: String) {
    }

    override fun sendAudio(path: String) {
    }

    override fun sendPic(path: String) {
        model?.sendPic(path) { view?.sendMessageSuccess(it) }
    }

    override fun bindConversation(conversation: AVIMConversation) {
        model?.bindConversation(conversation)
    }

    override fun getMoreMessage() {
        model?.getMoreMessage { view?.showMoreMessage(it) }
    }
}