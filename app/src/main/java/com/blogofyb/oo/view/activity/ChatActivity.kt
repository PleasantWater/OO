package com.blogofyb.oo.view.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil
import cn.dreamtobe.kpswitch.util.KeyboardUtil
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMMessage
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage
import com.blogofyb.oo.R
import com.blogofyb.oo.base.mvp.BaseActivity
import com.blogofyb.oo.bean.MessageBean
import com.blogofyb.oo.config.KEY_NICKNAME
import com.blogofyb.oo.config.KEY_PIC_PATH
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
    private lateinit var mManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initToolbar()
        initInputTool()
        mManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        initView()

        GlobalMessageManager.getConversation(intent?.getStringExtra(KEY_USERNAME)) {
            presenter?.bindConversation(it)
            presenter?.getMessage()
        }
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
                        message.from == AVUser.getCurrentUser().username
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
        mAdapter.refreshData(message)
        // 滚不倒底？？
        (rv_chat.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(message.size - 1, 0)
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

        rv_chat.viewTreeObserver.addOnGlobalLayoutListener {
            if (mManager.isAcceptingText) {
                scrollToBottom()
            }
        }

        val detector = GestureDetector(
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                    hideIME(rv_chat)
                    return false
                }
            }
        )
        rv_chat.setOnTouchListener { _, motionEvent ->
            detector.onTouchEvent(motionEvent)
        }
    }

    private fun initInputTool() {
        KeyboardUtil.attach(this, kp_fl_message_input_tool)

        iv_message_input_mic.setOnClickListener {  }

        iv_message_input_pic.setOnClickListener {
            val intent = Intent(this@ChatActivity, SelectPicActivity::class.java)
            startActivityForResult(intent, 0)
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
        if (message.isEmpty()) return
        mAdapter.showMoreMessage(message)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun getMoreMessage(event: OnMessageScrollToTopEvent) {
        presenter?.getMoreMessage()
        Log.d("EventBus", "getMoreMessage")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    presenter?.sendPic(data.getStringExtra(KEY_PIC_PATH) ?: "")
                }
            }
        }
    }

    private fun hideIME(view: View) {
        et_message_input.clearFocus()
        mManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}