package com.blogofyb.oo.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.interfaces.model.ILoginModel
import com.blogofyb.oo.interfaces.presenter.ILoginPresenter
import com.blogofyb.oo.interfaces.view.ILoginView
import com.blogofyb.oo.presenter.LoginPresenter
import com.blogofyb.oo.util.GlobalMessageManager
import com.blogofyb.oo.util.UserManager
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import com.blogofyb.oo.util.extensions.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by yuanbing
 * on 2019/8/15
 */
class SplashActivity : BaseActivity<ILoginView, ILoginPresenter, ILoginModel>(), ILoginView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initSplash()
        autoLogin()


        GlobalMessageManager.loadDataFromFile()
        Log.e("onCreate", "load data from file")
    }

    private fun autoLogin(): Boolean {
        return if (UserManager.isHaveUser()) {
            val currentUserInfo = UserManager.currentUserInfo()
            presenter?.login(currentUserInfo.first, currentUserInfo.second, true)
            true
        } else {
            false
        }
    }

    private fun initSplash() {
        Observable.create<Boolean> {
            val isHaveUser = autoLogin()
            Thread.sleep(2000)
            it.onNext(isHaveUser)
            it.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .safeSubscribeBy {
                if (it) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }

    override fun createPresenter() = LoginPresenter()

    override fun getViewToAttach() = this

    override fun loginFailed(msg: String) {
        toast(msg)
    }

    override fun loginSuccess() {}
}