package com.example.chatmessenger.modal
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.messaging.Constants
import java.util.*
class ImageMessage(
    val sender: String? = "",
    val receiver: String? = "",
    val message: String? = "",
    val time: String? = "",
    val type: String? = ""

    ) {

    val id : String get() = "$sender-$receiver-$message-$time-$type"


}

