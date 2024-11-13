package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import com.dicoding.asclepius.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    private val _listNews = MutableLiveData<List<ArticlesItem>?>()
    val listNews : MutableLiveData<List<ArticlesItem>?> = _listNews

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading

    init {
        getListNews()
    }
    private fun getListNews(){
        _showLoading.value = true
        val client = ApiConfig.getApiService().getNews(NEWS_ID)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listNews.value = responseBody.articles as List<ArticlesItem>?
                    }
                } else {
                    _showLoading.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _showLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "NewsActivity"
        private const val NEWS_ID = "cancer"
    }
}