package com.blogofyb.oo.model

import android.util.Log
import com.avos.avoscloud.*
//import cn.leancloud.AVException
//import cn.leancloud.AVQuery
//import cn.leancloud.AVUser
//import cn.leancloud.callback.CloudQueryCallback
//import cn.leancloud.query.AVCloudQueryResult
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.UserBean
import com.blogofyb.oo.config.*
import com.blogofyb.oo.interfaces.model.IAddFriendModel
import com.blogofyb.oo.util.extensions.safeSubscribeBy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by yuanbing
 * on 2019/8/21
 */
@Suppress("UNCHECKED_CAST")
class AddFriendsModel : BaseModel(), IAddFriendModel {
    override fun searchUser(usernameOrNickname: String, callback: (List<UserBean>) -> Unit) {
        val cql = "SELECT * FROM _User WHERE $KEY_USERNAME='$usernameOrNickname' OR $KEY_NICKNAME LIKE '%$usernameOrNickname%'"
        AVQuery.doCloudQueryInBackground(
            cql,
            object : CloudQueryCallback<AVCloudQueryResult>() {
                override fun done(result: AVCloudQueryResult?, avException: AVException?) {
                    if (avException != null) {
                        Log.d("searchUser", avException.message ?: "Exception")
                        return
                    } else {
                        if (result == null) return
                        val searchResult = result.results as List<AVUser>
                        Observable.create<List<UserBean>> {
                            it.onNext(searchResult.map { user ->
                                UserBean(
                                    user.username,
                                    user.getString(KEY_NICKNAME) ?: "",
                                    user.getString(KEY_SIGNATURE) ?: "",
                                    user.getString(KEY_USER_HEADER) ?: "",
                                    user.getString(KEY_GENDER) ?: "",
                                    user.getString(KEY_HOMETOWN) ?: "",
                                    user.getString(KEY_SCHOOL) ?: "",
                                    user.getString(KEY_BG) ?: ""
                                )
                            })
                            it.onComplete()
                        }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .safeSubscribeBy { callback(it) }
                    }
                }
            },
            AVUser::class.java
        )
    }
}