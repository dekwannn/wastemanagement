package com.widi.scan.data

import com.widi.scan.data.local.HistoryDao
import com.widi.scan.data.local.HistoryEntity

class ScanRepository(private val historyDao: HistoryDao) {

    suspend fun insert(history: HistoryEntity) {
        historyDao.insert(history)
    }

    suspend fun getAllHistory(): List<HistoryEntity> {
        return historyDao.getAllHistory()
    }
}
