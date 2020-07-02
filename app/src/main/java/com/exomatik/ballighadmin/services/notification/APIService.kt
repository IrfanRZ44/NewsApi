package com.exomatik.ballighadmin.services.notification

import com.exomatik.ballighadmin.services.notification.model.MyResponse
import com.exomatik.ballighadmin.services.notification.model.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers("Content-Type:application/json", "Authorization:key=AAAADWn3rmg:APA91bGkEe4Fms0ea3A6jJOF5ekuONvZTlS15c9rI-g9Abkuj1kTXHFvEPXKOCJk7DIX25DTYReXBNmzu975q6lyyCxZ1kPpDR6wVkq8KzPRUF55iot3ldgDgExkfJdU5hXQLmFMy109")
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender?): Call<MyResponse?>?
}