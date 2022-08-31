package com.seif.banquemisrttask.ui.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

class TrendingRepositoriesRowBinding {
    companion object {
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl:String?){
            imageUrl?.let {
                Picasso.get().load(it).into(imageView)
            }
        }
    }
}