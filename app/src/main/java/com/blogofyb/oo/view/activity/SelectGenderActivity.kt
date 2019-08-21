package com.blogofyb.oo.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.blogofyb.oo.R
import com.blogofyb.oo.base.BaseActivity
import com.blogofyb.oo.config.KEY_STRING_DATA
import kotlinx.android.synthetic.main.activity_select_gender.*

/**
 * Create by yuanbing
 * on 2019/8/18
 */
class SelectGenderActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_gender)

        initView()
    }

    private fun initView() {
        tv_male.setOnClickListener {
            val intent = Intent()
            intent.putExtra(KEY_STRING_DATA, tv_male.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        tv_female.setOnClickListener {
            val intent = Intent()
            intent.putExtra(KEY_STRING_DATA, tv_female.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}