package com.blogofyb.oo.view.activity

import android.os.Bundle
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.interfaces.model.IAddFriendModel
import com.blogofyb.oo.interfaces.presenter.IAddFriendsPresenter
import com.blogofyb.oo.interfaces.view.IAddFriendsView
import com.blogofyb.oo.presenter.AddFriendsPresenter

/**
 * Create by yuanbing
 * on 2019/8/20
 */
class AddFriendsActivity : BaseActivity<IAddFriendsView, IAddFriendsPresenter, IAddFriendModel>(),
    IAddFriendsView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)
    }

    override fun getViewToAttach() = this

    override fun createPresenter() = AddFriendsPresenter()

    override fun showSearchResult(user: List<UserBean>) {

    }
}