package com.blogofyb.oo.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.avos.avoscloud.AVUser
import com.blogofyb.oo.R
import com.blogofyb.oo.base.BaseActivity
import com.blogofyb.oo.bean.NewBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.util.extensions.setImageFromUrl
import com.blogofyb.oo.util.extensions.toast
import com.blogofyb.oo.view.adapter.NewPicRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_add_new.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Create by yuanbing
 * on 8/24/19
 */
class AddNewActivity : BaseActivity() {
    private lateinit var mAdapter: NewPicRecyclerViewAdapter
    private var mPic: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new)

        initToolbar()
        initView()
    }

    private fun initView() {
        val user = AVUser.getCurrentUser()
        iv_user_head.setImageFromUrl(user.getString(KEY_USER_HEADER))
        tv_nickname.text = user.getString(KEY_NICKNAME)
        iv_add_new_add_pic.setOnClickListener {
            val intent = Intent(this@AddNewActivity, SelectPicActivity::class.java)
            intent.putExtra(KEY_PIC_COUNT, 9)
            startActivityForResult(intent, 0)
        }
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.dock_new)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.inflateMenu(R.menu.add_new_tool_menu)
        toolbar.menu.getItem(0)?.setOnMenuItemClickListener {
            if (et_add_new_content.text.isBlank()) {
                toast("输入不可以为空")
            } else {
                val intent = Intent()
                val user = AVUser.getCurrentUser()
                intent.putExtra(KEY_NEW, NewBean(
                    user.getString(KEY_USER_HEADER),
                    user.getString(KEY_NICKNAME),
                    "",
                    et_add_new_content.text.toString(),
                    mPic,
                    user.username
                ))
                setResult(RESULT_OK, intent)
                finish()
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mPic = data.getStringArrayListExtra(KEY_PIC_LIST) ?: listOf()
                    mAdapter = NewPicRecyclerViewAdapter(mPic)
                    rv_add_new_pic.adapter = mAdapter
                    rv_add_new_pic.layoutManager = GridLayoutManager(
                        this,
                        if (mAdapter.itemCount > 2) 3 else mAdapter.itemCount
                    )
                }
            }
        }
    }
}