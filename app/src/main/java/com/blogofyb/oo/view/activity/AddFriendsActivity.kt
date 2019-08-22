package com.blogofyb.oo.view.activity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.interfaces.model.IAddFriendModel
import com.blogofyb.oo.interfaces.presenter.IAddFriendsPresenter
import com.blogofyb.oo.interfaces.view.IAddFriendsView
import com.blogofyb.oo.presenter.AddFriendsPresenter
import kotlinx.android.synthetic.main.activity_add_friends.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Create by yuanbing
 * on 2019/8/20
 */
class AddFriendsActivity : BaseActivity<IAddFriendsView, IAddFriendsPresenter, IAddFriendModel>(),
    IAddFriendsView {
    private lateinit var mIMEManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)
        mIMEManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        initToolbar()
        initEditText()
    }

    private fun initEditText() {
        et_search_friend.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (et_search_friend.text.isNotBlank()) {
                    presenter?.searchUser(et_search_friend.text.toString())
                    mIMEManager.hideSoftInputFromWindow(textView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
                true
            } else {
                false
            }
        }
    }

    private fun initToolbar() {
        toolbar.title = "添加"
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun getViewToAttach() = this

    override fun createPresenter() = AddFriendsPresenter()

    override fun showSearchResult(user: List<UserBean>) {

    }
}