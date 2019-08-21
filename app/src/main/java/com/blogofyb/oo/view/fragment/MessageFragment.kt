package com.blogofyb.oo.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.im.v2.AVIMConversation
import cn.leancloud.im.v2.AVIMMessage
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseFragment
import com.blogofyb.oo.bean.ConversationBean
import com.blogofyb.oo.interfaces.model.IMessageModel
import com.blogofyb.oo.interfaces.presenter.IMessagePresenter
import com.blogofyb.oo.interfaces.view.IMessageView
import com.blogofyb.oo.presenter.MessagePresenter
import com.blogofyb.oo.util.GlobalMessageManager
import com.blogofyb.oo.view.adapter.MessageRecyclerViewAdapter

/**
 * Create by yuanbing
 * on 2019/8/19
 */
class MessageFragment : BaseFragment<IMessageView, IMessagePresenter, IMessageModel>(), IMessageView {
    private lateinit var mAdapter: MessageRecyclerViewAdapter

    override fun onCreateView(view: View, savedInstanceState: Bundle?) {
        val message: RecyclerView = view.findViewById(R.id.rv_message)
        mAdapter = MessageRecyclerViewAdapter()
        message.adapter = mAdapter
        message.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()
        presenter?.getMessage()
        GlobalMessageManager.subscribe(
            observer = object : GlobalMessageManager.MessageObserver {
                override fun onMessage(message: AVIMMessage, conversation: AVIMConversation) {
                    presenter?.getMessage()
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        GlobalMessageManager.unSubscribe()
    }

    override fun getLayoutRes() = R.layout.fragment_message

    override fun getViewToAttach() = this

    override fun createPresenter() = MessagePresenter()

    override fun showMessage(message: List<ConversationBean>) {
        mAdapter.refreshData(message)
    }
}