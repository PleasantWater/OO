package com.blogofyb.oo.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseFragment
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.interfaces.model.IFragmentFriendsModel
import com.blogofyb.oo.interfaces.presenter.IFragmentFriendsPresenter
import com.blogofyb.oo.interfaces.view.IFragmentFriendsView
import com.blogofyb.oo.presenter.FragmentFriendsPresenter
import com.blogofyb.oo.view.adapter.FriendsRecyclerViewAdapter

/**
 * Create by yuanbing
 * on 2019/8/18
 */
class FriendsFragment :
    BaseFragment<IFragmentFriendsView ,IFragmentFriendsPresenter, IFragmentFriendsModel>(),
    IFragmentFriendsView {
    private lateinit var mAdapter: FriendsRecyclerViewAdapter
    private lateinit var mRefresh: SwipeRefreshLayout

    override fun onCreateView(view: View, savedInstanceState: Bundle?) {
        val friends = view.findViewById<RecyclerView>(R.id.rv_friends)
        friends.layoutManager = LinearLayoutManager(context)
        mAdapter = FriendsRecyclerViewAdapter()
        friends.adapter = mAdapter
        mRefresh = view.findViewById(R.id.srl_friends)
        mRefresh.setOnRefreshListener {
            mRefresh.isRefreshing = true
            presenter?.getFriends()
        }

        presenter?.getFriends()
    }

    override fun getLayoutRes() = R.layout.fragment_friends

    override fun getViewToAttach() = this

    override fun createPresenter() = FragmentFriendsPresenter()

    override fun showFriends(friends: List<UserBean>) {
        mRefresh.isRefreshing = false
        mAdapter.refreshData(friends)
    }
}