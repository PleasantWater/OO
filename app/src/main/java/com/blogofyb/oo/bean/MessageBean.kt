package com.blogofyb.oo.bean

import java.io.Serializable

/**
 * Create by yuanbing
 * on 2019/8/19
 */
data class MessageBean(
    val text: String = "",
    val pic: String = "",
    val audio: String = "",
    val video: String = "",
    var header: String = "",
    val isSend: Boolean = false
) : Serializable