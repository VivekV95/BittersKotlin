package com.vivekvishwanath.bitterskotlin.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.vivekvishwanath.bitterskotlin.ui.main.view.CustomFragment
import com.vivekvishwanath.bitterskotlin.ui.main.view.FavoritesFragment
import com.vivekvishwanath.bitterskotlin.ui.main.view.PopularFragment

class PageAdapter(fm: FragmentManager, private val numOfTabs: Int):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 ->  PopularFragment()
            1 ->  FavoritesFragment()
            2 ->  CustomFragment()
            else -> PopularFragment()
        }
    }

    override fun getCount() = numOfTabs


}