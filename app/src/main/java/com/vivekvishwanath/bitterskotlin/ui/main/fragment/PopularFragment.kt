package com.vivekvishwanath.bitterskotlin.ui.main.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.adapter.CocktailListAdapter
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.ui.main.MainViewModel
import com.vivekvishwanath.bitterskotlin.ui.main.state.MainStateEvent
import com.vivekvishwanath.bitterskotlin.util.SpacingItemDecoration
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_popular.*
import java.lang.Exception
import javax.inject.Inject

class PopularFragment : Fragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    @Inject
    lateinit var requestManager: RequestManager

    lateinit var viewModel: MainViewModel

    private lateinit var cocktailListAdapter: CocktailListAdapter

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

        viewModel = activity?.run {
            ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]
        }?: throw Exception("Invalid Activity")

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
            cocktailListAdapter = CocktailListAdapter(requestManager)
            adapter = cocktailListAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            dataState.data?.let { event ->
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
