package com.ritika.habitualcurator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ritika.habitualcurator.data.Habit
import com.ritika.habitualcurator.data.HabitDatabase
import com.ritika.habitualcurator.data.HabitRepository
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository
    val allHabits: LiveData<List<Habit>>
    val completedHabitsToday: LiveData<List<Habit>>
    val totalHabitsCount: LiveData<Int>
    val longestStreak: LiveData<Int>

    init {
        val habitDao = HabitDatabase.getDatabase(application).habitDao()
        repository = HabitRepository(habitDao)
        allHabits = repository.allHabits
        completedHabitsToday = repository.completedHabitsToday
        totalHabitsCount = repository.totalHabitsCount
        longestStreak = repository.longestStreak
    }

    fun insertHabit(habit: Habit) = viewModelScope.launch {
        repository.insert(habit)
    }

    fun updateHabit(habit: Habit) = viewModelScope.launch {
        repository.update(habit)
    }

    fun deleteHabit(habit: Habit) = viewModelScope.launch {
        repository.delete(habit)
    }

    fun toggleHabitCompletion(habit: Habit) = viewModelScope.launch {
        repository.toggleHabitCompletion(habit)
    }

    fun resetDailyCompletion() = viewModelScope.launch {
        repository.resetDailyCompletion()
    }
}