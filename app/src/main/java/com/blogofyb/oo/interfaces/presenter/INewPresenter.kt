package com.blogofyb.oo.interfaces.presenter

import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.bean.NewBean
import com.blogofyb.oo.interfaces.model.INewModel
import com.blogofyb.oo.interfaces.view.INewView

/**
 * Create by yuanbing
 * on 8/23/19
 */
interface INewPresenter : IBasePresenter<INewView, INewModel> {
    fun getNew(username: String)
    fun sendNew(new: NewBean)
}