package com.blogofyb.oo.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import com.blogofyb.oo.R
import com.blogofyb.oo.base.BaseActivity
import com.blogofyb.oo.config.KEY_INTENT
import com.blogofyb.oo.config.KEY_MAX_LENGTH
import com.blogofyb.oo.config.KEY_STRING_DATA
import kotlinx.android.synthetic.main.activity_input_user_information.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Create by yuanbing
 * on 2019/8/18
 */
class InputUserInformationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_user_information)

        initToolbar()
        initView()
    }

    private fun initView() {
        et_input_user_information.setText(intent?.getStringExtra(KEY_STRING_DATA))
        et_input_user_information.filters = arrayOf(InputFilter.LengthFilter(intent?.getIntExtra(
            KEY_MAX_LENGTH, 0) ?: 0))
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = intent?.getStringExtra(KEY_INTENT)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(KEY_STRING_DATA, et_input_user_information.text.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}