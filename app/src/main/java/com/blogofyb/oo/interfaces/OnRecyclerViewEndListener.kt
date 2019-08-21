package com.blogofyb.oo.interfaces

import android.view.View

/**
 * Create by yuanbing
 * on 2019/8/16
 */
interface OnRecyclerViewEndListener {
    fun beforeOnEnd(view: View)
    fun afterOnEnd(view: View)
}