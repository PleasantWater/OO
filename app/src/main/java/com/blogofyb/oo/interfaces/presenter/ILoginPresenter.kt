package com.blogofyb.oo.interfaces.presenter

import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.interfaces.model.ILoginModel
import com.blogofyb.oo.interfaces.view.ILoginView

/**
 * Create by yuanbing
 * on 2019/8/15
 */
interface ILoginPresenter : IBasePresenter<ILoginView, ILoginModel> {
    fun login(account: String, password: String, remember: Boolean)
}