package com.blogofyb.oo.model

import android.annotation.SuppressLint
import android.util.Log
import cn.leancloud.AVInstallation
import cn.leancloud.AVUser
import cn.leancloud.Messages
import cn.leancloud.im.AVIMOptions
import cn.leancloud.im.v2.AVIMClient
import cn.leancloud.im.v2.AVIMClientOpenOption
import cn.leancloud.im.v2.AVIMException
import cn.leancloud.im.v2.callback.AVIMClientCallback
import cn.leancloud.session.AVConnectionListener
import cn.leancloud.session.AVConnectionManager
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.config.KEY_INSTALLATION_ID
import com.blogofyb.oo.interfaces.model.ILoginModel
import com.blogofyb.oo.util.GlobalMessageManager
import com.blogofyb.oo.util.UserManager
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by yuanbing
 * on 2019/8/15
 */
class LoginModel : BaseModel(), ILoginModel {
    @SuppressLint("CheckResult")
    override fun login(account: String, password: String, remember: Boolean, callback: ILoginModel.Callback) {
        val check = checkInput(account, password)
        if (check.first) {
            AVUser.logIn(account, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map {
                        if (remember) {
                            UserManager.saveUserInformation(account, password)
                        }
                        AVInstallation.getCurrentInstallation().saveInBackground()
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .safeSubscribeBy {
                                    val user = AVUser.currentUser()
                                    user?.put(KEY_INSTALLATION_ID, AVInstallation.getCurrentInstallation().installationId)
                                    user?.save()
                                }
                        GlobalMessageManager.mClient = AVIMClient.getInstance(it.username)
                        GlobalMessageManager.mClient.open(
                            object : AVIMClientCallback() {
                                override fun done(client: AVIMClient?, e: AVIMException?) {
                                    Log.d("Client#open", if (e == null) "open client success" else "open client failed")
                                }
                            }
                        )
                        it
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        callback.onSuccess()
                    }, {
                        if (it.message?.isNotBlank() == true) {
                            callback.onFailed(it.message!!)
                        }
                    })
        } else {
            callback.onFailed(check.second)
        }
    }

    private fun checkInput(
            account: String,
            password: String
    ): Pair<Boolean, String> {
        return when {
            account.isBlank() || password.isBlank() -> Pair(false, "输入不能为空")
            else -> Pair(true, "")
        }
    }
}