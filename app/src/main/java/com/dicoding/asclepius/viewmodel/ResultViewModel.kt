package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.HistoryResultRepository
import com.dicoding.asclepius.data.db.HistoryResult
import kotlinx.coroutines.launch

class ResultViewModel (private val historyResultRepository: HistoryResultRepository): ViewModel(){

    fun insertBookmarkResult(historyResult: HistoryResult){
        viewModelScope.launch {
            historyResultRepository.insert(historyResult)
        }
    }

    companion object{
        const val KEY_LABEL = "key_label"
        const val KEY_SCORE = "key_score"
    }
}