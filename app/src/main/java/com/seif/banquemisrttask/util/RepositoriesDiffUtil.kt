package com.seif.banquemisrttask.util

import androidx.recyclerview.widget.DiffUtil
import com.seif.banquemisrttask.data.network.models.TrendingRepositoriesItem

class RepositoriesDiffUtil(
    private val oldList: List<TrendingRepositoriesItem>,
    private val newList: List<TrendingRepositoriesItem>,
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }
}