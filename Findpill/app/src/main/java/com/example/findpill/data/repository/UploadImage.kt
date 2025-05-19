package com.example.findpill.data.repository

import android.content.Context
import com.example.findpill.data.model.UploadResponse
import com.example.findpill.ui.utils.ChangeImagePath
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UploadImage(private val context: Context){
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://server.com:1321/") // 서버 연결
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ImageApi::class.java)

    suspend fun upload(pill1: String, pill2: String): UploadResponse? {
        val front = ChangeImagePath(context, pill1, "front")
        val back = ChangeImagePath(context, pill2, "back")
        // 앨범에서 고를때랑 사진 촬영할 때랑 경로가 달라서 이를 적절히 매핑
        val response = api.uploadImage(front, back)
        return if(response.isSuccessful){
            response.body()
        }else{
            null
        }
    }
}