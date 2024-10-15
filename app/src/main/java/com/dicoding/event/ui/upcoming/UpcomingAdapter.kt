package com.dicoding.event.ui.upcoming

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.dicoding.event.ui.Adapter
import com.dicoding.event.data.response.ListEventsItem
import com.dicoding.event.databinding.ItemEventBinding

class UpcomingAdapter(override val DIFF_CALLBACK: DiffUtil.ItemCallback<ListEventsItem>) : Adapter<ListEventsItem>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun bind(binding: ItemEventBinding, item: ListEventsItem) {
        binding.eventTitle.text = item.name
        Glide.with(binding.root.context).load(item.imageLogo).into(binding.eventImage)

        binding.root.setOnClickListener{
            onItemClickListener?.invoke(item)
        }
    }

}