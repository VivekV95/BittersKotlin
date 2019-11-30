package com.vivekvishwanath.bitterskotlin.ui.main.create


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.vivekvishwanath.bitterskotlin.R
import kotlinx.android.synthetic.main.fragment_create.*


class CreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_ingredients_fragment_button.setOnClickListener {
            findNavController().navigate(R.id.action_createFragment_to_selectIngredientsFragment)
        }
    }


}
