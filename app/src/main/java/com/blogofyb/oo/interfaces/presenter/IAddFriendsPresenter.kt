package com.blogofyb.oo.interfaces.presenter

import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.interfaces.model.IAddFriendModel
import com.blogofyb.oo.interfaces.view.IAddFriendsView

/**
 * Create by yuanbing
 * on 2019/8/21
 */
interface IAddFriendsPresenter : IBasePresenter<IAddFriendsView, IAddFriendModel> {
    fun searchUser(usernameOrNickname: String)
}