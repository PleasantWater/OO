package com.blogofyb.oo.base.mvp

/**
 * Create by yuanbing
 * on 2019/8/1
 */
interface IBasePresenter<V: IBaseView, M: IBaseModel> {

    fun attachView(view: V)

    fun detachView()

    fun attachModel(): M

    fun onStart()

    fun onStop()
}