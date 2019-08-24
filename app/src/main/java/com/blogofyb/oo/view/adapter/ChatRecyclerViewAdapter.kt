package com.blogofyb.oo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blogofyb.oo.R
import com.blogofyb.oo.bean.MessageBean
import com.blogofyb.oo.util.event.OnMessageScrollToTopEvent
import com.blogofyb.oo.util.extensions.setImageFromUrl
import com.blogofyb.oo.view.activity.showPhotos
import org.greenrobot.eventbus.EventBus

/**
 * Create by yuanbing
 * on 2019/8/19
 */
class ChatRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val sendTextMessage: Int = 0
    private val sendAudioMessage: Int = 1
    private val sendVideoMessage: Int = 2
    private val sendPicMessage: Int = 3
    private val receiveTextMessage: Int = 4
    private val receiveAudioMessage: Int = 5
    private val receiveVideoMessage: Int = 6
    private val receivePicMessage: Int = 7
    private var mMessage: MutableList<MessageBean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            sendTextMessage -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.recycle_item_text_message_send, parent, false)
                SendTextMessageViewHolder(view)
            }
            receiveTextMessage -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.recycle_item_text_message_receive, parent, false)
                ReceiveTextMessageViewHolder(view)
            }
            sendPicMessage -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.recycle_item_img_message_send, parent, false)
                SendPicMessageViewHolder(view)
            }
            receivePicMessage -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.recycle_item_img_message_receive, parent, false)
                ReceivePicMessageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.recycle_item_text_message_send, parent, false)
                SendTextMessageViewHolder(view)
            }
        }
    }

    override fun getItemCount() = mMessage.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position == 0) {
            EventBus.getDefault().post(OnMessageScrollToTopEvent())
        }

        val message = mMessage[position]
        when (getItemViewType(position)) {
            sendTextMessage -> {
                holder as SendTextMessageViewHolder
                holder.header.setImageFromUrl(message.header)
                holder.text.text = message.text
            }
            receiveTextMessage -> {
                holder as ReceiveTextMessageViewHolder
                holder.header.setImageFromUrl(message.header)
                holder.text.text = message.text
            }
            sendPicMessage -> {
                holder as SendPicMessageViewHolder
                holder.pic.setImageFromUrl(message.pic)
                holder.header.setImageFromUrl(message.header)
                holder.pic.setOnClickListener {
                    showPhotos(it.context, listOf(message.pic))
                }
            }
            receivePicMessage -> {
                holder as ReceivePicMessageViewHolder
                holder.pic.setImageFromUrl(message.pic)
                holder.header.setImageFromUrl(message.header)
                holder.pic.setOnClickListener {
                    showPhotos(it.context, listOf(message.pic))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = mMessage[position]
        return if (message.isSend) {
            when {
                message.pic.isNotBlank() -> sendPicMessage
                message.video.isNotBlank() -> sendVideoMessage
                message.audio.isNotBlank() -> sendAudioMessage
                else -> sendTextMessage
            }
        } else {
            when {
                message.pic.isNotBlank() -> receivePicMessage
                message.video.isNotBlank() -> receiveVideoMessage
                message.audio.isNotBlank() -> receiveAudioMessage
                else -> receiveTextMessage
            }
        }
    }

    fun refreshData(message: List<MessageBean>) {
        mMessage = message.toMutableList()
        notifyDataSetChanged()
    }

    fun newMessage(message: MessageBean) {
        mMessage.add(message)
        notifyDataSetChanged()
    }

    fun showMoreMessage(message: List<MessageBean>) {
        mMessage.addAll(0, message)
        notifyItemRangeInserted(0, message.size)
    }
}

class SendTextMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.findViewById(R.id.tv_recycle_item_text_message_send)
    val header: ImageView = view.findViewById(R.id.iv_user_head)
}

class ReceiveTextMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.findViewById(R.id.tv_recycle_item_text_message_receive)
    val header: ImageView = view.findViewById(R.id.iv_user_head)
}

class SendPicMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val pic: ImageView = view.findViewById(R.id.iv_recycle_item_img_message_send)
    val header: ImageView = view.findViewById(R.id.iv_user_head)
}

class ReceivePicMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val pic: ImageView = view.findViewById(R.id.iv_recycle_item_img_message_receive)
    val header: ImageView = view.findViewById(R.id.iv_user_head)
}