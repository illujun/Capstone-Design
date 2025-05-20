package com.example.findpill.data.repository

import android.content.Context
import android.util.Log
import com.example.findpill.data.model.UploadResponse
import com.example.findpill.ui.utils.ChangeImagePath
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class UploadImage(private val context: Context){
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://beatmania.app:1321/") // 서버 연결
        //.baseUrl("http://210.217.79.69:1321/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ImageApi::class.java)

    suspend fun upload(pill1: String, pill2: String): Boolean? {
        val front = ChangeImagePath(context, pill1, "front")
        val back = ChangeImagePath(context, pill2, "back")
        // 앨범에서 고를때랑 사진 촬영할 때랑 경로가 달라서 이를 적절히 매핑


        return try{
            val response: Response<ResponseBody> = api.uploadImage(front, back)

            Log.d("UploadImage", "Response code: ${response.code()}")
            Log.d("UploadImage", "Response successful: ${response.isSuccessful}")
            if(response.isSuccessful){
                Log.d("UploadImage", "Response body: ${response.body()}")
                response.isSuccessful
            }else{
                Log.e("UploadImage", "Upload failed: ${response.errorBody()?.string()}")
                false
            }
        }catch(e:Exception){
            Log.e("UploadImage", "api연결 오류", e)
            false
        }

    }
}