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
import com.squareup.picasso.Picasso

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
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
            view_cocktail_favorite_star -> {
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
            }
        }
    }

    private lateinit var currentCocktail: Cocktail

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
            }
        })
    }

    private fun showCocktail(cocktail: Cocktail) {
        picasso
            .load(cocktail.drinkImage)
            .into(view_cocktail_card_image)

        setStarSelected(cocktail.isFavorite)
        view_cocktail_favorite_star.setOnClickListener(this)

        arguments?.getInt("position").let { position ->
            ViewCompat.setTransitionName(view_cocktail_card_image, "cocktail_image_$position")
        }
    }

    fun setStarSelected(isFavorite: Boolean) {
        view_cocktail_favorite_star.setImageResource(
            if (isFavorite) R.drawable.ic_filled_star_view_cocktail
            else R.drawable.ic_empty_star_view_cocktail
        )
    }


}
