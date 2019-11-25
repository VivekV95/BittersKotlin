package com.vivekvishwanath.bitterskotlin.ui.main.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.ui.adapter.CocktailListAdapter
import com.vivekvishwanath.bitterskotlin.ui.main.BaseCocktailFragment
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.ui.main.CocktailViewModel
import com.vivekvishwanath.bitterskotlin.ui.main.state.MainStateEvent
import com.vivekvishwanath.bitterskotlin.util.SpacingItemDecoration
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_popular.*
import java.lang.Exception
import javax.inject.Inject

class PopularFragment : BaseCocktailFragment(), CocktailListAdapter.CocktailClickListener {


    private lateinit var cocktailListAdapter: CocktailListAdapter

    override fun onCocktailClicked(position: Int, item: Cocktail) {
        activity?.let {
            val bundle = bundleOf("cocktail" to item)
            Navigation
                .findNavController(it, R.id.main_nav_host_fragment)
                .navigate(R.id.action_popularFragment_to_viewCocktailFragment, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).mainComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        initRecyclerView()
        triggerGetPopularCocktailsEvent()
    }

    private fun triggerGetPopularCocktailsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetPopularCocktailsEvent)
    }

    private fun initRecyclerView() {
        popular_recycler_view.apply {
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(SpacingItemDecoration(8))
            cocktailListAdapter = CocktailListAdapter(requestManager, this@PopularFragment)
            adapter = cocktailListAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            stateChangeListener.onDataStateChanged(dataState)
            dataState.data?.data?.let { event ->
                event.getContentIfNotHandled()?.let { mainViewState ->
                    mainViewState.popularCocktails?.let { cocktails ->
                        viewModel.setPopularCocktailsData(cocktails)
                    }
                }
            }
        })

        viewModel.viewState.observe(this, Observer { viewState ->
            viewState.popularCocktails?.let { cocktails ->
                cocktailListAdapter.submitList(cocktails)
            }
        })
    }

}
