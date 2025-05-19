package com.example.findpill.data.repository

import com.example.findpill.data.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi{
    @Multipart
    @POST("/upload")
    suspend fun uploadImage(
        @Part front: MultipartBody.Part,
        @Part back: MultipartBody.Part
    ): Response<UploadResponse>
}