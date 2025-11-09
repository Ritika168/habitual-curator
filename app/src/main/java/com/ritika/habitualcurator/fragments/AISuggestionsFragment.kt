package com.ritika.habitualcurator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ritika.habitualcurator.R
import com.ritika.habitualcurator.adapters.SuggestionAdapter
import com.ritika.habitualcurator.data.Habit
import com.ritika.habitualcurator.databinding.FragmentAiSuggestionsBinding
import com.ritika.habitualcurator.viewmodel.HabitViewModel

data class HabitSuggestion(
    val title: String,
    val description: String
)

class AISuggestionsFragment : Fragment() {

    private var _binding: FragmentAiSuggestionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HabitViewModel
    private lateinit var suggestionAdapter: SuggestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiSuggestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[HabitViewModel::class.java]

        setupRecyclerView()
        setupButton()
    }

    private fun setupRecyclerView() {
        suggestionAdapter = SuggestionAdapter { suggestion ->
            addSuggestionToHabits(suggestion)
        }

        binding.rvSuggestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = suggestionAdapter
        }
    }

    private fun setupButton() {
        binding.btnGetSuggestions.setOnClickListener {
            loadSuggestions()
        }
    }

    private fun loadSuggestions() {
        // Simulated AI suggestions
        val suggestions = listOf(
            HabitSuggestion(
                "Stand on one leg while brushing teeth",
                "Improves balance and coordination"
            ),
            HabitSuggestion(
                "Take 3 deep breaths before meals",
                "Promotes mindfulness and better digestion"
            ),
            HabitSuggestion(
                "Write one sentence in a journal",
                "Cultivates gratitude and self-reflection"
            ),
            HabitSuggestion(
                "Do 5 push-ups every hour",
                "Builds strength throughout the day"
            ),
            HabitSuggestion(
                "Drink a glass of water upon waking",
                "Hydrates body and kickstarts metabolism"
            ),
            HabitSuggestion(
                "Send a gratitude text every Thursday",
                "Strengthens relationships and spreads positivity"
            ),
            HabitSuggestion(
                "Take a 3-minute doodle break after lunch",
                "Boosts creativity and mental clarity"
            ),
            HabitSuggestion(
                "Stretch for 2 minutes before bed",
                "Improves flexibility and sleep quality"
            )
        )

        suggestionAdapter.submitList(suggestions)
    }

    private fun addSuggestionToHabits(suggestion: HabitSuggestion) {
        val habit = Habit(
            title = suggestion.title,
            description = suggestion.description,
            frequency = "Daily"
        )

        viewModel.insertHabit(habit)

        Snackbar.make(
            binding.root,
            getString(R.string.habit_added),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
