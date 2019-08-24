package com.blogofyb.oo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blogofyb.oo.R
import com.blogofyb.oo.util.extensions.setImageFromUrl
import com.blogofyb.oo.view.activity.showPhotos

/**
 * Create by yuanbing
 * on 8/24/19
 */
class NewPicRecyclerViewAdapter(
    private val mPic: List<String>
) : RecyclerView.Adapter<NewPicViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewPicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_new_photo, parent,
            false)
        return NewPicViewHolder(view)
    }

    override fun getItemCount() = mPic.size

    override fun onBindViewHolder(holder: NewPicViewHolder, position: Int) {
        holder.mPic.setImageFromUrl(mPic[position])
        holder.itemView.setOnClickListener { showPhotos(it.context, mPic, position) }
    }
}

class NewPicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val mPic: ImageView = view.findViewById(R.id.iv_recycle_item_new_photo)
}