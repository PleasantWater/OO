package com.blogofyb.oo.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blogofyb.oo.R
import com.blogofyb.oo.bean.ConversationBean
import com.blogofyb.oo.config.KEY_NICKNAME
import com.blogofyb.oo.config.KEY_USERNAME
import com.blogofyb.oo.config.KEY_USER_HEADER
import com.blogofyb.oo.util.extensions.gone
import com.blogofyb.oo.util.extensions.setImageFromUrl
import com.blogofyb.oo.util.extensions.visible
import com.blogofyb.oo.view.activity.ChatActivity

/**
 * Create by yuanbing
 * on 2019/8/18
 */
class MessageRecyclerViewAdapter : RecyclerView.Adapter<MessageItemViewHolder>() {
    private var mConversation: List<ConversationBean> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_message,
            parent, false)
        return MessageItemViewHolder(view)
    }

    override fun getItemCount() = mConversation.size

    override fun onBindViewHolder(holder: MessageItemViewHolder, position: Int) {
        val conversation = mConversation[position]
        holder.mNickname.text = conversation.nickname
        holder.mHeader.setImageFromUrl(conversation.header)
        holder.mTime.text = conversation.time
        holder.mDescription.text = conversation.message
        if (conversation.unreadCount == 0) {
            holder.mUnreadCount.gone()
        } else {
            holder.mUnreadCount.visible()
            holder.mUnreadCount.text = conversation.unreadCount.toString()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ChatActivity::class.java)
            intent.putExtra(KEY_USERNAME, conversation.username)
            intent.putExtra(KEY_NICKNAME, conversation.nickname)
            intent.putExtra(KEY_USER_HEADER, conversation.header)
            it.context.startActivity(intent)
        }
    }

    fun refreshData(conversation: List<ConversationBean>) {
        mConversation = conversation
        notifyDataSetChanged()
    }
}

class MessageItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val mHeader: ImageView = view.findViewById(R.id.civ_recycle_item_message_header)
    val mNickname: TextView = view.findViewById(R.id.tv_recycle_item_message_nickname)
    val mTime: TextView = view.findViewById(R.id.tv_recycle_item_message_time)
    val mDescription: TextView = view.findViewById(R.id.tv_recycle_item_message_description)
    val mUnreadCount: TextView = view.findViewById(R.id.tv_recycle_item_message_unread_count)
}