package com.ritika.habitualcurator.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habits ORDER BY createdDate DESC")
    fun getAllHabits(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Int): Habit?

    @Query("SELECT * FROM habits WHERE isCompletedToday = 1")
    fun getCompletedHabitsToday(): LiveData<List<Habit>>

    @Query("UPDATE habits SET isCompletedToday = 0")
    suspend fun resetDailyCompletion()

    @Query("SELECT COUNT(*) FROM habits")
    fun getTotalHabitsCount(): LiveData<Int>

    @Query("SELECT MAX(streakCount) FROM habits")
    fun getLongestStreak(): LiveData<Int>

    @Query("DELETE FROM habits")
    suspend fun deleteAllHabits()
}