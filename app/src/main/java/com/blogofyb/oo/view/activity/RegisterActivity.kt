package com.blogofyb.oo.view.activity

import android.app.Activity
import android.os.Bundle
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.interfaces.model.IRegisterModel
import com.blogofyb.oo.interfaces.presenter.IRegisterPresenter
import com.blogofyb.oo.interfaces.view.IRegisterView
import com.blogofyb.oo.presenter.RegisterPresenter
import com.blogofyb.oo.util.extensions.toast
import kotlinx.android.synthetic.main.activity_register.*

/**
 * Create by yuanbing
 * on 2019/8/15
 */
class RegisterActivity : BaseActivity<IRegisterView, IRegisterPresenter, IRegisterModel>(),
    IRegisterView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView()
    }

    private fun initView() {
        btn_register.setOnClickListener {
            presenter?.register(
                et_account_register.text.toString(),
                et_password_register.text.toString(),
                et_re_password_register.text.toString()
            )
        }

        tv_goto_login.setOnClickListener { finish() }
    }

    override fun getViewToAttach() = this

    override fun createPresenter() = RegisterPresenter()

    override fun registerFaild(msg: String) {
        toast(msg)
    }

    override fun registerSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}