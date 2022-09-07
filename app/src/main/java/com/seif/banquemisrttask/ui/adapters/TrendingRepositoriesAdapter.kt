package com.seif.banquemisrttask.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.seif.banquemisrttask.databinding.ItemTrendingRowBinding
import com.seif.banquemisrttask.domain.model.TrendingRepository
import com.seif.banquemisrttask.util.RepositoriesDiffUtil


class TrendingRepositoriesAdapter :
    RecyclerView.Adapter<TrendingRepositoriesAdapter.MyViewHolder>() {
    private var trendingRepositories: List<TrendingRepository> = emptyList()
    var previousExpandedPosition = -1
    var expandedPosition = -1
    val expandedPositionMutableLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData(expandedPosition)
    }

    inner class MyViewHolder(private val binding: ItemTrendingRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trendingRepositories: TrendingRepository, position: Int) {
            binding.trendingRepository = trendingRepositories
            binding.executePendingBindings()

            val isExpanded = (position == expandedPosition) // to make all items closed at first
            binding.expandableConstraintLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

            if (isExpanded)
                previousExpandedPosition = position

            binding.constraintLayout.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position // save position as expanded
                expandedPositionMutableLiveData.postValue(expandedPosition)

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


    fun addTrendingRepositoriesItem(newTrendingRepositoriesItem: List<TrendingRepository>) { // used in sorting
        val diffUtilCallback =
            RepositoriesDiffUtil(this.trendingRepositories, newTrendingRepositoriesItem)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        this.trendingRepositories = newTrendingRepositoriesItem
        result.dispatchUpdatesTo(this)
    }
    fun resetExpandingHandlers(){
        previousExpandedPosition = -1
        expandedPosition = -1
        expandedPositionMutableLiveData.postValue(expandedPosition)
    }
    fun expandSavedInstanceItem(position: Int) {
        expandedPosition = position
      //  expandedPositionMutableLiveData.postValue(expandedPosition)
    }

}

