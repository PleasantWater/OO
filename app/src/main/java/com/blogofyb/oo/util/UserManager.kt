package com.blogofyb.oo.util

import com.blogofyb.oo.BaseApp
import com.blogofyb.oo.config.*
import com.blogofyb.oo.util.database.OOSQLiteOpenHelper
import com.blogofyb.oo.util.extensions.editor
import com.blogofyb.oo.util.extensions.sharedPreferences

/**
 * Create by yuanbing
 * on 2019/8/15
 */
object UserManager {
    fun isHaveUser() = BaseApp.context.sharedPreferences(XML_USER).getBoolean(
        KEY_IS_HAVE_USER_XML, false)

    fun saveUserInformation(account: String, password: String) {
        val database = OOSQLiteOpenHelper.writableDatabase
        val sql = "INSERT INTO $TABLE_USER VALUES('$account', '$password');"
        database.execSQL(sql)
        BaseApp.context.sharedPreferences(XML_USER).editor {
            putBoolean(KEY_IS_HAVE_USER_XML, true)
        }
    }

    fun logout(account: String) {
        val database = OOSQLiteOpenHelper.writableDatabase
        val sql = "DELETE FROM $TABLE_USER WHERE $FIELD_USER_NAME_USER='$account';"
        database.execSQL(sql)
        BaseApp.context.sharedPreferences(XML_USER).editor {
            putBoolean(KEY_IS_HAVE_USER_XML, false)
        }
        GlobalMessageManager.CONVERSATIONS.clear()
        GlobalMessageManager.mObservers.clear()
    }

    fun currentUserInfo(): Pair<String, String> {
        val database = OOSQLiteOpenHelper.writableDatabase
        val sql = "SELECT * FROM $TABLE_USER"
        val cursor = database.rawQuery(sql, null)
        var pair: Pair<String, String>? = null
        if (cursor.moveToNext()) {
            pair = Pair(cursor.getString(cursor.getColumnIndex(FIELD_USER_NAME_USER)),
                cursor.getString(cursor.getColumnIndex(FIELD_PASSWORD_USER)))
        }
        cursor.close()
        return pair ?: Pair("", "")
    }
}