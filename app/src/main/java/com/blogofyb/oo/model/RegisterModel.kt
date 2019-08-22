package com.blogofyb.oo.model

import android.annotation.SuppressLint
import com.avos.avoscloud.AVException
//import cn.leancloud.AVUser
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback
import com.avos.avoscloud.SignUpCallback
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.config.KEY_NICKNAME
import com.blogofyb.oo.interfaces.model.IRegisterModel
import com.blogofyb.oo.util.UserManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by yuanbing
 * on 2019/8/15
 */
class RegisterModel : BaseModel(), IRegisterModel {
    @SuppressLint("CheckResult")
    override fun register(
        account: String,
        password: String,
        repassword: String,
        callback: IRegisterModel.Callback
    ) {
        val check = checkInput(account, password, repassword)
        if (check.first) {
            val user = AVUser()
            user.username = account
            user.setPassword(password)
            user.put(KEY_NICKNAME, account)
            user.signUpInBackground(
                object : SignUpCallback() {
                    override fun done(e: AVException?) {
                        if (e != null)  {
                            callback.registerFailed(e.message!!)
                        } else {
                            UserManager.saveUserInformation(account, password)
                            AVUser.logInInBackground(
                                account,
                                password,
                                object : LogInCallback<AVUser>() {
                                    override fun done(user: AVUser?, e: AVException?) {
                                        if (e != null) return
                                        callback.registerSuccess()
                                    }
                                }
                            )
                        }
                    }
                }
            )
        } else {
            callback.registerFailed(check.second)
        }
    }

    private fun checkInput(
            account: String,
            password: String,
            repassword: String
    ): Pair<Boolean, String> {
        return when {
            account.isBlank() || password.isBlank() || repassword.isBlank() -> Pair(false, "输入不能为空")
            password != repassword -> Pair(false, "两次输入的密码不一致")
            else -> Pair(true, "")
        }
    }
}