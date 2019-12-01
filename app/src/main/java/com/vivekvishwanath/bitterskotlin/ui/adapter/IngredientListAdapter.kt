package com.vivekvishwanath.bitterskotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Ingredient
import kotlinx.android.synthetic.main.ingredients_list_item.view.*
import java.lang.Exception

class IngredientListAdapter(
    private val picasso: Picasso
) : RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>() {

    private var lastPosition = RecyclerView.NO_POSITION

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.ingredientName == newItem.ingredientName
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<Ingredient>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = IngredientViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.ingredients_list_item, parent, false)
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(
        holder: IngredientViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position])
        setEnterAnimation(holder.itemView, position)
    }

    private fun setEnterAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.fade_in)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Ingredient) = with(itemView) {
            this.ingredient_card_name.text = item.ingredientName
            this.ingredient_card_measurement.text = item.ingredientMeasurement

            this.shimmar_layout.showShimmer(true)
            picasso
                .load(item.ingredientImage)
                .into(this.ingredient_card_image, object : Callback {
                    override fun onSuccess() {
                        itemView.shimmar_layout.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        itemView.shimmar_layout.visibility = View.GONE
                    }
                })
        }
    }
}