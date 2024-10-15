package com.dicoding.event.ui



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.event.data.response.ListEventsItem
import com.dicoding.event.databinding.ItemEventBinding

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private val events: MutableList<ListEventsItem> = mutableListOf()

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.eventTitle.text = event.name
            Glide.with(binding.root.context).load(event.imageLogo).into(binding.eventImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun setEvents(newEvents: List<ListEventsItem?>?) {
        events.clear()
        newEvents?.let {
            it.filterNotNull().let { filteredEvents ->
                events.addAll(filteredEvents)
            }
        }
        notifyDataSetChanged()
    }

}
