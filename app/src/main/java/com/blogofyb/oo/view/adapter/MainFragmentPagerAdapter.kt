package com.blogofyb.oo.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blogofyb.oo.view.fragment.FriendsFragment
import com.blogofyb.oo.view.fragment.MessageFragment
import com.blogofyb.oo.view.fragment.NewFragment

/**
 * Create by yuanbing
 * on 2019/8/17
 */
class MainFragmentPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val mFragments: List<Fragment> = listOf(
        MessageFragment(),
        FriendsFragment(),
        NewFragment()
    )

    override fun getItem(position: Int) = mFragments[position]

    override fun getCount() = mFragments.size
}