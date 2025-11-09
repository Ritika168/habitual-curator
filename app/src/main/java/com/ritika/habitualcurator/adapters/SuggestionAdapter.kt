package com.ritika.habitualcurator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ritika.habitualcurator.databinding.ItemSuggestionBinding
import com.ritika.habitualcurator.fragments.HabitSuggestion

class SuggestionAdapter(
    private val onAddClick: (HabitSuggestion) -> Unit
) : ListAdapter<HabitSuggestion, SuggestionAdapter.SuggestionViewHolder>(SuggestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val binding = ItemSuggestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SuggestionViewHolder(
        private val binding: ItemSuggestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: HabitSuggestion) {
            binding.apply {
                tvSuggestionTitle.text = suggestion.title
                tvSuggestionDescription.text = suggestion.description

                btnAddToHabits.setOnClickListener {
                    onAddClick(suggestion)
                }
            }
        }
    }

    class SuggestionDiffCallback : DiffUtil.ItemCallback<HabitSuggestion>() {
        override fun areItemsTheSame(oldItem: HabitSuggestion, newItem: HabitSuggestion): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HabitSuggestion, newItem: HabitSuggestion): Boolean {
            return oldItem == newItem
        }
    }
}