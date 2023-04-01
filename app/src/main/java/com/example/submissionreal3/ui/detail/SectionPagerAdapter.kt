package com.example.submissionreal3.ui.detail

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.submissionreal3.R

class SectionPagerAdapter(fragmentActivity: FragmentActivity, data: Bundle) : FragmentStateAdapter(fragmentActivity) {

    private var fragmentBundle: Bundle

    init{
        fragmentBundle = data
    }

    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
        if (fragment != null) {
            fragment.arguments = this.fragmentBundle
        }
        return fragment as Fragment
    }

    fun getTabTitle(position: Int): Int {
        return Companion.TAB_TITLES[position]
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_1, R.string.tab_2)
    }


}
