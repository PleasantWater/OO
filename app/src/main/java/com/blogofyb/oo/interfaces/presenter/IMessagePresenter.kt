package com.blogofyb.oo.interfaces.presenter

import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.interfaces.model.IMessageModel
import com.blogofyb.oo.interfaces.view.IMessageView

/**
 * Create by yuanbing
 * on 2019/8/19
 */
interface IMessagePresenter : IBasePresenter<IMessageView, IMessageModel> {
    fun getMessage()
}