package com.blogofyb.oo.model

import cn.leancloud.AVUser
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.interfaces.model.IActivityEditUserInformationModel
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Create by yuanbing
 * on 2019/8/17
 */
class ActivityEditUserInformationModel : BaseModel(), IActivityEditUserInformationModel {
    override fun updateUserInformation(userInformation: UserBean) {
        val user = AVUser.currentUser() ?: return
        user.put(KEY_NICKNAME, userInformation.nickname)
        user.put(KEY_GENDER, userInformation.gender)
        user.put(KEY_SCHOOL, userInformation.school)
        user.put(KEY_HOMETOWN, userInformation.hometown)
        user.put(KEY_SIGNATURE, userInformation.signature)
        user.saveInBackground()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .safeSubscribeBy {  }
    }
}