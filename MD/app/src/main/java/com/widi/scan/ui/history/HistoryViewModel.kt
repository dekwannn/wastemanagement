package com.widi.scan.ui.history

import androidx.lifecycle.*
import com.widi.scan.data.ScanRepository
import com.widi.scan.data.local.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: ScanRepository) : ViewModel() {

    private val _allHistory = MutableLiveData<List<HistoryEntity>>()
    val allHistory: LiveData<List<HistoryEntity>> get() = _allHistory

    fun loadAllHistory() {
        viewModelScope.launch {
            val history = repository.getAllHistory()
            _allHistory.value = history.distinctBy { it.timestamp }
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            repository.deleteAllHistory()
            loadAllHistory()
        }
    }

}

