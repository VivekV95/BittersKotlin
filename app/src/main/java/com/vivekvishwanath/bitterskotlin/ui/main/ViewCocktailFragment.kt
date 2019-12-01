package com.vivekvishwanath.bitterskotlin.ui.main


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.ui.adapter.IngredientListAdapter
import com.vivekvishwanath.bitterskotlin.util.IngredientSpacingItemDecoration
import com.vivekvishwanath.bitterskotlin.util.convertIngredientsToList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_view_cocktail.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ViewCocktailFragment : BaseCocktailFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v) {
            view_cocktail_star_layout -> {
                if (currentCocktail.isFavorite) {
                    setStarSelected(false)
                    GlobalScope.launch(Main) {
                        viewModel.deleteFavoriteCocktail(currentCocktail)
                    }
                } else {
                    setStarSelected(true)
                    GlobalScope.launch(Main) {
                        viewModel.addFavoriteCocktail(currentCocktail)
                    }
                }
                YoYo
                    .with(Techniques.RubberBand)
                    .duration(500)
                    .playOn(view_cocktail_favorite_star)
            }
        }
    }

    private lateinit var currentCocktail: Cocktail

    private lateinit var ingredientListAdapter: IngredientListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater
                .from(context)
                .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_cocktail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Cocktail>("cocktail")?.let { cocktail ->
            viewModel.setCurrentCocktail(cocktail)
            showCocktail(cocktail)
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.getFavoriteIds().observe(viewLifecycleOwner, Observer { favoriteIds ->
            favoriteIds.data?.data?.peekContent()?.let { ids ->
                if (::currentCocktail.isInitialized) {
                    currentCocktail.isFavorite = ids.contains(currentCocktail.drinkId.toInt())
                    currentCocktail.let {
                        viewModel.setCurrentCocktail(it)
                        setStarSelected(currentCocktail.isFavorite)
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.viewCocktailField.currentCocktail?.let {
                this.currentCocktail = it
                initRecyclerView()
            }
        })
    }

    private fun showCocktail(cocktail: Cocktail) {

        view_cocktail_name.text = cocktail.drinkName
        view_cocktail_instructions.text = cocktail.drinkInstructions?.trim()
        picasso
            .load(cocktail.drinkImage)
            .into(view_cocktail_card_image)

        setStarSelected(cocktail.isFavorite)
        view_cocktail_star_layout.setOnClickListener(this)

        arguments?.getInt("position").let { position ->
            ViewCompat.setTransitionName(view_cocktail_card_image, "cocktail_image_$position")
        }
    }

    private fun initRecyclerView() {
        ingredient_recycler_view.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(activity)
            ingredientListAdapter = IngredientListAdapter(picasso)
            adapter = ingredientListAdapter
            val list = currentCocktail.convertIngredientsToList()
            ingredientListAdapter.submitList(list)
            isNestedScrollingEnabled = false
            addItemDecoration(IngredientSpacingItemDecoration(3))
        }
    }

    private fun setStarSelected(isFavorite: Boolean) {
        view_cocktail_favorite_star.setImageResource(
            if (isFavorite) R.drawable.ic_filled_star_view_cocktail
            else R.drawable.ic_empty_star_view_cocktail
        )
    }


}
