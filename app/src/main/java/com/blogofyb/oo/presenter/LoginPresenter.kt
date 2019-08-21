package com.blogofyb.oo.presenter

import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.interfaces.model.ILoginModel
import com.blogofyb.oo.interfaces.presenter.ILoginPresenter
import com.blogofyb.oo.interfaces.view.ILoginView
import com.blogofyb.oo.model.LoginModel

/**
 * Create by yuanbing
 * on 2019/8/15
 */
class LoginPresenter : BasePresenter<ILoginView, ILoginModel>(), ILoginPresenter {
    override fun attachModel() = LoginModel()

    override fun login(account: String, password: String, remember: Boolean) {
        model?.login(account, password, remember, object : ILoginModel.Callback {
            override fun onSuccess() {
                view?.loginSuccess()
            }

            override fun onFailed(msg: String) {
                view?.loginFailed(msg)
            }
        })
    }
}