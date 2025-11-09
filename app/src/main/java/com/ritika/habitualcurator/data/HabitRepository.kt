package com.ritika.habitualcurator.data

import androidx.lifecycle.LiveData

class HabitRepository(private val habitDao: HabitDao) {

    val allHabits: LiveData<List<Habit>> = habitDao.getAllHabits()
    val completedHabitsToday: LiveData<List<Habit>> = habitDao.getCompletedHabitsToday()
    val totalHabitsCount: LiveData<Int> = habitDao.getTotalHabitsCount()
    val longestStreak: LiveData<Int> = habitDao.getLongestStreak()

    suspend fun insert(habit: Habit): Long {
        return habitDao.insertHabit(habit)
    }

    suspend fun update(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun delete(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun getHabitById(id: Int): Habit? {
        return habitDao.getHabitById(id)
    }

    suspend fun resetDailyCompletion() {
        habitDao.resetDailyCompletion()
    }

    suspend fun toggleHabitCompletion(habit: Habit) {
        val currentTime = System.currentTimeMillis()
        val oneDayInMillis = 24 * 60 * 60 * 1000

        if (habit.isCompletedToday) {
            val updatedHabit = habit.copy(
                isCompletedToday = false,
                streakCount = if (habit.streakCount > 0) habit.streakCount - 1 else 0
            )
            habitDao.updateHabit(updatedHabit)
        } else {
            val daysSinceLastCompleted = (currentTime - habit.lastCompletedDate) / oneDayInMillis

            val newStreak = when {
                habit.lastCompletedDate == 0L -> 1
                daysSinceLastCompleted <= 1 -> habit.streakCount + 1
                else -> 1
            }

            val updatedHabit = habit.copy(
                isCompletedToday = true,
                streakCount = newStreak,
                lastCompletedDate = currentTime
            )
            habitDao.updateHabit(updatedHabit)
        }
    }
}