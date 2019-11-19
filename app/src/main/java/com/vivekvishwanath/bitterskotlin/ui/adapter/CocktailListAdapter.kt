package com.vivekvishwanath.bitterskotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import kotlinx.android.synthetic.main.cocktail_list_item.view.*

class CocktailListAdapter(val requestManager: RequestManager) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cocktail>() {

        override fun areItemsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean =
            oldItem.drinkId == newItem.drinkId

        override fun areContentsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean =
            (oldItem.drinkName == newItem.drinkName &&
                    oldItem.drinkImage == newItem.drinkImage)

    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CocktailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cocktail_list_item, parent, false)
        )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CocktailViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    fun submitList(list: List<Cocktail>) {
        differ.submitList(list)
    }

    inner class CocktailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Cocktail) = with(itemView) {
            itemView.cocktail_card_name.text = item.drinkName

            requestManager
                .load(item.drinkImage)
                .into(itemView.cocktail_card_image)
        }
    }
}