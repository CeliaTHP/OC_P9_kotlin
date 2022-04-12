package com.example.oc_p9_kotlin.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ItemEstateLayoutBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.utils.Utils
import java.text.DecimalFormat
import java.text.NumberFormat


class EstateAdapter(
    var estateList: MutableList<Estate>,
    val onClick: (Estate) -> Unit
) : RecyclerView.Adapter<EstateAdapter.EstateViewHolder>() {

    companion object {
        const val TAG = "EstateAdapter"
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EstateViewHolder {


        return EstateViewHolder(
            ItemEstateLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: EstateViewHolder,
        position: Int
    ) {

        val estate = estateList[position]

        holder.itemView.setOnClickListener {
            onClick(estate)
        }



        holder.itemEstateLayoutBinding.itemEstateType.text =
            Estate.getEstateType(holder.itemView.context, estate.type)

        holder.itemEstateLayoutBinding.itemEstateCity.text = estate.city

        holder.itemEstateLayoutBinding.itemEstatePrice.text =
            Utils().getPrice(estate)

        if (!estate.medias.isNullOrEmpty()) {
            val mediaUrl = estate.medias!![0].url
            Log.d(TAG, mediaUrl)

            Glide
                .with(holder.itemView.context)
                .load(mediaUrl)
                .error(R.drawable.ic_house)
                .centerCrop()
                .into(holder.itemEstateLayoutBinding.itemEstatePic)

        }

        if (!estate.isAvailable) {
            holder.itemEstateLayoutBinding.itemEstateSold.visibility = View.VISIBLE
            holder.itemEstateLayoutBinding.itemEstatePic.alpha = 0.5f
        } else {
            holder.itemEstateLayoutBinding.itemEstatePic.alpha = 1f
            holder.itemEstateLayoutBinding.itemEstateSold.visibility = View.INVISIBLE
        }


    }


    override fun getItemCount(): Int {
        return estateList.size
    }

    fun setFilter(estateType: EstateType) {

        var filteredList = estateList.filter {
            it.type == estateType
        } as MutableList
        Log.d(TAG, "filteredList : " + filteredList.toString())

        updateData(filteredList)

    }

    public fun updateData(newList: MutableList<Estate>) {
        Log.d(TAG, "old list : " + estateList.toString())
        this.estateList = newList
        notifyDataSetChanged()
        Log.d(TAG, "new list : " + newList.toString())
    }

    class EstateViewHolder(val itemEstateLayoutBinding: ItemEstateLayoutBinding) :
        RecyclerView.ViewHolder(itemEstateLayoutBinding.root)

}




