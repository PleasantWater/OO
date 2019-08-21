package com.blogofyb.oo.interfaces.model

import com.blogofyb.oo.base.mvp.IBaseModel
import com.blogofyb.oo.bean.ConversationBean

/**
 * Create by yuanbing
 * on 2019/8/19
 */
interface IMessageModel : IBaseModel {
    fun getMessage(callback: (List<ConversationBean>) -> Unit)
}