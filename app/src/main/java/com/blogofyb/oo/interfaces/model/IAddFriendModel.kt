package com.blogofyb.oo.interfaces.model

import com.blogofyb.oo.base.mvp.IBaseModel
import com.blogofyb.oo.bean.UserBean

/**
 * Create by yuanbing
 * on 2019/8/21
 */
interface IAddFriendModel : IBaseModel {
    fun searchUser(usernameOrNickname: String, callback: (List<UserBean>) -> Unit)
}