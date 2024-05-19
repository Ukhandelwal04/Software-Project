package com.example.chatmessenger.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatmessenger.R
import com.example.chatmessenger.Utils
import com.example.chatmessenger.adapter.MessageAdapter
import com.example.chatmessenger.databinding.FragmentChatfromHomeBinding
import com.example.chatmessenger.modal.Messages
import com.example.chatmessenger.mvvm.ChatAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.util.UUID

@Suppress("DEPRECATION")
class ChatfromHome : Fragment() {



    lateinit var args : ChatfromHomeArgs
    lateinit var binding: FragmentChatfromHomeBinding
    lateinit var viewModel : ChatAppViewModel
    lateinit var toolbar: Toolbar
    lateinit var adapter : MessageAdapter
    lateinit var bitmap: Bitmap
    private lateinit var storageRef: StorageReference
    var uri: Uri? = null
    lateinit var storage: FirebaseStorage
    val firestore = FirebaseFirestore.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chatfrom_home, container, false)


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar = view.findViewById(R.id.toolBarChat)
        val circleImageView = toolbar.findViewById<CircleImageView>(R.id.chatImageViewUser)
        val textViewName = toolbar.findViewById<TextView>(R.id.chatUserName)
        val textViewStatus = view.findViewById<TextView>(R.id.chatUserStatus)


        args = ChatfromHomeArgs.fromBundle(requireArguments())


        viewModel = ViewModelProvider(this).get(ChatAppViewModel::class.java)


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference



        Glide.with(view.getContext()).load(args.recentchats.friendsimage!!).placeholder(R.drawable.person).dontAnimate().into(circleImageView);
        textViewName.setText(args.recentchats.name)
        //textViewStatus.setText(args.users.status)


        binding.fabsendimage.setOnClickListener {

            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose your profile picture")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> {

                        takePhotoWithCamera()


                    }
                    options[item] == "Choose from Gallery" -> {
                        pickImageFromGallery()
                    }
                    options[item] == "Cancel" -> dialog.dismiss()
                }
            }
            builder.show()






        }



        binding.chatBackBtn.setOnClickListener {


            view.findNavController().navigate(R.id.action_chatfromHome_to_homeFragment)

        }

        binding.sendBtn.setOnClickListener {


            viewModel.sendMessage(Utils.getUidLoggedIn(), args.recentchats.friendid!!, args.recentchats.name!!, args.recentchats.friendsimage!!)





        }


        viewModel.getMessages(args.recentchats.friendid!!).observe(viewLifecycleOwner, Observer {





            initRecyclerView(it)



        })




    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun pickImageFromGallery() {

        val pickPictureIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (pickPictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(pickPictureIntent, Utils.REQUEST_IMAGE_PICK)
        }
    }

    // To take a photo with the camera, you can use this code
    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhotoWithCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE)


    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                Utils.REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap

                    uploadImageToFirebaseStorage(Utils.getUidLoggedIn(), args.recentchats.friendid!!, args.recentchats.name!!, args.recentchats.friendsimage!!,imageBitmap)
                }
                Utils.REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    val imageBitmap =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
                    uploadImageToFirebaseStorage(Utils.getUidLoggedIn(), args.recentchats.friendid!!, args.recentchats.name!!, args.recentchats.friendsimage!!,imageBitmap)
                }
            }
        }


    }



    private fun uploadImageToFirebaseStorage(sender: String, receiver: String, friendname: String, friendimage: String, imageBitmap: Bitmap?) {
        val baos = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // Set the image bitmap to the ImageView if needed
        binding.fabsendimage.setImageBitmap(imageBitmap)

        val storagePath = storageRef.child("Photos/${UUID.randomUUID()}.jpg")
        val uploadTask = storagePath.putBytes(data)
        uploadTask.addOnSuccessListener { uploadTaskSnapshot ->
            val downloadUrlTask = uploadTaskSnapshot.metadata?.reference?.downloadUrl
            downloadUrlTask?.addOnSuccessListener { downloadUri ->
                val imageUrl = downloadUri.toString()

                // Create a message HashMap with image URL
                val messageHashMap = hashMapOf<String, Any>(
                    "sender" to sender,
                    "receiver" to receiver,
                    "message" to imageUrl,
                    "time" to Utils.getTime(),
                    "type" to "image"

                )

                val uniqueId = listOf(sender, receiver).sorted()
                uniqueId.joinToString(separator = "")

                // Upload the message to Firestore
                firestore.collection("Messages").document(uniqueId.toString())
                    .collection("chats").document(Utils.getTime())
                    .set(messageHashMap)
                    .addOnCompleteListener { messageUploadTask ->
                        if (messageUploadTask.isSuccessful) {
                            // Message uploaded successfully
                            // Now, update conversation for both sender and receiver

                            // Update sender's conversation
                            val senderConversationMap = hashMapOf<String, Any>(
                                "friendid" to receiver,
                                "time" to Utils.getTime(),
                                "sender" to sender,
                                "message" to imageUrl, // Update with image URL
                                "type" to "image"
                                // Add additional fields as needed
                            )
                            firestore.collection("Conversation$sender").document(receiver)
                                .set(senderConversationMap)

                            // Update receiver's conversation
                            val receiverConversationMap = hashMapOf<String, Any>(
                                "friendid" to sender,
                                "time" to Utils.getTime(),
                                "sender" to sender,
                                "message" to imageUrl, // Update with image URL
                                "type" to "image"
                                // Add additional fields as needed
                            )
                            firestore.collection("Conversation$receiver").document(sender)
                                .set(receiverConversationMap)

                            // You can continue with notification logic here if needed
                        } else {
                            // Message upload failed
                            Log.e("uploadImageAndSendMessage", "Failed to upload message")
                        }
                    }
            }
        }.addOnFailureListener { exception ->
            // Handle failure
            Log.e("uploadImageAndSendMessage", "Failed to upload image", exception)
        }
    }







    private fun initRecyclerView(list: List<Messages>) {
        adapter = MessageAdapter()
        val layoutManager = LinearLayoutManager(context)
        binding.messagesRecyclerView.layoutManager = layoutManager
        layoutManager.stackFromEnd = true

        // Set the adapter to the RecyclerView
        binding.messagesRecyclerView.adapter = adapter

        // Set the list to the adapter after it's initialized
        adapter.setList(list)
        adapter.notifyDataSetChanged()
    }



}