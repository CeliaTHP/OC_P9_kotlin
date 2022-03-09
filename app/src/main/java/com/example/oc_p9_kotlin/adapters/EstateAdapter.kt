package com.example.oc_p9_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oc_p9_kotlin.databinding.ItemEstateLayoutBinding
import com.example.oc_p9_kotlin.models.Estate

class EstateAdapter(
    val estateList: List<Estate>,
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
        holder.itemEstateLayoutBinding.itemEstateType.text = estate.type.name.lowercase().replaceFirstChar { it.uppercase()  }

        holder.itemEstateLayoutBinding.itemEstateCity.text = estate.city
        holder.itemEstateLayoutBinding.itemEstatePrice.text = estate.priceInDollars.toString()


    }

    override fun getItemCount(): Int {
        return estateList.size
    }

    class EstateViewHolder(val itemEstateLayoutBinding: ItemEstateLayoutBinding) :
        RecyclerView.ViewHolder(itemEstateLayoutBinding.root)

}



