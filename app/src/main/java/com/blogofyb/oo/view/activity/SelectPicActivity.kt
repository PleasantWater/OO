package com.blogofyb.oo.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.blogofyb.oo.R
import com.blogofyb.oo.base.BaseActivity
import com.blogofyb.oo.config.*
import com.blogofyb.oo.util.extensions.doPermissionAction
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_select_pic.*
import top.limuyang2.photolibrary.activity.LPhotoPickerActivity
import top.limuyang2.photolibrary.util.LPPImageType
import java.io.File
import java.io.IOException

/**
 * Create by yuanbing
 * on 8/24/19
 */
class SelectPicActivity : BaseActivity() {
    private lateinit var mPicUri: Uri
    private var mMaxRW = 0
    private var mMaxRH = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pic)

        initView()
    }

    private fun initView() {
        tv_cancel.setOnClickListener { finish() }
        val intentForPhotoPicker = LPhotoPickerActivity.IntentBuilder(this)
            .maxChooseCount(intent?.getIntExtra(KEY_PIC_COUNT, 1) ?: 1)
            .columnsNumber(4)
            .imageType(LPPImageType.ofAll())
            .pauseOnScroll(false)
            .isSingleChoose(false)
            .theme(R.style.AppTheme)
            .selectedPhotos(ArrayList())
            .build()

        Log.d("maxPicCount", (intent?.getIntExtra(KEY_PIC_COUNT, 1) ?: 1).toString())

        tv_album.setOnClickListener {
            doPermissionAction(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                doAfterGranted { startActivityForResult(intentForPhotoPicker, 0) }
            }
        }

        tv_camera.setOnClickListener {
            doPermissionAction(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ) {
                doAfterGranted {
                    val outputImage = File(externalCacheDir, "${System.currentTimeMillis()}.jpg")
                    try {
                        if (outputImage.exists()) outputImage.delete()
                        outputImage.createNewFile()
                    } catch (e: IOException) {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                    mPicUri = if (Build.VERSION.SDK_INT >= 24) {
                        FileProvider.getUriForFile(this@SelectPicActivity,
                            "com.blogofyb.oo.fileprovider", outputImage)
                    } else {
                        Uri.fromFile(outputImage)
                    }
                    val intent = Intent("android.media.action.IMAGE_CAPTURE")
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mPicUri)
                    startActivityForResult(intent, 1)
                }
            }
        }

        mMaxRH = intent?.getIntExtra(KEY_MAX_HEIGHT, 0) ?: 0
        mMaxRW = intent?.getIntExtra(KEY_MAX_WIDTH, 0) ?: 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = LPhotoPickerActivity.getSelectedPhotos(data)
                    if (result.size == 1) {
                        val outUri = Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}.jpg"))
                        val crop = UCrop.of(Uri.fromFile(File(result[0])), outUri)
                        if (mMaxRH != 0 && mMaxRW != 0) {
                            crop.withMaxResultSize(mMaxRW, mMaxRH)
                        }
                        crop.start(this)
                    } else {
                        val intent = Intent()
                        intent.putExtra(KEY_PIC_LIST, result)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val outUri = Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}.jpg"))
                    val crop = UCrop.of(mPicUri, outUri)
                    if (mMaxRH != 0 && mMaxRW != 0) {
                        crop.withMaxResultSize(mMaxRW, mMaxRH)
                    }
                    crop.start(this)
                }
            }
            UCrop.REQUEST_CROP -> {
                data?.let {
                    val resultUri = UCrop.getOutput(data)
                    val intentResult = Intent()
                    if (intent?.getIntExtra(KEY_PIC_COUNT, 1) == 1) {
                        intentResult.putExtra(KEY_PIC_PATH, resultUri?.path)
                    } else {
                        val picList: ArrayList<String> = ArrayList()
                        picList.add(resultUri?.path ?: "")
                        intentResult.putStringArrayListExtra(KEY_PIC_LIST, picList)
                    }
                    setResult(Activity.RESULT_OK, intentResult)
                    finish()
                    Log.d("UCrop.REQUEST_CROP", resultUri?.path ?: "no such file")
                }
            }
        }
    }
}