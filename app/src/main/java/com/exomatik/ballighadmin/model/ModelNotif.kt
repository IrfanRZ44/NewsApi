package com.exomatik.ballighadmin.model

import com.exomatik.ballighadmin.services.notification.model.Notification

data class ModelNotif (
    var notification: Notification? = null,
    var id: String? = "",
    var idNotif: String = ""
)