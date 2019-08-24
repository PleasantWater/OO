package com.blogofyb.oo.model

import android.annotation.SuppressLint
import com.avos.avoscloud.*
import com.blogofyb.oo.base.mvp.BaseModel
import com.blogofyb.oo.bean.NewBean
import com.blogofyb.oo.interfaces.model.INewModel
import com.blogofyb.oo.util.GlobalMessageManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by yuanbing
 * on 8/23/19
 */
class NewModel : BaseModel(), INewModel {
    override fun getMore(username: String, callback: INewModel.GetCallback) {
        AVStatus.inboxQuery(AVUser.getCurrentUser(), AVStatus.INBOX_TYPE.TIMELINE.toString())
//            .limit(10)
            .findInBackground(
                object : FindCallback<AVStatus>() {
                    @SuppressLint("CheckResult")
                    override fun done(
                        avObjects: MutableList<AVStatus>?,
                        avException: AVException?
                    ) {
                        if (avException != null) {
                            callback.getNewFailed()
                        } else {
                            Observable.just(avObjects)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .map { status ->
                                    status.map { new ->
                                        NewBean(
                                            new.data["header"].toString(),
                                            new.data["nickname"].toString(),
                                            GlobalMessageManager.parseTimeStamp(new.createdAt.time),
                                            new.data["content"].toString(),
                                            new.data["pic"] as List<String>,
                                            new.data["username"].toString()
                                        )
                                    }
                                }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    callback.getNewSuccess(it)
                                }, {
                                    callback.getNewFailed()
                                })
                        }
                    }
                }
            )
    }

    override fun getNew(username: String, callback: INewModel.GetCallback) {
        getMore(username, callback)
    }

    @SuppressLint("CheckResult")
    override fun postNew(new: NewBean, callback: INewModel.SendCallback) {
        val status = AVStatus()
        val pic: ArrayList<String> = ArrayList()
        Observable.fromIterable(new.pic)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .concatMap { picPath ->
                val file = AVFile.withAbsoluteLocalPath("${System.currentTimeMillis()}.png", picPath)
                file.save()
                Observable.just(file.url)
            }
            .observeOn(Schedulers.io())
            .subscribe({ picUrl ->
                pic.add(picUrl) }, {}, {
                status.data = mapOf(
                    "header" to new.header,
                    "nickname" to new.nickname,
                    "time" to new.time,
                    "content" to new.content,
                    "pic" to pic,
                    "username" to new.username
                )
                status.sendInBackground(
                    object : SaveCallback() {
                        override fun done(e: AVException?) {
                            if (e != null) {
                                callback.sendNewFailed()
                            } else {
                                callback.sendNewSuccess(new)
                            }
                        }
                    }
                )
            })
    }
}