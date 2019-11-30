package com.vivekvishwanath.bitterskotlin.ui.main


import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.bumptech.glide.RequestManager

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import kotlinx.android.synthetic.main.fragment_view_cocktail.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ViewCocktailFragment : Fragment() {

    @Inject
    lateinit var requestManager: RequestManager

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
        arguments?.getParcelable<Cocktail>("cocktail").let {
            requestManager
                .load(it?.drinkImage)
                .into(view_cocktail_card_image)
        }

        arguments?.getInt("position").let {  position ->
            ViewCompat.setTransitionName(view_cocktail_card_image,"cocktail_image_$position")
        }
    }


}
