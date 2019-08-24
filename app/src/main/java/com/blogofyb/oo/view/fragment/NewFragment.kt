package com.blogofyb.oo.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.avos.avoscloud.AVUser
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseFragment
import com.blogofyb.oo.bean.NewBean
import com.blogofyb.oo.interfaces.model.INewModel
import com.blogofyb.oo.interfaces.presenter.INewPresenter
import com.blogofyb.oo.interfaces.view.INewView
import com.blogofyb.oo.presenter.NewPresenter
import com.blogofyb.oo.util.event.PostNewEvent
import com.blogofyb.oo.view.adapter.NewRecyclerViewAdapter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.toast

/**
 * Create by yuanbing
 * on 8/23/19
 */
class NewFragment : BaseFragment<INewView, INewPresenter, INewModel>(), INewView {
    private lateinit var mAdapter: NewRecyclerViewAdapter
    private lateinit var mRefresh: SwipeRefreshLayout

    override fun onCreateView(view: View, savedInstanceState: Bundle?) {
        val new: RecyclerView = view.findViewById(R.id.rv_new)
        mAdapter = NewRecyclerViewAdapter()
        new.adapter = mAdapter
        new.layoutManager = LinearLayoutManager(context)
        mRefresh = view.findViewById(R.id.srl_new)
        mRefresh.setOnRefreshListener {
            mRefresh.isRefreshing = true
            presenter?.getNew(AVUser.getCurrentUser().username)
        }

        presenter?.getNew(AVUser.getCurrentUser().username)
    }

    override fun getLayoutRes() = R.layout.fragment_new

    override fun getViewToAttach() = this

    override fun createPresenter() = NewPresenter()

    override fun showNew(new: List<NewBean>) {
        mRefresh.isRefreshing = false
        mAdapter.refreshData(new)
    }

    override fun getNewFailed() {
        mRefresh.isRefreshing = false
    }

    override fun sendNewFailed() {
        mRefresh.isRefreshing = false
        toast("发表失败")
    }

    override fun sendNewSuccess(new: NewBean) {
        mRefresh.isRefreshing = false
        mAdapter.newNew(new)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun postNew(event: PostNewEvent) {
        presenter?.sendNew(event.new)
    }
}