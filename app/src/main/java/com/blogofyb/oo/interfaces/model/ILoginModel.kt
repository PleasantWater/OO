package com.blogofyb.oo.interfaces.model

import com.blogofyb.oo.base.mvp.IBaseModel

/**
 * Create by yuanbing
 * on 2019/8/15
 */
interface ILoginModel : IBaseModel {
    fun login(account: String, password: String, remember: Boolean, callback: Callback)

    interface Callback {
        fun onSuccess()
        fun onFailed(msg: String)
    }
}