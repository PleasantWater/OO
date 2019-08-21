package com.blogofyb.oo.util.extensions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

/**
 * Create by yuanbing
 * on 2019/8/20
 */
class PermissionActionBuilder {
    var doAfterGranted: () -> Unit = {}
        private set
    var doAfterRefused: (() -> Unit)? = null
        private set
    var reason: String? = null


    fun doAfterGranted(action: () -> Unit) {
        doAfterGranted = action
    }

    fun doAfterRefused(action: () -> Unit) {
        doAfterRefused = action
    }
}

private fun requestPermission(rxPermissions: RxPermissions,
                              builder: PermissionActionBuilder,
                              vararg permissionsToRequest: String) =
    rxPermissions.request(*permissionsToRequest).subscribe { granted ->
        if (granted) {
            builder.doAfterGranted()
        } else {
            builder.doAfterRefused?.invoke()
        }
    }

private fun performRequestPermission(context: Context,
                                     rxPermissions: RxPermissions,
                                     vararg permissionsRequired: String,
                                     actionBuilder: PermissionActionBuilder.() -> Unit) {
    val builder = PermissionActionBuilder().apply(actionBuilder)
    val permissionsToRequest = permissionsRequired.filterNot { rxPermissions.isGranted(it) }

    when {
        permissionsToRequest.isEmpty() -> builder.doAfterGranted.invoke()
        builder.reason != null -> context.alert(builder.reason!!) {
            yesButton {
                requestPermission(rxPermissions, builder, *permissionsToRequest.toTypedArray())
            }
        }.show()
        else -> requestPermission(rxPermissions, builder, *permissionsToRequest.toTypedArray())
    }
}

fun AppCompatActivity.doPermissionAction(vararg permissionsRequired: String,
                                         actionBuilder: PermissionActionBuilder.() -> Unit) {
    performRequestPermission(this, RxPermissions(this), *permissionsRequired, actionBuilder = actionBuilder)
}

fun androidx.fragment.app.Fragment.doPermissionAction(vararg permissionsRequired: String,
                                                      actionBuilder: PermissionActionBuilder.() -> Unit) {
    performRequestPermission(activity!!, RxPermissions(this), *permissionsRequired, actionBuilder = actionBuilder)
}

fun AppCompatActivity.isPermissionGranted(permissions: String) = RxPermissions(this).isGranted(permissions)
fun androidx.fragment.app.Fragment.isPermissionGranted(permissions: String) = RxPermissions(this).isGranted(permissions)
