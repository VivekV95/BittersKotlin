package com.vivekvishwanath.bitterskotlin.ui.adapter

import android.view.*
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import kotlinx.android.synthetic.main.cocktail_list_item.view.*
import kotlinx.android.synthetic.main.cocktail_list_item.view.cocktail_card_star
import java.lang.Exception

class CocktailListAdapter(
    private val picasso: Picasso,
    private val cocktailInteractionListener: CocktailInteractionListener? = null
) :
    RecyclerView.Adapter<CocktailListAdapter.CocktailViewHolder>() {

    private var favoriteids = setOf<Int>()
    private var cocktails = listOf<Cocktail>()

    private var lastPosition = RecyclerView.NO_POSITION

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
        picasso
            .load(cocktails[position].drinkImage)
            .into(holder.itemView.cocktail_card_image, object : Callback {
                override fun onSuccess() {
                    holder.itemView.shimmar_layout.stopShimmer()
                }

                override fun onError(e: Exception?) {
                    holder.itemView.shimmar_layout.stopShimmer()
                }
            })

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

    fun submitCocktails(list: List<Cocktail>) {
        this.cocktails = list
        list.forEach {
            if (favoriteids.contains(it.drinkId.toInt()))
                it.isFavorite = true
        }
        submitList()
    }

    fun submitFavoriteIds(favoriteids: Set<Int>) {
        this.favoriteids = favoriteids
        cocktails.forEach {
            if (favoriteids.contains(it.drinkId.toInt()))
                it.isFavorite = true
        }
        submitList()
    }

    private fun submitList() {
        differ.submitList(cocktails)
    }

    inner class CocktailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Cocktail) = with(itemView) {

            val isFavorite = favoriteids.contains(item.drinkId.toInt())

            this.cocktail_card_name.text = item.drinkName

            if (isFavorite)
                this.cocktail_card_star.setImageResource(R.drawable.ic_filled_star_cocktail_card)
            else
                this.cocktail_card_star.setImageResource(R.drawable.ic_empty_star_cocktail_card)

            ViewCompat.setTransitionName(
                this.cocktail_card_image, "cocktail_image_$adapterPosition"
            )

            this.setOnClickListener {
                val cocktail = item.copy()
                cocktail.isFavorite = favoriteids.contains(cocktail.drinkId.toInt())
                val extras = FragmentNavigatorExtras(
                    this.cocktail_card_image to ViewCompat.getTransitionName(itemView.cocktail_card_image)!!
                )
                cocktailInteractionListener?.onCocktailSelected(adapterPosition, cocktail, extras)
            }

            this.setOnLongClickListener {
                cocktailInteractionListener?.onCocktailLongPressed(adapterPosition, item)
                if (isFavorite) {
                    this.cocktail_card_star.setImageResource(R.drawable.ic_empty_star_cocktail_card)
                    item.isFavorite = false
                } else {
                    this.cocktail_card_star.setImageResource(R.drawable.ic_filled_star_cocktail_card)
                    item.isFavorite = true
                }
                YoYo
                    .with(Techniques.Pulse)
                    .duration(500)
                    .playOn(this)
                true
            }
        }
    }

    interface CocktailInteractionListener {
        fun onCocktailSelected(position: Int, item: Cocktail, extras: FragmentNavigator.Extras)

        fun onCocktailLongPressed(position: Int, item: Cocktail)
    }
}