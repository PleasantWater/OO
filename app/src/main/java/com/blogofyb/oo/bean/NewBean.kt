package com.blogofyb.oo.bean

import java.io.Serializable

/**
 * Create by yuanbing
 * on 8/23/19
 */
data class NewBean(
    val header: String,
    val nickname: String,
    val time: String,
    val content: String,
    val pic: List<String>,
    val username: String
) : Serializable