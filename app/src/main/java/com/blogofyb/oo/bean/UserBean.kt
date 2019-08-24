package com.blogofyb.oo.bean

import java.io.Serializable

/**
 * Create by yuanbing
 * on 2019/8/17
 */
data class UserBean(
        val username: String,
        val nickname: String,
        val signature: String,
        val header: String,
        val gender: String,
        val hometown: String,
        val school: String,
        val bg: String,
        var userType: Int = 0  // 0 -> 当前用户，-1 -> follower，1 -> 非follower
) : Serializable