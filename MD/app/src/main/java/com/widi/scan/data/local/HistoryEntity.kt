package com.widi.scan.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey val id: String,
    val imageUri: String,
    val label: String,
    val timestamp: Long,
    val confidence: Int
)

