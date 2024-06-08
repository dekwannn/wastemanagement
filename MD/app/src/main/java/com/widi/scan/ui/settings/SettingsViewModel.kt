package com.widi.scan.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.widi.scan.data.ScanRepository
import com.widi.scan.data.local.HistoryEntity

class SettingsViewModel (private val repository: ScanRepository) : ViewModel(){
    private val _allHistory = MutableLiveData<List<HistoryEntity>>()
    val allHistory: MutableLiveData<List<HistoryEntity>> get() = _allHistory

    fun clearHistory() {
        _allHistory.postValue(emptyList())
    }

}