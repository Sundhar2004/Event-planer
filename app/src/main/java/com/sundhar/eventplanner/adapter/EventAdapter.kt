package com.sundhar.eventplanner.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sundhar.eventplanner.databinding.EventListItemBinding
import com.sundhar.eventplanner.model.EventPlanerModel

class EventAdapter(private val onClick: (EventPlanerModel) -> Unit): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private var eventList = listOf<EventPlanerModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<EventPlanerModel>) {
        eventList = list
        notifyDataSetChanged()
    }

    inner class EventViewHolder(val binding: EventListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventPlanerModel) {
            binding.title.text = event.title
            binding.time.text = event.time

            binding.root.setOnClickListener {
                onClick(event)
            }
        }

    }
}