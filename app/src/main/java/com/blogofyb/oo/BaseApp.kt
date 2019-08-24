package com.blogofyb.oo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.PushService
import com.blogofyb.oo.view.activity.MainActivity

/**
 * Create by yuanbing
 * on 2019/8/15
 */
class BaseApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        // 初始化LeanCloud
        AVOSCloud.initialize(this, "cHihduhooJaIQx4Fge4TnFNp-gzGzoHsz",
            "LEgfaaGyj5yyAqmNb24iYgLh")
        PushService.setDefaultChannelId(this, context.packageName)
        PushService.setDefaultPushCallback(this, MainActivity::class.java)
    }
}