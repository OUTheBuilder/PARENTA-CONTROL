package com.parentguard.ui.parent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parentguard.data.GeoZone
import com.parentguard.databinding.ItemZoneBinding

class ZoneAdapter : RecyclerView.Adapter<ZoneAdapter.ViewHolder>() {

    private val items = mutableListOf<GeoZone>()

    fun submitList(newItems: List<GeoZone>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemZoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    class ViewHolder(private val binding: ItemZoneBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GeoZone) {
            binding.zoneName.text = item.name
            binding.zoneRadius.text = "Radius: ${item.radiusMeters.toInt()}m"
        }
    }
}
