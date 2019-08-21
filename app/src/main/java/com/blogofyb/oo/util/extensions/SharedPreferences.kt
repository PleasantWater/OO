package com.blogofyb.oo.util.extensions

import android.content.Context
import android.content.SharedPreferences
import com.blogofyb.oo.config.DEFAULT_XML

/**
 * Create by yuanbing
 * on 2019/8/15
 */
val Context.defaultSharedPreferences get() = sharedPreferences(DEFAULT_XML)

fun Context.sharedPreferences(name: String): SharedPreferences = getSharedPreferences(name, Context.MODE_PRIVATE)
fun SharedPreferences.editor(editorBuilder: SharedPreferences.Editor.() -> Unit) = edit().apply(editorBuilder).apply()