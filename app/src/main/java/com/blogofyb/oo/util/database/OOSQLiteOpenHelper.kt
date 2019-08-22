package com.blogofyb.oo.util.database

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.blogofyb.oo.BaseApp
import com.blogofyb.oo.config.DATABASE
import com.blogofyb.oo.config.FIELD_PASSWORD_USER
import com.blogofyb.oo.config.FIELD_USER_NAME_USER
import com.blogofyb.oo.config.TABLE_USER

/**
 * Create by yuanbing
 * on 2019/8/15
 */
object OOSQLiteOpenHelper : SQLiteOpenHelper(
    BaseApp.context,
    DATABASE,
    null,
    1
) {
    override fun onCreate(p0: SQLiteDatabase?) {
        val sql = "CREATE TABLE $TABLE_USER ($FIELD_USER_NAME_USER TEXT NOT NULL, " +
                "$FIELD_PASSWORD_USER TEXT NOT NULL);"
        p0?.execSQL(sql)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}