package com.vivekvishwanath.bitterskotlin.ui.main.view


import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.ui.adapter.CocktailListAdapter
import com.vivekvishwanath.bitterskotlin.ui.main.BaseCocktailFragment
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.util.COCKTAIL_RV_SPACING
import com.vivekvishwanath.bitterskotlin.util.LANDSCAPE_RV_COLUMNS
import com.vivekvishwanath.bitterskotlin.util.PORTRAIT_RV_COLUMNS
import com.vivekvishwanath.bitterskotlin.util.CocktailSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_cocktail_list.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CocktailListFragment : BaseCocktailFragment(), CocktailListAdapter.CocktailInteractionListener {

    private lateinit var cocktailListAdapter: CocktailListAdapter

    override fun onCocktailSelected(position: Int, item: Cocktail, extras: FragmentNavigator.Extras) {
        val bundle = bundleOf("cocktail" to item,
            "position" to position)

        findNavController().navigate(
            R.id.action_cocktailListFragment_to_viewCocktailFragment,
            bundle, null, extras)
    }

    override fun onCocktailLongPressed(position: Int, item: Cocktail) {
        GlobalScope.launch(Main) {
            if(item.isFavorite)
                viewModel.addFavoriteCocktail(item)
            else
                viewModel.deleteFavoriteCocktail(item)
            cocktailListAdapter.notifyItemChanged(position)
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
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        initRecyclerView()
        subscribeObservers()
    }

    private fun initRecyclerView() {
        popular_recycler_view.apply {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = GridLayoutManager(activity, LANDSCAPE_RV_COLUMNS)
                addItemDecoration(CocktailSpacingItemDecoration(COCKTAIL_RV_SPACING, false))
            }
             else {
                layoutManager = GridLayoutManager(activity, PORTRAIT_RV_COLUMNS)
                addItemDecoration(CocktailSpacingItemDecoration(COCKTAIL_RV_SPACING, true))
            }
            cocktailListAdapter = CocktailListAdapter(picasso, this@CocktailListFragment)
            adapter = cocktailListAdapter
            postponeEnterTransition()
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChanged(dataState)
            dataState.data?.data?.let { event ->
                event.getContentIfNotHandled()?.let { mainViewState ->
                    mainViewState.cocktailFields.popularCocktails.let { cocktails ->
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

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.cocktailFields.popularCocktails.let { cocktails ->
                cocktailListAdapter.submitCocktails(cocktails)
            }

            viewState.favoriteIds.let { ids ->
                cocktailListAdapter.submitFavoriteIds(ids)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cocktail_list_menu, menu)
    }
}
