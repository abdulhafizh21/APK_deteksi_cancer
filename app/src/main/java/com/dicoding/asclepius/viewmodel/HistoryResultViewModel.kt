package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.HistoryResultRepository
import com.dicoding.asclepius.data.db.HistoryResult
import kotlinx.coroutines.launch

class HistoryResultViewModel(private val historyResultRepository: HistoryResultRepository): ViewModel() {

    private val _historyResult = historyResultRepository.getAllHistoryResult()
    val historyResult: LiveData<List<HistoryResult>> = _historyResult

    init {
        getHistoryResult()
    }

    private fun getHistoryResult(){
        viewModelScope.launch {
            try {
                historyResultRepository.getAllHistoryResult()
            }catch (e: Exception){
                Log.d(TAG, "Gagal Mendapatkan Data: ${e.message}")
            }
        }
    }

    companion object {
        const val TAG = "HistoryResultViewModel"
    }
}