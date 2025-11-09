package com.ritika.habitualcurator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ritika.habitualcurator.data.Habit
import com.ritika.habitualcurator.databinding.ItemHabitBinding

class HabitAdapter(
    private val onHabitClick: (Habit) -> Unit,
    private val onHabitChecked: (Habit) -> Unit,
    private val onHabitLongClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitViewHolder>(HabitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HabitViewHolder(
        private val binding: ItemHabitBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.apply {
                tvHabitTitle.text = habit.title
                tvHabitDescription.text = habit.description
                tvStreak.text = "🔥 ${habit.streakCount}"
                cbComplete.isChecked = habit.isCompletedToday

                cbComplete.setOnCheckedChangeListener { _, _ ->
                    onHabitChecked(habit)
                }

                root.setOnClickListener {
                    onHabitClick(habit)
                }

                root.setOnLongClickListener {
                    onHabitLongClick(habit)
                    true
                }
            }
        }
    }

    class HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }
    }
}