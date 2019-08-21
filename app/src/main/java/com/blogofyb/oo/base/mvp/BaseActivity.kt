package com.blogofyb.oo.base.mvp

import android.os.Bundle
import com.blogofyb.oo.base.BaseActivity

/**
 * Create by yuanbing
 * on 2019/8/1
 */
abstract class BaseActivity<V: IBaseView, P: IBasePresenter<V, M>, M: IBaseModel>: BaseActivity() {
    protected var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
        presenter?.attachView(getViewToAttach())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    abstract fun getViewToAttach(): V

    abstract fun createPresenter(): P
}