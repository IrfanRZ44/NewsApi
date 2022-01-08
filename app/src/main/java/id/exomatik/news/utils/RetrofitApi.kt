package id.exomatik.news.utils

import id.exomatik.news.model.response.*
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by IrfanRZ on 02/08/2019.
 */
interface RetrofitApi {
    @Headers("Accept:application/json")
    @GET
    fun getDaftarNews(@Url url: String?): Call<ModelResponseDaftarNews>
}