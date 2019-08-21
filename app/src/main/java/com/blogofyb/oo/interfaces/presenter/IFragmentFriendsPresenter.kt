package com.blogofyb.oo.interfaces.presenter

import com.blogofyb.oo.base.mvp.IBasePresenter
import com.blogofyb.oo.interfaces.model.IFragmentFriendsModel
import com.blogofyb.oo.interfaces.view.IFragmentFriendsView

/**
 * Create by yuanbing
 * on 2019/8/18
 */
interface IFragmentFriendsPresenter : IBasePresenter<IFragmentFriendsView, IFragmentFriendsModel> {
    fun getFriends()
}