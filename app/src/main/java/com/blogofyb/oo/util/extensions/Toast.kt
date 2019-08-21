package com.blogofyb.oo.util.extensions

import android.widget.Toast
import com.blogofyb.oo.BaseApp

/**
 * Create by yuanbing
 * on 2019/8/15
 */
fun toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(BaseApp.context, msg, length).show()
}