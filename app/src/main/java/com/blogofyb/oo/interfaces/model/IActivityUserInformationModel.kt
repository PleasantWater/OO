package com.blogofyb.oo.interfaces.model

import com.blogofyb.oo.base.mvp.IBaseModel
import com.blogofyb.oo.bean.UserBean

/**
 * Create by yuanbing
 * on 2019/8/17
 */
interface IActivityUserInformationModel : IBaseModel {
    fun getUserInformation(username: String, callback: (UserBean) -> Unit)
    fun updateUserHead(path: String, callback: () -> Unit)
    fun updateUserBg(path: String, callback: () -> Unit)
}