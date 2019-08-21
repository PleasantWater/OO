package com.blogofyb.oo.interfaces.view

import com.blogofyb.oo.base.mvp.IBaseView
import com.blogofyb.oo.bean.MessageBean

/**
 * Create by yuanbing
 * on 2019/8/19
 */
interface IChatView : IBaseView {
    fun showMessage(message: List<MessageBean>)
    fun sendMessageSuccess(message: MessageBean)
    fun showMoreMessage(message: List<MessageBean>)
}