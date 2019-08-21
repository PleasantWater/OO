package com.blogofyb.oo.interfaces.presenter

import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.interfaces.model.IRegisterModel
import com.blogofyb.oo.interfaces.view.IRegisterView

/**
 * Create by yuanbing
 * on 2019/8/15
 */
interface IRegisterPresenter : IBasePresenter<IRegisterView, IRegisterModel> {
    fun register(account: String, password: String, repassword: String)
}