package com.blogofyb.oo.interfaces.view

import com.blogofyb.oo.base.mvp.IBaseView
import com.blogofyb.oo.bean.UserBean

/**
 * Create by yuanbing
 * on 2019/8/21
 */
interface IAddFriendsView : IBaseView {
    fun showSearchResult(user: List<UserBean>)
}