package com.blogofyb.oo.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil
import cn.dreamtobe.kpswitch.util.KeyboardUtil
import cn.leancloud.AVUser
import cn.leancloud.im.v2.AVIMConversation
import cn.leancloud.im.v2.AVIMMessage
import cn.leancloud.im.v2.messages.AVIMAudioMessage
import cn.leancloud.im.v2.messages.AVIMImageMessage
import cn.leancloud.im.v2.messages.AVIMTextMessage
import cn.leancloud.im.v2.messages.AVIMVideoMessage
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.bean.MessageBean
import com.blogofyb.oo.config.KEY_NICKNAME
import com.blogofyb.oo.config.KEY_USERNAME
import com.blogofyb.oo.config.KEY_USER_HEADER
import com.blogofyb.oo.interfaces.model.IChatModel
import com.blogofyb.oo.interfaces.presenter.IChatPresenter
import com.blogofyb.oo.interfaces.view.IChatView
import com.blogofyb.oo.presenter.ChatPresenter
import com.blogofyb.oo.util.GlobalMessageManager
import com.blogofyb.oo.util.event.OnMessageScrollToTopEvent
import com.blogofyb.oo.util.extensions.doPermissionAction
import com.blogofyb.oo.view.adapter.ChatRecyclerViewAdapter
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_input_tool.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import top.limuyang2.photolibrary.activity.LPhotoPickerActivity
import top.limuyang2.photolibrary.util.LPPImageType
import java.io.File

/**
 * Create by yuanbing
 * on 2019/8/17
 */
class ChatActivity : BaseActivity<IChatView, IChatPresenter, IChatModel>(), IChatView {
    private lateinit var mAdapter: ChatRecyclerViewAdapter
    private lateinit var mListener: ViewTreeObserver.OnGlobalLayoutListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initToolbar()
        initInputTool()
        initView()

        GlobalMessageManager.getConversation(intent?.getStringExtra(KEY_USERNAME)) {
            presenter?.bindConversation(it)
            presenter?.getMessage()
        }
        mListener = ViewTreeObserver.OnGlobalLayoutListener {
            scrollToBottom()
        }
        rv_chat.viewTreeObserver.addOnGlobalLayoutListener(mListener)
    }

    override fun onResume() {
        super.onResume()
        GlobalMessageManager.subscribe(
            intent?.getStringExtra(KEY_USERNAME) ?: "",
            object : GlobalMessageManager.MessageObserver {
                override fun onMessage(message: AVIMMessage, conversation: AVIMConversation) {
                    sendMessageSuccess(MessageBean(
                        if (message is AVIMTextMessage) message.text else "",
                        if (message is AVIMImageMessage) message.fileUrl else "",
                        if (message is AVIMAudioMessage) message.fileUrl else "",
                        if (message is AVIMVideoMessage) message.fileUrl else "",
                        intent?.getStringExtra(KEY_USER_HEADER) ?: "",
                        message.from == AVUser.currentUser().username
                    ))
                }
            }
        )
    }

    override fun onStop() {
        super.onStop()
        GlobalMessageManager.unSubscribe(intent?.getStringExtra(KEY_USERNAME) ?: "")
    }

    override fun getViewToAttach() = this

    override fun createPresenter() = ChatPresenter()

    override fun sendMessageSuccess(message: MessageBean) {
        mAdapter.newMessage(message)
        scrollToBottom()
    }

    override fun showMessage(message: List<MessageBean>) {
        val fromHeader = intent?.getStringExtra(KEY_USER_HEADER)
        val currentHeader = AVUser.currentUser()?.getString(KEY_USER_HEADER)
        mAdapter.refreshData(
            message.map {
                if (it.isSend) {
                    it.header = currentHeader ?: ""
                } else {
                    it.header = fromHeader ?: ""
                }
                it
            }
        )
        rv_chat.layoutManager?.scrollToPosition(message.size - 1)
    }

    private fun initView() {
        mAdapter = ChatRecyclerViewAdapter()
        rv_chat.adapter = mAdapter
        rv_chat.layoutManager = LinearLayoutManager(this)

        btn_message_send.setOnClickListener {
            presenter?.sendText(et_message_input.text.toString())
            et_message_input.text.clear()
        }
        btn_message_send.setTextColor(Color.GRAY)
        btn_message_send.isClickable = false

        et_message_input.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.isNullOrBlank()) {
                        btn_message_send.setTextColor(Color.GRAY)
                        btn_message_send.isClickable = false
                    } else {
                        btn_message_send.setTextColor(Color.WHITE)
                        btn_message_send.isClickable = true
                    }
                }
            }
        )
    }

    private fun initInputTool() {
        KeyboardUtil.attach(this, kp_fl_message_input_tool)

        iv_message_input_mic.setOnClickListener {  }

        iv_message_input_pic.setOnClickListener {
            val intent = LPhotoPickerActivity.IntentBuilder(this@ChatActivity)
                .maxChooseCount(1)
                .columnsNumber(4)
                .imageType(LPPImageType.ofAll())
                .pauseOnScroll(false)
                .isSingleChoose(true)
                .theme(R.style.AppTheme)
                .selectedPhotos(ArrayList())
                .build()
            doPermissionAction(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                doAfterGranted { startActivityForResult(intent, 0) }
            }
        }

        iv_message_input_camera.setOnClickListener {

        }
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = intent?.getStringExtra(KEY_NICKNAME)
        toolbar.setNavigationOnClickListener { finish() }

        window.statusBarColor = resources.getColor(R.color.colorPrimary)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            if (kp_fl_message_input_tool.visibility == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(kp_fl_message_input_tool)
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun scrollToBottom() {
        val layoutManager = rv_chat.layoutManager as LinearLayoutManager
        layoutManager.smoothScrollToPosition(rv_chat, null, rv_chat.adapter?.itemCount ?: 0)
    }

    override fun showMoreMessage(message: List<MessageBean>) {
        // TODO bug
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun getMoreMessage(event: OnMessageScrollToTopEvent) {
        presenter?.getMoreMessage()
        Log.d("EventBus", "getMoreMessage")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = LPhotoPickerActivity.getSelectedPhotos(data)
                    val outUri = Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}.jpg"))
                    UCrop.of(Uri.fromFile(File(result[0])), outUri)
                        .start(this)
                }
            }
            UCrop.REQUEST_CROP -> {
                data?.let {
                    val resultUri = UCrop.getOutput(data)
                    presenter?.sendPic(resultUri?.path ?: "")
                    Log.d("UCrop.REQUEST_CROP", resultUri?.path ?: "no file")
                }
            }
        }
    }
}