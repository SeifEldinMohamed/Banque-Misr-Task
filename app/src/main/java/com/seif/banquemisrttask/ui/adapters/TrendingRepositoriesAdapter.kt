package com.seif.banquemisrttask.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.seif.banquemisrttask.data.network.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.databinding.ItemTrendingRowBinding
import com.seif.banquemisrttask.util.RepositoriesDiffUtil
import com.squareup.picasso.Picasso

class TrendingRepositoriesAdapter :
    RecyclerView.Adapter<TrendingRepositoriesAdapter.MyViewHolder>() {
    private var trendingRepositories: List<TrendingRepositoriesItem> = emptyList()
    var previousExpandedPosition = -1
    var expandedPosition = -1

    inner class MyViewHolder(private val binding: ItemTrendingRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trendingRepositoriesItem: TrendingRepositoriesItem, position: Int) {
            binding.tvAuthor.text = trendingRepositoriesItem.author
            binding.tvName.text = trendingRepositoriesItem.name
            binding.tvDescription.text = trendingRepositoriesItem.description
            binding.tvLanguage.text = trendingRepositoriesItem.language
            binding.tvStars.text = trendingRepositoriesItem.stars.toString()
            binding.tvFork.text = trendingRepositoriesItem.forks.toString()
//                binding.cvBullet.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, trendingRepositoriesItem.languageColor.toInt()))
//                binding.cvBullet.setBackgroundColor(Color.parseColor(trendingRepositoriesItem.languageColor))
            Picasso.get().load(trendingRepositoriesItem.avatar).into(binding.ivAvatar)

            val isExpanded = (position == expandedPosition)
            binding.expandableConstraintLayout.visibility =
                if (isExpanded) View.VISIBLE else View.GONE

            if (isExpanded)
                previousExpandedPosition = position

            binding.constraintLayout.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position // save position as expanded
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTrendingRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(trendingRepositories[position], position)
    }

    override fun getItemCount() = trendingRepositories.size


    fun addTrendingRepositoriesItem(newTrendingRepositoriesItem: List<TrendingRepositoriesItem>) { // used in sorting
        val diffUtilCallback =
            RepositoriesDiffUtil(this.trendingRepositories, newTrendingRepositoriesItem)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        this.trendingRepositories = newTrendingRepositoriesItem
        result.dispatchUpdatesTo(this)
    }

}

