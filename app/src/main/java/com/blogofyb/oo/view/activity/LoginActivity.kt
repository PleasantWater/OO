package com.blogofyb.oo.view.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.interfaces.model.ILoginModel
import com.blogofyb.oo.interfaces.presenter.ILoginPresenter
import com.blogofyb.oo.interfaces.view.ILoginView
import com.blogofyb.oo.presenter.LoginPresenter
import com.blogofyb.oo.util.extensions.toast
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Create by yuanbing
 * on 2019/8/15
 */
class LoginActivity : BaseActivity<ILoginView, ILoginPresenter, ILoginModel>(), ILoginView {
    private lateinit var mDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mDialog = Dialog(this)

        initToolbar()
        initView()
    }

    private fun initToolbar() {

    }

    private fun initView() {
        btn_login.setOnClickListener {
            presenter?.login(
                et_account_login.text.toString(),
                et_password_login.text.toString(),
                switch_remember_login.isChecked
            )
            mDialog.show()
        }

        tv_goto_register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun getViewToAttach() = this

    override fun createPresenter() = LoginPresenter()

    override fun loginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        mDialog.dismiss()
    }

    override fun loginFailed(msg: String) {
        toast(msg)
        mDialog.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}