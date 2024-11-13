package com.dicoding.asclepius.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.db.HistoryResult
import com.dicoding.asclepius.databinding.HistoryRowBinding
import com.dicoding.asclepius.view.ResultActivity
import com.dicoding.asclepius.viewmodel.ResultViewModel

class HistoryResultAdapter: ListAdapter<HistoryResult, HistoryResultAdapter.MyViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = HistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, ResultActivity::class.java)

            intentDetail.putExtra(ResultActivity.EXTRA_IMAGE_URI, history.image)
            intentDetail.putExtra(ResultViewModel.KEY_LABEL, history.label)
            intentDetail.putExtra(ResultViewModel.KEY_SCORE, history.score)

            holder.itemView.context.startActivity(intentDetail)
        }

    }

    class MyViewHolder(private val binding: HistoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyResult: HistoryResult) {
            binding.tvLabel.text = historyResult.label
            binding.tvScore.text = historyResult.score
            Glide.with(itemView.context)
                .load(historyResult.image)
                .into(binding.ivResult)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryResult>() {
            override fun areItemsTheSame(oldItem: HistoryResult, newItem: HistoryResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryResult, newItem: HistoryResult): Boolean {
                return oldItem == newItem
            }
        }
    }

}