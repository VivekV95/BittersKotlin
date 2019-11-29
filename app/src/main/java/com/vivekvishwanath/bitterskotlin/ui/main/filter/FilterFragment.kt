package com.vivekvishwanath.bitterskotlin.ui.main.filter


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.vivekvishwanath.bitterskotlin.R
import kotlinx.android.synthetic.main.fragment_filter.*


class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_cocktail_test_button.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_viewCocktailFragment2)
        }
    }
}
