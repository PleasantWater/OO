package com.blogofyb.oo.interfaces.view

import com.blogofyb.oo.base.mvp.IBaseView
import com.blogofyb.oo.bean.ConversationBean

/**
 * Create by yuanbing
 * on 2019/8/19
 */
interface IMessageView : IBaseView {
    fun showMessage(message: List<ConversationBean>)
}