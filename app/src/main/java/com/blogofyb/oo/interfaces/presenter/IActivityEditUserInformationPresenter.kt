package com.blogofyb.oo.interfaces.presenter

import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.interfaces.model.IActivityEditUserInformationModel
import com.blogofyb.oo.interfaces.view.IActivityEditUserInformationView

/**
 * Create by yuanbing
 * on 2019/8/17
 */
interface IActivityEditUserInformationPresenter :
        IBasePresenter<IActivityEditUserInformationView, IActivityEditUserInformationModel> {
    fun saveChange(userInformation: UserBean)
}