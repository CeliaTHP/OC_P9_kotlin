package com.example.oc_p9_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ItemEstateLayoutBinding
import com.example.oc_p9_kotlin.databinding.ItemPicLayoutBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.Media

class ImageAdapter(
    var imageList: MutableList<Media>
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemPicLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val media = imageList[position]

        holder.itemPicLayoutBinding.itemPicTitle.text = media.name

        Glide.with(holder.itemView.context)
            .load(media.url)
            .error(R.drawable.ic_house)
            .into(holder.itemPicLayoutBinding.itemPic)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ImageViewHolder(val itemPicLayoutBinding: ItemPicLayoutBinding) :
        RecyclerView.ViewHolder(itemPicLayoutBinding.root)
}