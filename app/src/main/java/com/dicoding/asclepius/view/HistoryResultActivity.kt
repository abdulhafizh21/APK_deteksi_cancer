package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryResultAdapter
import com.dicoding.asclepius.data.db.HistoryResult
import com.dicoding.asclepius.data.HistoryResultRepository
import com.dicoding.asclepius.databinding.ActivityHistoryResultBinding
import com.dicoding.asclepius.helper.ViewModelFactory
import com.dicoding.asclepius.viewmodel.HistoryResultViewModel
import com.dicoding.asclepius.viewmodel.ResultViewModel

@Suppress("DEPRECATION")
class HistoryResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryResultBinding
    private lateinit var historyResultViewModel: HistoryResultViewModel
    private lateinit var historyResultRepository: HistoryResultRepository
    private val adapter = HistoryResultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyResultRepository = HistoryResultRepository(application)
        historyResultViewModel = obtainViewModel(this@HistoryResultActivity)

        setTitle()
        setRecyclerView()

        historyResultViewModel.historyResult.observe(this){ history ->
            val items = arrayListOf<HistoryResult>()
            if (history.isNotEmpty()){
                history.map { historyResult ->
                    val item = HistoryResult(image = historyResult.image, label = historyResult.label, score = historyResult.score)
                    items.add(item)
                }
                adapter.submitList(items)
                binding.rvHistoryResult.adapter = adapter
            }
            else {
                binding.rvHistoryResult.visibility = View.GONE
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.tvEmptyMessage.text = getString(R.string.empty_message)
            }
        }
    }

    private fun setTitle(){
        val title = getString(R.string.analysis_history)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun setRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvHistoryResult.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHistoryResult.addItemDecoration(itemDecoration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryResultViewModel {
        historyResultRepository = HistoryResultRepository(application)
        val factory = ViewModelFactory.getInstance(historyResultRepository)
        return ViewModelProvider(activity, factory)[HistoryResultViewModel::class.java]
    }
}