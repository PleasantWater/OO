package com.blogofyb.oo.presenter

import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.interfaces.model.IAddFriendModel
import com.blogofyb.oo.interfaces.presenter.IAddFriendsPresenter
import com.blogofyb.oo.interfaces.view.IAddFriendsView
import com.blogofyb.oo.model.AddFriendsModel

/**
 * Create by yuanbing
 * on 2019/8/21
 */
class AddFriendsPresenter : BasePresenter<IAddFriendsView, IAddFriendModel>(), IAddFriendsPresenter {
    override fun attachModel() = AddFriendsModel()

    override fun searchUser(usernameOrNickname: String) {
        model?.searchUser(usernameOrNickname) { view?.showSearchResult(it) }
    }
}