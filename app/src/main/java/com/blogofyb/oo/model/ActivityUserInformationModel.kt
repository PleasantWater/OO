package com.blogofyb.oo.model

//import cn.leancloud.AVFile
//import cn.leancloud.AVUser
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.SaveCallback
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.interfaces.model.IActivityUserInformationModel
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Create by yuanbing
 * on 2019/8/17
 */
class ActivityUserInformationModel : BaseModel(), IActivityUserInformationModel {
    override fun updateUserHead(path: String, callback: () -> Unit) {
        val head = AVFile.withAbsoluteLocalPath(path.split("/").last(), path)
        head.saveInBackground(
            object : SaveCallback() {
                override fun done(e: AVException?) {
                    val user = AVUser.getCurrentUser()
                    user.put(KEY_USER_HEADER, head.url)
                    user.save()
                    callback()
                }
            }
        )
    }

    override fun updateUserBg(path: String, callback: () -> Unit) {
        val bg = AVFile.withAbsoluteLocalPath(path.split("/").last(), path)
        bg.saveInBackground(
            object : SaveCallback() {
                override fun done(e: AVException?) {
                    val user = AVUser.getCurrentUser()
                    user.put(KEY_BG, bg.url)
                    user.save()
                    callback()
                }
            }
        )
    }

    override fun getUserInformation(username: String, callback: (UserBean) -> Unit) {
        Observable.create<AVUser> {
            val user = AVUser.getQuery()
            it.onNext(user.whereContains(KEY_USERNAME, username).first)
            it.onComplete()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    val userInformation = UserBean(
                            it.username,
                            it.getString(KEY_NICKNAME) ?: "",
                            it.getString(KEY_SIGNATURE) ?: "",
                            it.getString(KEY_USER_HEADER) ?: "",
                            it.getString(KEY_GENDER) ?: "",
                            it.getString(KEY_HOMETOWN) ?: "",
                            it.getString(KEY_SCHOOL) ?: "",
                            it.getString(KEY_BG) ?: ""
                    )
                    val currentUser = AVUser.getCurrentUser()
                    if (currentUser.username == username) {
                        currentUser.put(KEY_NICKNAME, it.getString(KEY_NICKNAME))
                        currentUser.put(KEY_SIGNATURE, it.getString(KEY_SIGNATURE))
                        currentUser.put(KEY_USER_HEADER, it.getString(KEY_USER_HEADER))
                        currentUser.put(KEY_GENDER, it.getString(KEY_GENDER))
                        currentUser.put(KEY_HOMETOWN, it.getString(KEY_HOMETOWN))
                        currentUser.put(KEY_SCHOOL, it.getString(KEY_SCHOOL))
                        currentUser.put(KEY_BG, it.getString(KEY_BG))
                    }
                    userInformation
                }
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribeBy { callback(it) }
    }
}