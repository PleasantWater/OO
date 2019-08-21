package com.blogofyb.oo.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blogofyb.oo.R
import com.blogofyb.oo.base.TYPE_HEADER
import com.blogofyb.oo.base.TYPE_ITEM
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.KEY_USERNAME
import com.blogofyb.oo.util.extensions.setImageFromUrl
import com.blogofyb.oo.view.activity.UserInformationActivity

/**
 * Create by yuanbing
 * on 2019/8/18
 */
class FriendsRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mFriends: List<UserBean> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_friend,
                parent, false)
            FriendsHeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_friend,
                parent, false)
            FriendsItemViewHolder(view)
        }
    }

    override fun getItemCount() = mFriends.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_HEADER) {

        } else {
            holder as FriendsItemViewHolder
            val friend = mFriends[position - 1]
            holder.mHeader.setImageFromUrl(friend.header)
            holder.mNickname.text = friend.nickname
            holder.mSignature.text = friend.signature

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, UserInformationActivity::class.java)
                intent.putExtra(KEY_USERNAME, friend.username)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int) = if (position == 0) TYPE_HEADER else TYPE_ITEM

    fun refreshData(friends: List<UserBean>) {
        mFriends = friends
        notifyDataSetChanged()
    }
}

class FriendsHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

}

class FriendsItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val mHeader: ImageView = view.findViewById(R.id.civ_recycle_item_friend_header)
    val mNickname: TextView = view.findViewById(R.id.tv_recycle_item_friend_nickname)
    val mSignature: TextView = view.findViewById(R.id.tv_recycle_item_friend_signature)
}