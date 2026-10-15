package com.parentguard.ui.parent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parentguard.data.SmsMessageItem
import com.parentguard.databinding.ItemMessageBinding
import java.text.DateFormat
import java.util.Date

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private val items = mutableListOf<SmsMessageItem>()

    fun submitList(newItems: List<SmsMessageItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    class ViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SmsMessageItem) {
            binding.contact.text = item.sender
            binding.content.text = item.body
            binding.time.text = DateFormat.getDateTimeInstance().format(Date(item.timestampMillis))
        }
    }
}
