package com.vivekvishwanath.bitterskotlin.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import kotlinx.android.synthetic.main.cocktail_list_item.view.*
import kotlinx.android.synthetic.main.cocktail_list_item.view.cocktail_card_star

class CocktailListAdapter(
    private val requestManager: RequestManager,
    private val cocktailInteractionListener: CocktailInteractionListener? = null
) :
    RecyclerView.Adapter<CocktailListAdapter.CocktailViewHolder>() {

    private var favoriteids = setOf<Int>()
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
        holder.itemView.shimmar_layout.showShimmer(true)
        requestManager
            .load(cocktails[position].drinkImage)
            .timeout(10000)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.itemView.shimmar_layout.hideShimmer()
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.itemView.shimmar_layout.stopShimmer()
                    return false
                }
            })
            .into(holder.itemView.cocktail_card_image)
            .clearOnDetach()

        setEnterAnimation(holder.itemView, position)
    }

    private fun setEnterAnimation(viewToAnimate: View, position: Int) {
       // if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
        //    lastPosition = position
       // }
    }

    fun submitCocktails(list: List<Cocktail>) {
        this.cocktails = list
        submitList()
    }

    fun submitFavoriteIds(favoriteids: Set<Int>) {
        this.favoriteids = favoriteids
    }

    private fun submitList() {
        differ.submitList(cocktails)
    }

    inner class CocktailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Cocktail) = with(itemView) {

            val isFavorite = favoriteids.contains(item.drinkId.toInt())

            itemView.cocktail_card_name.text = item.drinkName

            if (isFavorite)
                itemView.cocktail_card_star.setImageResource(R.drawable.ic_filled_star)
            else
                itemView.cocktail_card_star.setImageResource(R.drawable.ic_empty_star)

            itemView.setOnClickListener {
                val cocktail = item.copy()
                cocktail.isFavorite = isFavorite
                cocktailInteractionListener?.onCocktailSelected(adapterPosition, cocktail)
            }
        }
    }

    interface CocktailInteractionListener {
        fun onCocktailSelected(position: Int, item: Cocktail)
    }
}