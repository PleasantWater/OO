package com.blogofyb.oo.presenter

import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.interfaces.model.IActivityEditUserInformationModel
import com.blogofyb.oo.interfaces.presenter.IActivityEditUserInformationPresenter
import com.blogofyb.oo.interfaces.view.IActivityEditUserInformationView
import com.blogofyb.oo.model.ActivityEditUserInformationModel

/**
 * Create by yuanbing
 * on 2019/8/17
 */
class ActivityEditUserInformationPresenter :
        BasePresenter<IActivityEditUserInformationView, IActivityEditUserInformationModel>(),
        IActivityEditUserInformationPresenter {
    override fun attachModel() = ActivityEditUserInformationModel()

    override fun saveChange(userInformation: UserBean) {
        model?.updateUserInformation(userInformation)
    }
}