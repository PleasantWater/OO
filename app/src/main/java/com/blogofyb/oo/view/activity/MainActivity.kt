package com.blogofyb.oo.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import cn.leancloud.AVUser
import com.blogofyb.oo.R
import com.blogofyb.oo.base.BaseActivity
import com.blogofyb.oo.config.*
import com.blogofyb.oo.util.GlobalMessageManager
import com.blogofyb.oo.util.UserManager
import com.blogofyb.oo.util.extensions.setImageFromUrl
import com.blogofyb.oo.util.extensions.toast
import com.blogofyb.oo.view.adapter.MainFragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

/**
 * Create by yuanbing
 * on 2019/8/16
 */
class MainActivity : BaseActivity() {
    private var mFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDock()
        initNavigator()
        initViewPager()
        initToolbar()
    }

    override fun onResume() {
        super.onResume()
        initNavigatorHeader()
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_main)
        toolbar.setNavigationOnClickListener {
            dl_main.openDrawer(GravityCompat.START)
        }
        toolbar.inflateMenu(R.menu.friend_menu)
        toolbar.menu.getItem(0)?.isVisible = false
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.item_add_friend) {
                val intent = Intent(this@MainActivity, AddFriendsActivity::class.java)
                startActivity(intent)
            }
            true
        }
    }

    private fun initNavigatorHeader() {
        val headerView = navigation_view_main.getHeaderView(0)
        val userHeader: ImageView = headerView.findViewById(R.id.iv_user_head)
        val userName: TextView = headerView.findViewById(R.id.tv_user_name)
        val userSignature: TextView = headerView.findViewById(R.id.tv_user_signature)
        val currentUser = AVUser.currentUser()
        userName.text = currentUser?.getString(KEY_NICKNAME) ?: currentUser.username
        userSignature.text = currentUser?.getString((KEY_SIGNATURE))
        userHeader.setImageFromUrl(currentUser?.getString(KEY_USER_HEADER), R.drawable.ic_launcher,
            R.drawable.ic_launcher)
        headerView.setOnClickListener {
            val intent = Intent(this@MainActivity, UserInformationActivity::class.java)
            intent.putExtra(KEY_USERNAME, currentUser?.username)
            startActivity(intent)
        }
    }

    private fun initDock() {
        tl_dock_main.addOnTabSelectedListener(
            object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
                override fun onTabReselected(p0: TabLayout.Tab?) {}

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    if (p0 == null) return
                    p0.setIcon(when (p0.position) {
                        0 -> R.drawable.ic_message_unselected
                        1 -> R.drawable.ic_friends_unselected
                        else -> R.drawable.ic_new_unselected
                    })
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    if (p0 == null) return
                    p0.setIcon(when (p0.position) {
                        0 -> R.drawable.ic_message_selected
                        1 -> R.drawable.ic_friends_selected
                        else -> R.drawable.ic_new_selected
                    })
                    vp_main.currentItem = p0.position
                    toolbar.menu.getItem(0)?.isVisible = p0.position == 1
                }
            }
        )
        tl_dock_main.getTabAt(0)?.setIcon(R.drawable.ic_message_selected)
    }

    private fun initNavigator() {
        navigation_view_main.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_collection -> {

                }
                R.id.item_logout -> {
                    val user = AVUser.currentUser()
                    val username = user?.username ?: ""
                    UserManager.logout(username)
                    AVUser.logOut()
                    GlobalMessageManager.mClient.close(null)
                    GlobalMessageManager.clearData()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            false
        }
    }

    private fun initViewPager() {
        vp_main.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}

                override fun onPageSelected(position: Int) {
                    tl_dock_main.getTabAt(position)?.select()
                }
            }
        )
        vp_main.adapter = MainFragmentPagerAdapter(supportFragmentManager)
    }

    override fun onBackPressed() {
        if (dl_main.isDrawerOpen(GravityCompat.START)) {
            dl_main.closeDrawers()
            return
        }
        if (mFlag) {
            finish()
        } else {
            toast("再按一次退出")
            mFlag = true
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    mFlag = false
                }
            }, 1500)
        }
    }
}