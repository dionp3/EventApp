package com.dicoding.event.ui.Finished

import com.dicoding.event.data.response.DetailResponse
import com.dicoding.event.data.response.Event
import com.dicoding.event.data.response.EventResponse
import com.dicoding.event.data.response.ListEventsItem
import com.dicoding.event.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FinishedViewModel : ViewModel() {
    private val _finished = MutableLiveData<List<ListEventsItem>?>()
    val finished: LiveData<List<ListEventsItem>?> = _finished

    private val _detailFinished = MutableLiveData<Event?>()
    val detailFinished: LiveData<Event?> = _detailFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _error

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    companion object {
        private const val TAG = "FinishedViewModel"
        private const val EVENT_ID = 0
    }

    init {
        listFinishedEvents()
    }

    private fun listFinishedEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListEvents(EVENT_ID)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _isLoading.value = false
                        _finished.value = it.listEvents as List<ListEventsItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.value = true
                    _message.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
                _error.value = true
                if (t is java.net.UnknownHostException) {
                    _message.value = "Tidak ada koneksi internet"
                } else {
                    _message.value = "Error: ${t.message}"
                }
            }
        })
    }

    fun detailFinishedEvents(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<DetailResponse<Event>> {
            override fun onResponse(call: Call<DetailResponse<Event>>, response: Response<DetailResponse<Event>>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _detailFinished.value = response.body()?.event
                } else {
                    _isLoading.value = false
                    _error.value = true
                    _message.value = "Error: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse<Event>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                _message.value = "Error: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }}