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

        Log.d(TAG, media.toString())

        holder.itemPicLayoutBinding.itemPicTitle.text = media.name

        Glide.with(holder.itemView.context)
            .load(media.uri)
            .centerCrop()
            .placeholder(R.drawable.ic_house)
            .error(R.drawable.ic_house)
            .into(holder.itemPicLayoutBinding.itemPic)



        holder.itemView.setOnLongClickListener {
            Log.d(TAG, "longClick")
            onLongClick()
            true
        }


        if (isEditing) {
            Log.d(TAG, "is Editing")
            holder.itemPicLayoutBinding.addEstateDelete.visibility = View.VISIBLE


            holder.itemPicLayoutBinding.addEstateDelete.setOnClickListener {
                removeData(position)
            }

        } else {
            Log.d(TAG, "is NOT Editing")
            holder.itemPicLayoutBinding.addEstateDelete.visibility = View.GONE


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
        onDataUpdate()

    }

    public fun removeData(position: Int) {
        this.imageList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        Log.d(TAG, imageList.toString())
        onDataUpdate()

    }

    class ImageViewHolder(val itemPicLayoutBinding: ItemPicLayoutBinding) :
        RecyclerView.ViewHolder(itemPicLayoutBinding.root)
}