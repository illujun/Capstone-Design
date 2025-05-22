package com.example.findpill.data.api

import com.example.findpill.data.model.PillInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPillByIdApi{
    @GET("api/pill/getpill")
    suspend fun getPillById(@Path("id") id:Int): PillInfo?
}