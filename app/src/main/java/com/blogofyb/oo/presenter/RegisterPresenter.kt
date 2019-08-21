package com.blogofyb.oo.presenter

import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.interfaces.model.IRegisterModel
import com.blogofyb.oo.interfaces.presenter.IRegisterPresenter
import com.blogofyb.oo.interfaces.view.IRegisterView
import com.blogofyb.oo.model.RegisterModel

/**
 * Create by yuanbing
 * on 2019/8/15
 */
class RegisterPresenter : BasePresenter<IRegisterView, IRegisterModel>(), IRegisterPresenter {
    override fun attachModel() = RegisterModel()

    override fun register(account: String, password: String, repassword: String) {
        model?.register(account, password, repassword, object : IRegisterModel.Callback {
            override fun registerSuccess() {
                view?.registerSuccess()
            }

            override fun registerFailed(msg: String) {
                view?.registerFaild(msg)
            }
        })
    }
}