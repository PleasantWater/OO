package com.blogofyb.oo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogofyb.oo.R
import com.blogofyb.oo.bean.NewBean
import com.blogofyb.oo.util.DateBuilder
import com.blogofyb.oo.util.event.OnNewEndEvent
import com.blogofyb.oo.util.extensions.gone
import com.blogofyb.oo.util.extensions.setImageFromUrl
import com.blogofyb.oo.util.extensions.visible
import org.greenrobot.eventbus.EventBus

/**
 * Create by yuanbing
 * on 8/23/19
 */
class NewRecyclerViewAdapter : RecyclerView.Adapter<NewViewHolder>() {
    private var mNew: MutableList<NewBean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_new, parent,
            false)
        return NewViewHolder(view)
    }

    override fun getItemCount() = mNew.size

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val new = mNew[position]
        holder.mHeader.setImageFromUrl(new.header)
        holder.mNickname.text = new.nickname
        holder.mTime.text = DateBuilder.time(new.time)
        holder.mContent.text = new.content
        if (new.pic.isNotEmpty()) {
            holder.mPic.visible()
            holder.mPic.layoutManager = GridLayoutManager(
                holder.itemView.context,
                if (new.pic.size > 2) 3 else new.pic.size
            )
            holder.mPic.adapter = NewPicRecyclerViewAdapter(new.pic)
        } else {
            holder.mPic.gone()
        }

        if (position == itemCount - 1) {
            EventBus.getDefault().post(OnNewEndEvent())
        }
    }

    fun refreshData(new: List<NewBean>) {
        mNew = new.toMutableList()
        notifyDataSetChanged()
    }

    fun newNew(new: NewBean) {
        mNew.add(0, new)
        notifyItemInserted(0)
    }

    fun showMoreNew(new: List<NewBean>) {
        mNew.addAll(new)
        notifyDataSetChanged()
    }
}

class NewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val mHeader: ImageView = view.findViewById(R.id.iv_user_head)
    val mNickname: TextView = view.findViewById(R.id.tv_nickname)
    val mTime: TextView = view.findViewById(R.id.tv_time)
    val mContent: TextView = view.findViewById(R.id.tv_content)
    val mPic: RecyclerView = view.findViewById(R.id.rv_new_pic)
}