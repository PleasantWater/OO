package com.blogofyb.oo.interfaces.view

import com.blogofyb.oo.base.mvp.IBaseView
import com.blogofyb.oo.bean.UserBean

/**
 * Create by yuanbing
 * on 2019/8/17
 */
interface IActivityUserInformationView : IBaseView {
    fun showUserInformation(userInformation: UserBean)
}