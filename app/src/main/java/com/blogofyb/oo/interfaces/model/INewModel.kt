package com.blogofyb.oo.interfaces.model

import com.blogofyb.oo.base.mvp.IBaseModel
import com.blogofyb.oo.bean.NewBean

/**
 * Create by yuanbing
 * on 8/23/19
 */
interface INewModel : IBaseModel {
    fun getMore(username: String, callback: GetCallback)
    fun getNew(username: String, callback: GetCallback)
    fun postNew(new: NewBean, callback: SendCallback)

    interface SendCallback {
        fun sendNewSuccess(new: NewBean)
        fun sendNewFailed()
    }

    interface GetCallback {
        fun getNewSuccess(new: List<NewBean>)
        fun getNewFailed()
    }
}