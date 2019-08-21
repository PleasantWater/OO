package com.blogofyb.oo.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.interfaces.model.IActivityEditUserInformationModel
import com.blogofyb.oo.interfaces.presenter.IActivityEditUserInformationPresenter
import com.blogofyb.oo.interfaces.view.IActivityEditUserInformationView
import com.blogofyb.oo.presenter.ActivityEditUserInformationPresenter
import kotlinx.android.synthetic.main.activity_edit_user_information.*
import kotlinx.android.synthetic.main.activity_user_information.tv_nickname
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Create by yuanbing
 * on 2019/8/17
 */
class EditUserInformationActivity :
        BaseActivity<IActivityEditUserInformationView, IActivityEditUserInformationPresenter,
                IActivityEditUserInformationModel>(), IActivityEditUserInformationView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_information)

        initToolbar()
        initView()
    }

    private fun initView() {
        val userInformation = intent.getSerializableExtra(KEY_USER_INFORMATION) as UserBean
        tv_nickname.text = userInformation.nickname
        ll_nickname.setOnClickListener {
            val intent = Intent(this@EditUserInformationActivity,
                InputUserInformationActivity::class.java)
            intent.putExtra(KEY_INTENT, "昵称")
            intent.putExtra(KEY_MAX_LENGTH, 8)
            intent.putExtra(KEY_STRING_DATA, tv_nickname.text.toString())
            startActivityForResult(intent, 0)
        }

        tv_gender.text = userInformation.gender
        ll_gender.setOnClickListener {
            val intent = Intent(this@EditUserInformationActivity,
                SelectGenderActivity::class.java)
            intent.putExtra(KEY_STRING_DATA, tv_gender.text.toString())
            intent.putExtra(KEY_INTENT, "性别")
            startActivityForResult(intent, 1)
        }

        tv_signature.text = userInformation.signature
        ll_signature.setOnClickListener {
            val intent = Intent(this@EditUserInformationActivity,
                InputUserInformationActivity::class.java)
            intent.putExtra(KEY_INTENT, "签名")
            intent.putExtra(KEY_MAX_LENGTH, 20)
            intent.putExtra(KEY_STRING_DATA, tv_signature.text.toString())
            startActivityForResult(intent, 2)
        }

        tv_hometown.text = userInformation.hometown
        ll_hometown.setOnClickListener {
            val intent = Intent(this@EditUserInformationActivity,
                InputUserInformationActivity::class.java)
            intent.putExtra(KEY_INTENT, "家乡")
            intent.putExtra(KEY_MAX_LENGTH, 8)
            intent.putExtra(KEY_STRING_DATA, tv_hometown.text.toString())
            startActivityForResult(intent, 3)
        }

        tv_school.text = userInformation.school
        ll_school.setOnClickListener {
            val intent = Intent(this@EditUserInformationActivity,
                InputUserInformationActivity::class.java)
            intent.putExtra(KEY_INTENT, "学校")
            intent.putExtra(KEY_MAX_LENGTH, 10)
            intent.putExtra(KEY_STRING_DATA, tv_school.text.toString())
            startActivityForResult(intent, 4)
        }

        btn_save_user_information.setOnClickListener {
            val user = UserBean(
                userInformation.username,
                tv_nickname.text.toString(),
                tv_signature.text.toString(),
                userInformation.header,
                tv_gender.text.toString(),
                tv_hometown.text.toString(),
                tv_school.text.toString(),
                userInformation.bg
            )
            presenter?.saveChange(user)
            finish()
        }
    }

    private fun initToolbar() {
        toolbar.title = "修改资料"
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun getViewToAttach() = this

    override fun createPresenter() = ActivityEditUserInformationPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK) {
                    tv_nickname.text = data.getStringExtra(KEY_STRING_DATA)
                }
            }
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    tv_gender.text = data.getStringExtra(KEY_STRING_DATA)
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    tv_signature.text = data.getStringExtra(KEY_STRING_DATA)
                }
            }
            3 -> {
                if (resultCode == Activity.RESULT_OK) {
                    tv_hometown.text = data.getStringExtra(KEY_STRING_DATA)
                }
            }
            4 -> {
                if (resultCode == Activity.RESULT_OK) {
                    tv_school.text = data.getStringExtra(KEY_STRING_DATA)
                }
            }
        }
    }
}