package com.blogofyb.oo.presenter

import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.interfaces.model.IMessageModel
import com.blogofyb.oo.interfaces.presenter.IMessagePresenter
import com.blogofyb.oo.interfaces.view.IMessageView
import com.blogofyb.oo.model.MessageModel

/**
 * Create by yuanbing
 * on 2019/8/20
 */
class MessagePresenter : BasePresenter<IMessageView, IMessageModel>(), IMessagePresenter {
    override fun attachModel() = MessageModel()

    override fun getMessage() {
        model?.getMessage { view?.showMessage(it) }
    }
}