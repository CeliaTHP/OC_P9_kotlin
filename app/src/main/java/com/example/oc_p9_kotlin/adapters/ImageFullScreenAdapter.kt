package com.example.oc_p9_kotlin.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ItemEstateLayoutBinding
import com.example.oc_p9_kotlin.databinding.ItemPicFullscreenLayoutBinding
import com.example.oc_p9_kotlin.databinding.ItemPicLayoutBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.Media
import java.io.File

class ImageFullScreenAdapter(

    var imageList: MutableList<Media>,
) : RecyclerView.Adapter<ImageFullScreenAdapter.ImageViewHolder>() {

    companion object {
        var TAG = "ImageAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemPicFullscreenLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val media = imageList[position]

        Log.d(TAG, media.toString())

        holder.itemPicFullscreenLayoutBinding.itemPicFullscreenTitle.text = media.name

        Glide.with(holder.itemView.context)
            .load(media.url)
            .centerCrop()
            .error(R.drawable.ic_house)
            .into(holder.itemPicFullscreenLayoutBinding.itemPicFullscreen)


    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    class ImageViewHolder(val itemPicFullscreenLayoutBinding: ItemPicFullscreenLayoutBinding) :
        RecyclerView.ViewHolder(itemPicFullscreenLayoutBinding.root)
}