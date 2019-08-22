package com.blogofyb.oo.model

import android.annotation.SuppressLint
import com.avos.avoscloud.AVException
//import cn.leancloud.AVUser
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.GetCallback
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.interfaces.model.IFragmentFriendsModel
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
        val friends: Vector<UserBean> = Vector()
        val friendsList = user.getList(KEY_FRIENDS) ?: return
        Observable.fromIterable(friendsList as List<String>)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .concatMap {
                val query = AVUser.getQuery()
                Observable.just(query.whereContains(KEY_USERNAME, it).first)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
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
                friends.add(userInformation)
            }, {}, {
                callback(friends)
            })
    }
}