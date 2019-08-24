package com.blogofyb.oo.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.avos.avoscloud.AVUser
//import cn.leancloud.AVUser
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.interfaces.model.IActivityUserInformationModel
import com.blogofyb.oo.interfaces.presenter.IActivityUserInformationPresenter
import com.blogofyb.oo.interfaces.view.IActivityUserInformationView
import com.blogofyb.oo.presenter.ActivityUserInformationPresenter
import com.blogofyb.oo.util.extensions.doPermissionAction
import com.blogofyb.oo.util.extensions.gone
import com.blogofyb.oo.util.extensions.setImageFromUrl
import com.blogofyb.oo.util.extensions.visible
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_user_information.*
import kotlinx.android.synthetic.main.toolbar.*
import top.limuyang2.photolibrary.activity.LPhotoPickerActivity
import top.limuyang2.photolibrary.util.LPPImageType
import java.io.File

/**
 * Create by yuanbing
 * on 2019/8/17
 */
class UserInformationActivity :
        BaseActivity<IActivityUserInformationView, IActivityUserInformationPresenter,
                IActivityUserInformationModel>(), IActivityUserInformationView {
    private lateinit var mUsername: String
    private lateinit var mUserInformation: UserBean
    private var mIsHead = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_information)
        mUsername = intent?.getStringExtra(KEY_USERNAME) ?: ""

        initToolbar()
        btn_user_information.gone()
        tv_user_signature.gone()
    }

    override fun onResume() {
        super.onResume()
        presenter?.getUserInformation(mUsername)
    }

    private fun initView() {
        iv_user_bg.setImageFromUrl(mUserInformation.bg)
        tv_nickname.text = mUserInformation.nickname
        tv_user_name.text = mUserInformation.username
        tv_user_signature.text = mUserInformation.signature
        tv_user_gender.text = mUserInformation.gender
        tv_user_school.text = mUserInformation.school
        tv_user_hometown.text = mUserInformation.hometown
        iv_user_head.setImageFromUrl(mUserInformation.header)

        toolbar.setBackgroundColor(Color.TRANSPARENT)
        app_bar_layout.setBackgroundColor(Color.TRANSPARENT)

        btn_user_information.visible()
        tv_user_signature.visible()
        when (mUserInformation.userType) {
            0 -> {
                btn_user_information.text = "修改资料"
                btn_user_information.setOnClickListener {
                    val intent = Intent(this@UserInformationActivity,
                        EditUserInformationActivity::class.java)
                    intent.putExtra(KEY_USER_INFORMATION, mUserInformation)
                    startActivity(intent)
                }

                iv_user_head.setOnClickListener {
                    val intent = Intent(this@UserInformationActivity, SelectPicActivity::class.java)
                    intent.putExtra(KEY_MAX_WIDTH, iv_user_head.width)
                    intent.putExtra(KEY_MAX_HEIGHT, iv_user_head.height)
                    startActivityForResult(intent, 0)
                }

                iv_user_bg.setOnClickListener {
                    val intent = Intent(this@UserInformationActivity, SelectPicActivity::class.java)
                    intent.putExtra(KEY_MAX_WIDTH, iv_user_bg.width)
                    intent.putExtra(KEY_MAX_HEIGHT, iv_user_bg.height)
                    startActivityForResult(intent, 1)
                }
            }
            1 -> {
                btn_user_information.text = "加好友"
                btn_user_information.setOnClickListener{
                    presenter?.addFriend(mUsername)
                }
            }
            -1 -> {
                btn_user_information.text = "发消息"
                btn_user_information.setOnClickListener {
                    val intent = Intent(this@UserInformationActivity, ChatActivity::class.java)
                    intent.putExtra(KEY_USERNAME, mUsername)
                    intent.putExtra(KEY_NICKNAME, mUserInformation.nickname)
                    intent.putExtra(KEY_USER_HEADER, mUserInformation.header)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun getViewToAttach() = this

    override fun createPresenter() = ActivityUserInformationPresenter()

    override fun showUserInformation(userInformation: UserBean) {
        mUserInformation = userInformation
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val path = data.getStringExtra(KEY_PIC_PATH)
                    if (!path.isNullOrBlank()) {
                        presenter?.updateUserHead(path)
                    }
                }
            }
            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val path = data.getStringExtra(KEY_PIC_PATH)
                    if (!path.isNullOrBlank()) {
                        presenter?.updateUserBg(path)
                    }
                }
            }
        }
    }

    override fun addFriendSuccess() {
        btn_user_information.text = "发消息"
        btn_user_information.setOnClickListener {
            val intent = Intent(this@UserInformationActivity, ChatActivity::class.java)
            intent.putExtra(KEY_USERNAME, mUsername)
            intent.putExtra(KEY_NICKNAME, mUserInformation.nickname)
            intent.putExtra(KEY_USER_HEADER, mUserInformation.header)
            startActivity(intent)
            finish()
        }
    }
}