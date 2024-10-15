package com.dicoding.event.data.retrofit

import com.dicoding.event.data.response.DetailResponse
import com.dicoding.event.data.response.Event
import com.dicoding.event.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getListEvents(
        @Query("active") active: Int,
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<DetailResponse<Event>>

    @GET("events")
    fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String
    ): Call<EventResponse>
}
