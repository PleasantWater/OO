package com.blogofyb.oo.interfaces.model

import com.blogofyb.oo.base.mvp.IBaseModel

/**
 * Create by yuanbing
 * on 2019/8/15
 */
interface IRegisterModel : IBaseModel {
    fun register(account: String, password: String, repassword: String, callback: Callback)

    interface Callback {
        fun registerSuccess()
        fun registerFailed(msg: String)
    }
}