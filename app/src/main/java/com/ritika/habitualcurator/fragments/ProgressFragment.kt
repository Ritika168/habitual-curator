package com.ritika.habitualcurator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ritika.habitualcurator.R
import com.ritika.habitualcurator.databinding.FragmentProgressBinding
import com.ritika.habitualcurator.viewmodel.HabitViewModel

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[HabitViewModel::class.java]

        observeStats()
    }

    private fun observeStats() {
        viewModel.totalHabitsCount.observe(viewLifecycleOwner) { count ->
            binding.tvTotalHabits.text = count?.toString() ?: "0"
        }

        viewModel.longestStreak.observe(viewLifecycleOwner) { streak ->
            binding.tvLongestStreak.text = "🔥 ${streak ?: 0}"
            updateMotivation(streak ?: 0)
        }
    }

    private fun updateMotivation(streak: Int) {
        val message = when {
            streak == 0 -> getString(R.string.motivation_start)
            streak >= 7 -> getString(R.string.motivation_consistent, streak)
            else -> getString(R.string.motivation_keep_going)
        }
        binding.tvMotivation.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}