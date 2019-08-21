package com.blogofyb.oo.presenter

import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.interfaces.model.IFragmentFriendsModel
import com.blogofyb.oo.interfaces.presenter.IFragmentFriendsPresenter
import com.blogofyb.oo.interfaces.view.IFragmentFriendsView
import com.blogofyb.oo.model.FragmentFriendsModel

/**
 * Create by yuanbing
 * on 2019/8/18
 */
class FragmentFriendsPresenter : BasePresenter<IFragmentFriendsView, IFragmentFriendsModel>(),
    IFragmentFriendsPresenter {
    override fun attachModel() = FragmentFriendsModel()

    override fun getFriends() {
        model?.getFriends { view?.showFriends(it) }
    }
}