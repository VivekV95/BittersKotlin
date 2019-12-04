package com.vivekvishwanath.bitterskotlin.ui.main.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.*

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.adapter.PageAdapter
import kotlinx.android.synthetic.main.fragment_view.*

class FragmentView : Fragment() {

    private lateinit var pageAdapter: PageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageAdapter = PageAdapter(childFragmentManager, tabs.tabCount)
        view_pager.adapter = pageAdapter

        tabs.addOnTabSelectedListener(object: OnTabSelectedListener {
            override fun onTabReselected(p0: Tab?) {
            }

            override fun onTabUnselected(p0: Tab?) {
            }

            override fun onTabSelected(tab: Tab?) {
                view_pager.currentItem  = tab!!.position
            }

        })
        view_pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabs))
    }


}
