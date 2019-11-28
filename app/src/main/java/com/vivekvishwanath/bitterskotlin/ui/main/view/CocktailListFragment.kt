package com.vivekvishwanath.bitterskotlin.ui.main.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.network.FirebaseDatabaseDao
import com.vivekvishwanath.bitterskotlin.ui.adapter.CocktailListAdapter
import com.vivekvishwanath.bitterskotlin.ui.main.BaseCocktailFragment
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListStateEvent
import com.vivekvishwanath.bitterskotlin.util.SpacingItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cocktail_list.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CocktailListFragment : BaseCocktailFragment(), CocktailListAdapter.CocktailClickListener {


    private lateinit var cocktailListAdapter: CocktailListAdapter

    override fun onCocktailClicked(position: Int, item: Cocktail, favorite: Boolean) {
        activity?.let {
            GlobalScope.launch(Main) {
                stateChangeListener.onDataStateChanged(DataState.loading(true, null))
                if (!favorite) {
                    val dataState = viewModel.addFavoriteCocktail(cocktail = item)
                    stateChangeListener.onDataStateChanged(DataState.loading(false, null))
                    dataState.data?.data?.getContentIfNotHandled()?.let {
                        cocktailListAdapter.notifyItemChanged(position)
                    }
                } else {
                    val dataState = viewModel.deleteFavoriteCocktail(cocktail = item)
                    stateChangeListener.onDataStateChanged(DataState.loading(false, null))
                    dataState.data?.data?.getContentIfNotHandled()?.let {
                        cocktailListAdapter.notifyItemChanged(position)
                    }
                }
            }
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
        return inflater.inflate(R.layout.fragment_cocktail_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        subscribeObservers()
        triggerGetPopularCocktailsEvent()
    }

    private fun triggerGetPopularCocktailsEvent() {
        viewModel.setStateEvent(CocktailListStateEvent.GetPopularCocktailsEvent)
    }

    private fun initRecyclerView() {
        popular_recycler_view.apply {
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(SpacingItemDecoration(8))
            cocktailListAdapter = CocktailListAdapter(requestManager, this@CocktailListFragment)
            adapter = cocktailListAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChanged(dataState)
            dataState.data?.data?.let { event ->
                event.getContentIfNotHandled()?.let { mainViewState ->
                    mainViewState.popularCocktails?.let { cocktails ->
                        viewModel.setPopularCocktailsData(cocktails)
                    }
                }
            }
        })

        viewModel.getFavoriteIds().observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.data?.data?.let { event ->
                event.getContentIfNotHandled()?.let { ids ->
                    viewModel.setFavoriteIdsData(ids)
                }
            }

        })

        viewModel.viewState.observe(this, Observer { viewState ->
            viewState.popularCocktails?.let { cocktails ->
                cocktailListAdapter.submitCocktails(cocktails)
            }

            viewState.favoriteCocktailIds?.let { ids  ->
                cocktailListAdapter.submitFavoriteIds(ids)
            }
        })
    }

}
