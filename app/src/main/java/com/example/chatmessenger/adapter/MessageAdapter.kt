package com.example.chatmessenger.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatmessenger.R
import com.example.chatmessenger.Utils
import com.example.chatmessenger.modal.Messages
import com.squareup.picasso.Picasso


class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfMessage = listOf<Messages>()

    companion object {
        private const val MESSAGE_LEFT = 0
        private const val MESSAGE_RIGHT = 1
        private const val MESSAGE_LEFT_IMAGE = 2
        private const val MESSAGE_RIGHT_IMAGE = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MESSAGE_RIGHT -> {
                val view = inflater.inflate(R.layout.chatitemright, parent, false)
                MessageHolder(view)
            }
            MESSAGE_LEFT -> {
                val view = inflater.inflate(R.layout.chatitemleft, parent, false)
                MessageHolder(view)
            }
            MESSAGE_RIGHT_IMAGE -> {
                val view = inflater.inflate(R.layout.chatitemrightimage, parent, false)
                MessageImageHolder(view)
            }
            MESSAGE_LEFT_IMAGE -> {
                val view = inflater.inflate(R.layout.chatitemleftimage, parent, false)
                MessageImageHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun getItemCount() = listOfMessage.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = listOfMessage[position]
        when (holder) {
            is MessageHolder -> holder.bind(message)
            is MessageImageHolder -> holder.bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            listOfMessage[position].sender == Utils.getUidLoggedIn() && listOfMessage[position].type == "image" -> MESSAGE_RIGHT_IMAGE
            listOfMessage[position].sender == Utils.getUidLoggedIn() -> MESSAGE_RIGHT
            listOfMessage[position].type == "image" -> MESSAGE_LEFT_IMAGE
            else -> MESSAGE_LEFT
        }
    }

    fun setList(newList: List<Messages>) {
        this.listOfMessage = newList
        notifyDataSetChanged()
    }

    inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.show_message)
        private val timeOfSent: TextView = itemView.findViewById(R.id.timeView)

        fun bind(message: Messages) {
            messageText.text = message.message
            timeOfSent.text = message.time?.substring(0, 5) ?: ""
        }
    }

//    inner class MessageImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val imageViewMessage: ImageView = itemView.findViewById(R.id.imageView_message_image)
//        private val timeOfSent: TextView = itemView.findViewById(R.id.textView_message_time)
//
//        fun bind(message: Messages) {
//            // Assuming the layout for messages with images contains ImageView for the image
//            // Set your image resource or image URL here
//            timeOfSent.text = message.time?.substring(0, 5) ?: ""
//        }
//    }
inner class MessageImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageViewMessage: ImageView = itemView.findViewById(R.id.imageView_message_image)
    private val timeOfSent: TextView = itemView.findViewById(R.id.textView_message_time)

    fun bind(message: Messages) {
        // Load image using Picasso
        message.message?.let { imageUrl ->
            Picasso.get()
                .load(imageUrl)
                .into(imageViewMessage)
        }

        // Set time of sent
        timeOfSent.text = message.time?.substring(0, 5) ?: ""
    }
}


}
