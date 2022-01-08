package id.exomatik.news.utils

import id.exomatik.news.model.response.ModelResponseDaftarNews
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils{
    private val retrofitDaftarNews = Retrofit.Builder()
        .baseUrl(Constant.reffBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiDaftarNews = retrofitDaftarNews.create(RetrofitApi::class.java)

    fun getDaftarNews(uri: String, callback: Callback<ModelResponseDaftarNews>){
        val call = apiDaftarNews.getDaftarNews(uri)
        call.enqueue(callback)
    }
}