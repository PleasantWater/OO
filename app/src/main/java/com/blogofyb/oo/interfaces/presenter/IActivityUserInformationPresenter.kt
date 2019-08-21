package com.blogofyb.oo.interfaces.presenter

import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.interfaces.model.IActivityUserInformationModel
import com.blogofyb.oo.interfaces.view.IActivityUserInformationView

/**
 * Create by yuanbing
 * on 2019/8/17
 */
interface IActivityUserInformationPresenter :
        IBasePresenter<IActivityUserInformationView, IActivityUserInformationModel> {
    fun getUserInformation(username: String)
    fun updateUserHead(path: String)
    fun updateUserBg(path: String)
}