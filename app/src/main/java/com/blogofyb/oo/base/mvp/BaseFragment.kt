package com.blogofyb.oo.base.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.blogofyb.oo.base.BaseFragment

/**
 * Create by yuanbing
 * on 2019/8/1
 */
abstract class BaseFragment<V: IBaseView, P: IBasePresenter<V, M>, M: IBaseModel>: BaseFragment() {
    protected var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
        presenter?.attachView(getViewToAttach())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutRes(), container, false)
        onCreateView(view, savedInstanceState)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    abstract fun onCreateView(view: View, savedInstanceState: Bundle?)

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun getViewToAttach(): V

    abstract fun createPresenter(): P
}