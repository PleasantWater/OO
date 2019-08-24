package com.blogofyb.oo.model

//import cn.leancloud.AVFile
//import cn.leancloud.AVUser
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.interfaces.model.IActivityUserInformationModel
import com.blogofyb.oo.util.GlobalMessageManager
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import com.blogofyb.oo.util.extensions.sendMessageByCustom
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
                    user.saveInBackground(
                        object : SaveCallback() {
                            override fun done(e: AVException?) {
                                if (e == null) {
                                    callback()
                                }
                            }
                        }
                    )
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
                    user.saveInBackground(
                        object : SaveCallback() {
                            override fun done(e: AVException?) {
                                if (e == null) {
                                    callback()
                                }
                            }
                        }
                    )
                }
            }
        )
    }

    override fun getUserInformation(username: String, callback: IActivityUserInformationModel.Callback) {
        AVUser.getQuery().whereEqualTo(KEY_USERNAME, username).getFirstInBackground(
            object : GetCallback<AVUser>() {
                override fun done(it: AVUser?, e: AVException?) {
                    if (e != null || it == null) {
                        callback.getUserInformationFailed()
                    } else {
                        val currentUser = AVUser.getCurrentUser()
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
                        if (currentUser.username == username) {
                            currentUser.put(KEY_NICKNAME, it.getString(KEY_NICKNAME))
                            currentUser.put(KEY_SIGNATURE, it.getString(KEY_SIGNATURE))
                            currentUser.put(KEY_USER_HEADER, it.getString(KEY_USER_HEADER))
                            currentUser.put(KEY_GENDER, it.getString(KEY_GENDER))
                            currentUser.put(KEY_HOMETOWN, it.getString(KEY_HOMETOWN))
                            currentUser.put(KEY_SCHOOL, it.getString(KEY_SCHOOL))
                            currentUser.put(KEY_BG, it.getString(KEY_BG))
                            callback.getUserInformationSuccess(userInformation)
                            return
                        }
                        currentUser.followerQuery(AVUser::class.java)
                            .whereEqualTo("follower", it)
                            .getFirstInBackground(
                                object : GetCallback<AVUser>() {
                                    override fun done(`object`: AVUser?, e: AVException?) {
                                        if (e != null) {
                                            callback.getUserInformationFailed()
                                        } else {
                                            userInformation.userType = if (`object` == null) 1 else  -1
                                            callback.getUserInformationSuccess(userInformation)
                                        }
                                    }
                                }
                            )
                    }
                }
            }
        )
    }

    override fun addFriend(username: String, callback: () -> Unit) {
        Observable.just(username)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map {
                // 不做好友请求通知了，直接强行加好友
                val currentUser = AVUser.getCurrentUser()
                val friendUser = AVUser.getQuery().whereEqualTo(KEY_USERNAME, username).first

                currentUser.followInBackground(
                    friendUser.objectId,
                    object : FollowCallback<AVUser>() {
                        override fun done(`object`: AVUser?, e: AVException?) {
                            if (e != null) return
                            callback()
                        }
                    }
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}