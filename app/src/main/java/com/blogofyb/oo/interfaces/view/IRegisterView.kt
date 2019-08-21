package com.blogofyb.oo.interfaces.view

import com.blogofyb.oo.base.mvp.IBaseView

/**
 * Create by yuanbing
 * on 2019/8/15
 */
interface IRegisterView : IBaseView {
    fun registerFaild(msg: String)
    fun registerSuccess()
}