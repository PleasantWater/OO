package com.blogofyb.oo.util.extensions

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.blogofyb.oo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Create by yuanbing
 * on 2019/8/16
 */
fun Context.loadImage(
    rowUrl: String?,
    imageView: ImageView,
    @DrawableRes placeholder: Int = R.drawable.ic_image_place_holder,
    @DrawableRes error: Int = R.drawable.ic_image_place_holder
) {
    val url = when {
        rowUrl.isNullOrEmpty() -> {
            imageView.setImageResource(error)
            return
        }
        else -> rowUrl
    }
    Glide.with(this)
        .load(url)
        .apply(RequestOptions().placeholder(placeholder).error(error))
        .into(imageView)
}

fun ImageView.setImageFromUrl(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.ic_image_place_holder,
    @DrawableRes error: Int = R.drawable.ic_image_place_holder
) = context.loadImage(url, this, placeholder, error)