package com.ritika.habitualcurator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val frequency: String = "Daily",
    val streakCount: Int = 0,
    val lastCompletedDate: Long = 0L,
    val isCompletedToday: Boolean = false,
    val createdDate: Long = System.currentTimeMillis()
)