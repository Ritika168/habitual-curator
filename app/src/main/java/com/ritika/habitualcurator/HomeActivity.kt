package com.ritika.habitualcurator


import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.ritika.habitualcurator.adapters.HabitAdapter
import com.ritika.habitualcurator.data.Habit
import com.ritika.habitualcurator.databinding.ActivityHomeBinding
import com.ritika.habitualcurator.databinding.DialogAddHabitBinding
import com.ritika.habitualcurator.fragments.AISuggestionsFragment
import com.ritika.habitualcurator.fragments.ProgressFragment
import com.ritika.habitualcurator.viewmodel.HabitViewModel
import java.util.Calendar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HabitViewModel
    private lateinit var habitAdapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        updateGreeting()

        viewModel = ViewModelProvider(this)[HabitViewModel::class.java]

        setupRecyclerView()
        observeHabits()
        setupFAB()
        setupBottomNavigation()
    }

    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 0..11 -> getString(R.string.greeting_morning)
            in 12..16 -> getString(R.string.greeting_afternoon)
            else -> getString(R.string.greeting_evening)
        }
        binding.toolbar.title = greeting
    }

    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            onHabitClick = { habit ->
                // Handle habit click (edit)
            },
            onHabitChecked = { habit ->
                viewModel.toggleHabitCompletion(habit)
                if (!habit.isCompletedToday) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.habit_completed),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            },
            onHabitLongClick = { habit ->
                showDeleteDialog(habit)
            }
        )

        binding.rvHabits.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = habitAdapter
        }
    }

    private fun observeHabits() {
        viewModel.allHabits.observe(this) { habits ->
            habitAdapter.submitList(habits)
            binding.tvEmptyState.visibility = if (habits.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupFAB() {
        binding.fabAddHabit.setOnClickListener {
            showAddHabitDialog()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Show main content, hide fragment container
                    binding.mainContent.visibility = View.VISIBLE
                    binding.fragmentContainer.visibility = View.GONE
                    binding.fabAddHabit.show()

                    // Clear back stack
                    supportFragmentManager.popBackStack(
                        null,
                        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    true
                }
                R.id.nav_suggestions -> {
                    // Hide main content, show fragment
                    binding.mainContent.visibility = View.GONE
                    binding.fragmentContainer.visibility = View.VISIBLE
                    binding.fabAddHabit.hide()

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, AISuggestionsFragment())
                        .commit()
                    true
                }
                R.id.nav_progress -> {
                    // Hide main content, show fragment
                    binding.mainContent.visibility = View.GONE
                    binding.fragmentContainer.visibility = View.VISIBLE
                    binding.fabAddHabit.hide()

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProgressFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    private fun showAddHabitDialog() {
        val dialogBinding = DialogAddHabitBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnSave.setOnClickListener {
            val title = dialogBinding.etHabitName.text.toString().trim()
            val description = dialogBinding.etDescription.text.toString().trim()
            val frequency = if (dialogBinding.rbDaily.isChecked) "Daily" else "Weekly"

            if (title.isNotEmpty()) {
                val habit = Habit(
                    title = title,
                    description = description,
                    frequency = frequency
                )
                viewModel.insertHabit(habit)

                Snackbar.make(
                    binding.root,
                    getString(R.string.habit_added),
                    Snackbar.LENGTH_SHORT
                ).show()

                dialog.dismiss()
            } else {
                dialogBinding.etHabitName.error = "Please enter a habit name"
            }
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteDialog(habit: Habit) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Habit")
            .setMessage("Are you sure you want to delete '${habit.title}'?")
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteHabit(habit)
                Snackbar.make(
                    binding.root,
                    getString(R.string.habit_deleted),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.undo)) {
                    viewModel.insertHabit(habit)
                }.show()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // TODO: Open settings
                true
            }
            R.id.action_delete_all -> {
                // TODO: Delete all habits
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}