package com.blogofyb.oo.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blogofyb.oo.interfaces.OnRecyclerViewEndListener

/**
 * Create by yuanbing
 * on 2019/8/16
 */
const val TYPE_HEADER = 0
const val TYPE_ITEM = 1
const val TYPE_FOOTER = 2
abstract class BaseRecyclerViewAdapter<HEADER, ITEM, FOOTER>(
    protected val mHeader: HEADER,
    protected var mItems: MutableList<ITEM>,
    protected val mFooter: FOOTER,
    protected var mHaveMore: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var mListener: OnRecyclerViewEndListener? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            itemCount - 1 -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }

    override fun getItemCount() = mItems.size + 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> createHeaderViewHolder(parent)
            TYPE_ITEM -> createItemViewHolder(parent)
            else -> createFooterViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> bindHeaderViewHolder(holder, position)
            TYPE_ITEM -> bindItemViewHolder(holder, position)
            else -> {
                if (mHaveMore) {
                    mListener?.beforeOnEnd(holder.itemView)
                    bindFooterViewHolder(holder, position)
                    mListener?.afterOnEnd(holder.itemView)
                }
            }
        }
    }

    abstract fun createHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun createItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun createFooterViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    abstract fun bindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    abstract fun bindFooterViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    fun addOnEndLister(listener: OnRecyclerViewEndListener) {
        mListener = listener
    }

    fun refreshData(items: List<ITEM>, haveMore: Boolean) {
        mItems = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addMore(items: List<ITEM>, haveMore: Boolean) {
        mItems.addAll(items)
        mHaveMore = haveMore
    }
}