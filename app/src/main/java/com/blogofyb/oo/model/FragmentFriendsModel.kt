package com.blogofyb.oo.model

import android.annotation.SuppressLint
import com.avos.avoscloud.AVException
//import cn.leancloud.AVUser
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.FindCallback
import com.avos.avoscloud.GetCallback
import com.avos.avoscloud.callback.AVFriendshipCallback
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.interfaces.model.IFragmentFriendsModel
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Create by yuanbing
 * on 2019/8/18
 */
class FragmentFriendsModel : BaseModel(), IFragmentFriendsModel {
    @SuppressLint("CheckResult")
    override fun getFriends(callback: (List<UserBean>) -> Unit) {
        val user = AVUser.getCurrentUser() ?: return
        user.followerQuery(AVUser::class.java).include("follower").findInBackground(
            object : FindCallback<AVUser>() {
                override fun done(avObjects: MutableList<AVUser>?, avException: AVException?) {
                    Observable.just(avObjects)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map { followers ->
                            followers.map { follower ->
                                UserBean(
                                    follower.username,
                                    follower.getString(KEY_NICKNAME) ?: "",
                                    follower.getString(KEY_SIGNATURE) ?: "",
                                    follower.getString(KEY_USER_HEADER) ?: "",
                                    follower.getString(KEY_GENDER) ?: "",
                                    follower.getString(KEY_HOMETOWN) ?: "",
                                    follower.getString(KEY_SCHOOL) ?: "",
                                    follower.getString(KEY_BG) ?: ""
                                )
                            }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .safeSubscribeBy { callback(it) }
                }
            }
        )
    }
}