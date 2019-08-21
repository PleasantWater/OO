package com.blogofyb.oo.bean

/**
 * Create by yuanbing
 * on 2019/8/18
 */
data class ConversationBean(
    var username: String = "",
    var time: String = "",
    var message: String = "",
    var nickname: String = "",
    var unreadCount: Int = 0,
    var header: String = "",
    var conversationId: String =""
)