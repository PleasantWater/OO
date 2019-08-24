package com.blogofyb.oo.interfaces.view

import com.blogofyb.oo.base.mvp.IBaseView
import com.blogofyb.oo.bean.NewBean

/**
 * Create by yuanbing
 * on 8/23/19
 */
interface INewView : IBaseView {
    fun showNew(new: List<NewBean>)
    fun getNewFailed()
    fun sendNewSuccess(new: NewBean)
    fun sendNewFailed()
}