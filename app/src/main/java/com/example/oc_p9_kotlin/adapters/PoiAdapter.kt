package com.example.oc_p9_kotlin.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ItemPoisBinding
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.Media

class PoiAdapter(

    var poiList: MutableList<FakePOI>,
    private var isEditing: Boolean = false,
    private var onDataUpdate: () -> Unit,

    ) : RecyclerView.Adapter<PoiAdapter.PoiViewHolder>() {

    companion object {
        var TAG = "PoiAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        return PoiViewHolder(
            ItemPoisBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {

        val poi = poiList[position]

        holder.itemPoisBinding.itemPoiType.text =
            holder.itemView.context.getString(poi.poiType.stringValue)
        holder.itemPoisBinding.itemPoiName.text = poi.name
        holder.itemPoisBinding.itemPoiDescription.editText?.setText(poi.description)
        holder.itemPoisBinding.itemPoiDescription.editText?.isFocusable = false
        holder.itemPoisBinding.itemPoiLocation.text = holder.itemView.context.getString(
            R.string.add_estate_location, poi.latitude.toString(), poi.longitude.toString()
        )

        if (isEditing) {
            holder.itemPoisBinding.addPoiDelete.visibility = View.VISIBLE
            holder.itemPoisBinding.addPoiDelete.setOnClickListener {
                removeData(position)
            }

        } else {
            holder.itemPoisBinding.addPoiDelete.visibility = View.GONE

        }


    }

    override fun getItemCount(): Int {
        return poiList.size
    }


    public fun updateData(newList: MutableList<FakePOI>) {
        this.poiList = newList
        notifyDataSetChanged()
    }

    public fun addData(poi: FakePOI) {
        this.poiList.add(poi)
        notifyItemInserted(poiList.size)
        onDataUpdate()
    }

    public fun removeData(position: Int) {
        this.poiList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        onDataUpdate()

    }

    class PoiViewHolder(val itemPoisBinding: ItemPoisBinding) :
        RecyclerView.ViewHolder(itemPoisBinding.root)
}