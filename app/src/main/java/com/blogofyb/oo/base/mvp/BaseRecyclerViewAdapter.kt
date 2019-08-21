package com.blogofyb.oo.base.mvp

import androidx.recyclerview.widget.RecyclerView
import com.blogofyb.oo.base.BaseRecyclerViewAdapter

/**
 * Create by yuanbing
 * on 2019/8/17
 */
abstract class BaseRecyclerViewAdapter<HEADER, ITEM, FOOTER, V: IBaseView, P:IBasePresenter<V, M>, M: IBaseModel>(
    header: HEADER,
    items: MutableList<ITEM>,
    footer: FOOTER,
    haveMore: Boolean
): BaseRecyclerViewAdapter<HEADER, ITEM, FOOTER>(header, items, footer, haveMore) {
    protected var presenter: P
    var mPage = 0

    init {
        presenter = createPresenter()
        presenter.attachModel()
        presenter.attachView(getViewToAttach())
    }

    protected abstract fun createPresenter(): P

    protected abstract fun getViewToAttach(): V

    override fun bindFooterViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mPage++
    }
}