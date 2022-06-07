package com.example.oc_p9_kotlin.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ItemPicLayoutBinding
import com.example.oc_p9_kotlin.models.Media

class ImageAdapter(
    var imageList: MutableList<Media>,
    private var isEditing: Boolean = false,
    private var onDataUpdate: () -> Unit,
    private var onLongClick: () -> Unit
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

        Glide.with(holder.itemView.context)
            .load(media.uri)
            .centerCrop()
            .placeholder(R.drawable.ic_house)
            .error(R.drawable.ic_house)
            .into(holder.itemPicLayoutBinding.itemPic)

        holder.itemView.setOnLongClickListener {
            onLongClick()
            true
        }

        if (isEditing) {
            holder.itemPicLayoutBinding.addEstateDelete.visibility = View.VISIBLE
            holder.itemPicLayoutBinding.addEstateDelete.setOnClickListener {
                removeData(position)
            }

        } else {
            holder.itemPicLayoutBinding.addEstateDelete.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


     fun updateData(newList: MutableList<Media>) {
        this.imageList = newList
        notifyDataSetChanged()
    }

     fun addData(media: Media) {
        this.imageList.add(media)
        notifyItemInserted(imageList.size)
        Log.d(TAG, imageList.toString())
        onDataUpdate()

    }

    private fun removeData(position: Int) {
        this.imageList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        Log.d(TAG, imageList.toString())
        onDataUpdate()

    }

    class ImageViewHolder(val itemPicLayoutBinding: ItemPicLayoutBinding) :
        RecyclerView.ViewHolder(itemPicLayoutBinding.root)
}