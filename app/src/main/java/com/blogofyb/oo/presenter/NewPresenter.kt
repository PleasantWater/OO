package com.blogofyb.oo.presenter

import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.bean.NewBean
import com.blogofyb.oo.interfaces.model.INewModel
import com.blogofyb.oo.interfaces.presenter.INewPresenter
import com.blogofyb.oo.interfaces.view.INewView
import com.blogofyb.oo.model.NewModel

/**
 * Create by yuanbing
 * on 8/23/19
 */
class NewPresenter : BasePresenter<INewView, INewModel>(), INewPresenter {
    override fun attachModel() = NewModel()

    override fun getNew(username: String) {
        model?.getNew(
            username,
            object : INewModel.GetCallback {
                override fun getNewSuccess(new: List<NewBean>) {
                    view?.showNew(new)
                }

                override fun getNewFailed() {
                    view?.getNewFailed()
                }
            }
        )
    }

    override fun sendNew(new: NewBean) {
        model?.postNew(
            new,
            object : INewModel.SendCallback {
                override fun sendNewSuccess(new: NewBean) {
                    view?.sendNewSuccess(new)
                }

                override fun sendNewFailed() {
                    view?.sendNewFailed()
                }
            }
        )
    }
}