package com.vivekvishwanath.bitterskotlin.ui.main.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.*

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.adapter.PageAdapter
import com.vivekvishwanath.bitterskotlin.ui.main.BaseCocktailFragment
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListStateEvent
import kotlinx.android.synthetic.main.fragment_view.*

class ViewFragment : BaseCocktailFragment() {

    private lateinit var pageAdapter: PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).mainComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

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

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(p0: Tab?) {

            }

            override fun onTabUnselected(p0: Tab?) {
            }

            override fun onTabSelected(tab: Tab?) {
                tab?.position?.let { position ->
                    when (position) {
                        1 -> {
                            viewModel.setStateEvent(CocktailListStateEvent.GetFavoriteCocktailsEvent)
                        }
                    }
                    view_pager.currentItem = position
                }
            }

        })
        view_pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabs))
    }
}
