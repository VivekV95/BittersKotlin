package com.vivekvishwanath.bitterskotlin.ui.main

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.ui.DataStateChangedListener
import com.vivekvishwanath.bitterskotlin.ui.adapter.CocktailListAdapter
import com.vivekvishwanath.bitterskotlin.ui.main.view.viewmodel.CocktailListViewModel
import com.vivekvishwanath.bitterskotlin.util.*
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import java.lang.Exception
import javax.inject.Inject

abstract class BaseCocktailFragment: Fragment(), CocktailListAdapter.CocktailInteractionListener {

    override fun onCocktailSelected(
        position: Int,
        item: Cocktail,
        extras: FragmentNavigator.Extras
    ) {
        val bundle = bundleOf(
            "cocktail" to item,
            "position" to position
        )

        findNavController().navigate(
            R.id.action_viewFragment_to_viewCocktailFragment,
            bundle, null, extras
        )
    }

    override fun onCocktailLongPressed(position: Int, item: Cocktail) {
        GlobalScope.launch(Dispatchers.Main) {
            if (item.isFavorite)
                viewModel.addFavoriteCocktail(item)
            else
                viewModel.deleteFavoriteCocktail(item)
            cocktailListAdapter.notifyItemChanged(position)
        }
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    @Inject
    lateinit var picasso: Picasso

    lateinit var viewModel: CocktailListViewModel

    lateinit var stateChangeListener: DataStateChangedListener

    protected lateinit var cocktailListAdapter: CocktailListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        activity?.run {
            viewModel = ViewModelProvider(this, viewModelProviderFactory)[CocktailListViewModel::class.java]
        }
        cancelActiveJobs()
    }

    protected fun cancelActiveJobs() {
        viewModel.cancelActiveJobs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangedListener
        } catch (e: ClassCastException) {
            Log.d(LOG_TAG, "${this.javaClass.simpleName}: $context must implement DataStateChangeListener")
        }
    }


    protected fun submitCocktails(cocktails: List<Cocktail>) {
        cocktailListAdapter.submitCocktails(cocktails)
    }

    protected fun submitFavoriteIds(ids: Set<Int>) {
        cocktailListAdapter.submitFavoriteIds(ids)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cocktail_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cocktail_type -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}