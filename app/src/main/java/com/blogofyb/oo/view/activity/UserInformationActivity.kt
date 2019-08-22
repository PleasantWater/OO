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
import com.blogofyb.oo.config.KEY_NICKNAME
import com.blogofyb.oo.config.KEY_USERNAME
import com.blogofyb.oo.config.KEY_USER_HEADER
import com.blogofyb.oo.config.KEY_USER_INFORMATION
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
        initView()
    }

    override fun onResume() {
        super.onResume()
        presenter?.getUserInformation(mUsername)
    }

    private fun initView() {
        btn_user_information.gone()
        tv_user_signature.gone()

        val username = AVUser.getCurrentUser()?.username
        if (mUsername == username) {
            btn_user_information.text = "修改资料"
            btn_user_information.setOnClickListener {
                val intent = Intent(this@UserInformationActivity, EditUserInformationActivity::class.java)
                intent.putExtra(KEY_USER_INFORMATION, mUserInformation)
                startActivity(intent)
            }

            val intent = LPhotoPickerActivity.IntentBuilder(this@UserInformationActivity)
                .maxChooseCount(1)
                .columnsNumber(4)
                .imageType(LPPImageType.ofAll())
                .pauseOnScroll(false)
                .isSingleChoose(true)
                .theme(R.style.AppTheme)
                .selectedPhotos(ArrayList())
                .build()

            iv_user_head.setOnClickListener {
                mIsHead = true
                doPermissionAction(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    doAfterGranted { startActivityForResult(intent, 0) }
                }
            }

            iv_user_bg.setOnClickListener {
                mIsHead = false
                doPermissionAction(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    doAfterGranted { startActivityForResult(intent, 1) }
                }
            }
        } else {
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

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun getViewToAttach() = this

    override fun createPresenter() = ActivityUserInformationPresenter()

    override fun showUserInformation(userInformation: UserBean) {
        mUserInformation = userInformation
        iv_user_bg.setImageFromUrl(userInformation.bg)
        tv_nickname.text =
                if (userInformation.nickname.isBlank()) userInformation.username else userInformation.nickname
        tv_user_name.text = userInformation.username
        tv_user_signature.text = userInformation.signature
        tv_user_gender.text = userInformation.gender
        tv_user_school.text = userInformation.school
        tv_user_hometown.text = userInformation.hometown
        iv_user_head.setImageFromUrl(userInformation.header)

        toolbar.setBackgroundColor(Color.TRANSPARENT)
        app_bar_layout.setBackgroundColor(Color.TRANSPARENT)

        btn_user_information.visible()
        tv_user_signature.visible()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = LPhotoPickerActivity.getSelectedPhotos(data)
                    val outUri = Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}.jpg"))
                    UCrop.of(Uri.fromFile(File(result[0])), outUri)
                        .withAspectRatio(1f, 1f)
                        .withMaxResultSize(iv_user_head.width, iv_user_head.height)
                        .start(this)
                }
            }
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = LPhotoPickerActivity.getSelectedPhotos(data)
                    val outUri = Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}.jpg"))
                    UCrop.of(Uri.fromFile(File(result[0])), outUri)
                        .withAspectRatio(iv_user_bg.width.toFloat(), iv_user_bg.height.toFloat())
                        .withMaxResultSize(iv_user_bg.width, iv_user_bg.height)
                        .start(this)
                }
            }
            UCrop.REQUEST_CROP -> {
                data?.let {
                    val resultUri = UCrop.getOutput(data)
                    if (mIsHead) {
                        presenter?.updateUserHead(resultUri?.path ?: "")
                    } else {
                        presenter?.updateUserBg(resultUri?.path ?: "")
                    }
                    Log.d("UCrop.REQUEST_CROP", resultUri?.path ?: "no file")
                }
            }
        }
    }
}