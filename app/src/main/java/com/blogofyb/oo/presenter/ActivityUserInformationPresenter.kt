package com.blogofyb.oo.presenter

//import cn.leancloud.AVUser
import com.avos.avoscloud.AVUser
import com.blogofyb.oo.base.mvp.BasePresenter
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.interfaces.model.IActivityUserInformationModel
import com.blogofyb.oo.interfaces.presenter.IActivityUserInformationPresenter
import com.blogofyb.oo.interfaces.view.IActivityUserInformationView
import com.blogofyb.oo.model.ActivityUserInformationModel

/**
 * Create by yuanbing
 * on 2019/8/17
 */
class ActivityUserInformationPresenter :
        BasePresenter<IActivityUserInformationView, IActivityUserInformationModel>(),
        IActivityUserInformationPresenter {
    override fun updateUserHead(path: String) {
        model?.updateUserHead(path) {
            model?.getUserInformation(
                AVUser.getCurrentUser().username,
                object : IActivityUserInformationModel.Callback {
                    override fun getUserInformationSuccess(userInformation: UserBean) {
                        view?.showUserInformation(userInformation)
                    }

                    override fun getUserInformationFailed() {

                    }
                }
            )
        }
    }

    override fun updateUserBg(path: String) {
        model?.updateUserBg(path) {
            model?.getUserInformation(
                AVUser.getCurrentUser().username,
                object : IActivityUserInformationModel.Callback {
                    override fun getUserInformationSuccess(userInformation: UserBean) {
                        view?.showUserInformation(userInformation)
                    }

                    override fun getUserInformationFailed() {

                    }
                }
            )
        }
    }

    override fun attachModel() = ActivityUserInformationModel()

    override fun getUserInformation(username: String) {
        model?.getUserInformation(
            username,
            object : IActivityUserInformationModel.Callback {
                override fun getUserInformationSuccess(userInformation: UserBean) {
                    view?.showUserInformation(userInformation)
                }

                override fun getUserInformationFailed() {

                }
            }
        )
    }

    override fun addFriend(username: String) {
        model?.addFriend(username) { view?.addFriendSuccess() }
    }
}