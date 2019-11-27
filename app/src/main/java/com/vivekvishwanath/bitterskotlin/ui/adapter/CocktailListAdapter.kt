package com.vivekvishwanath.bitterskotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import kotlinx.android.synthetic.main.cocktail_list_item.view.*
import kotlinx.android.synthetic.main.cocktail_list_item.view.coctail_card_star
import kotlinx.android.synthetic.main.fragment_view_cocktail.view.*

class CocktailListAdapter(
    private val requestManager: RequestManager,
    private val cocktailClickListener: CocktailClickListener? = null
) :
    RecyclerView.Adapter<CocktailListAdapter.CocktailViewHolder>() {

    private var favoriteIds = listOf<Int>()
    private var cocktails = listOf<Cocktail>()

    private var lastPosition = -1

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cocktail>() {

        override fun areItemsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
            return oldItem.drinkId == newItem.drinkId
        }

        override fun areContentsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CocktailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cocktail_list_item, parent, false)
        )

    override fun getItemCount() = differ.currentList.size


    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        setEnterAnimation(holder.itemView, position)
    }
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is CocktailViewHolder -> {
//                holder.bind(differ.currentList[position])
//                //setEnterAnimation(holder.itemView.cocktail_card, position)
//            }
//        }
//    }

    private fun setEnterAnimation(viewToAnimate: View, position: Int) {
        //if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
          //  lastPosition = position
        //}
    }

    fun submitCocktails(list: List<Cocktail>) {
        this.cocktails = list
        cocktails.forEach { cocktail ->
            cocktail.isFavorite = favoriteIds.contains(cocktail.drinkId.toInt())
        }
        submitList()
    }

    fun submitFavoriteIds(favoriteIds: List<Int>) {
        this.favoriteIds = favoriteIds
        cocktails.forEach { cocktail ->
            cocktail.isFavorite = favoriteIds.contains(cocktail.drinkId.toInt())
        }
        submitList()
    }

    private fun submitList() {
        differ.submitList(cocktails)
    }

    inner class CocktailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Cocktail) = with(itemView) {
            itemView.setOnClickListener {
                cocktailClickListener?.onCocktailClicked(adapterPosition, item)
                notifyItemChanged(adapterPosition)
            }

            itemView.cocktail_card_name.text = item.drinkName
            if (item.isFavorite)
                itemView.coctail_card_star.setImageResource(R.drawable.ic_filled_star)
            else
                itemView.coctail_card_star.setImageResource(R.drawable.ic_empty_star)

            requestManager
                .load(item.drinkImage)
                .into(itemView.cocktail_card_image)
        }
    }

    interface CocktailClickListener {
        fun onCocktailClicked(position: Int, item: Cocktail)
    }
}