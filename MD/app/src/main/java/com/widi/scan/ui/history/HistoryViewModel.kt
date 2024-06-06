package com.widi.scan.ui.history

import androidx.lifecycle.*
import com.widi.scan.data.ScanRepository
import com.widi.scan.data.local.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: ScanRepository) : ViewModel() {

    private val _allHistory = MutableLiveData<List<HistoryEntity>>()
    val allHistory: LiveData<List<HistoryEntity>> get() = _allHistory

    fun insert(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insert(history)
        }
    }

    fun getAllHistory() {
        viewModelScope.launch {
            _allHistory.value = repository.getAllHistory()
        }
    }

}

class HistoryViewModelFactory(private val repository: ScanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
