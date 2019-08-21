package com.blogofyb.oo.interfaces.model

import com.blogofyb.oo.base.mvp.IBaseModel
import com.blogofyb.oo.bean.UserBean

/**
 * Create by yuanbing
 * on 2019/8/17
 */
interface IActivityEditUserInformationModel : IBaseModel {
    fun updateUserInformation(userInformation: UserBean)
}