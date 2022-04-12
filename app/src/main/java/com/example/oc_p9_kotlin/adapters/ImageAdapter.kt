package com.example.oc_p9_kotlin.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ItemEstateLayoutBinding
import com.example.oc_p9_kotlin.databinding.ItemPicLayoutBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.Media
import java.io.File

class ImageAdapter(

    var imageList: MutableList<Media>

) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    companion object {
        var TAG = "ImageAdapter"
    }

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

        if (media.isLocal) {
            Glide.with(holder.itemView.context)
                .load(File(media.url)) // Uri of the picture
                .into(holder.itemPicLayoutBinding.itemPic)

        } else {
            Glide.with(holder.itemView.context)
                .load(media.url)
                .centerCrop()
                .error(R.drawable.ic_house)
                .into(holder.itemPicLayoutBinding.itemPic)
        }


    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    public fun updateData(newList: MutableList<Media>) {
        Log.d(TAG, "old list : " + imageList.size)
        this.imageList = newList
        notifyDataSetChanged()
        Log.d(TAG, "new list : " + newList.size)

    }

    public fun addData(media: Media) {
        this.imageList.add(media)
        notifyItemInserted(imageList.size)
        Log.d(TAG, imageList.toString())


    }

    class ImageViewHolder(val itemPicLayoutBinding: ItemPicLayoutBinding) :
        RecyclerView.ViewHolder(itemPicLayoutBinding.root)
}