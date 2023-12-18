package com.nhan.trainticketapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var imageView: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button
    private var imageUri: Uri? = null

    private fun uploadImageAndSaveUserInfo(
        imageUri: Uri, name: String, email: String, phone: String
    ) {
        val resizedBitmap = getResizedBitmap(
            imageUri, 800, 800
        ) // Giả sử bạn muốn giảm kích thước xuống còn 800x800 pixels
        val baos = ByteArrayOutputStream()
        resizedBitmap?.compress(
            Bitmap.CompressFormat.JPEG, 70, baos
        ) // Nén hình ảnh và chuyển đổi thành mảng byte
        val data = baos.toByteArray()

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val storageRef =
            FirebaseStorage.getInstance().reference.child("user_images/${userId}/profile.jpg")
        val uploadTask = storageRef.putBytes(data)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                if (userId != null) {
                    val userRef = FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(userId)
                    println(userId)
                    val userInfo = mapOf(
                        "name" to name,
                        "email" to email,
                        "phone" to phone,
                        "image" to downloadUri.toString()
                    )
                    userRef.updateChildren(userInfo)
                }
            } else {
                Toast.makeText(
                    this, "Upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getResizedBitmap(imageUri: Uri, maxWidth: Int, maxHeight: Int): Bitmap? {
        val inputStream = contentResolver.openInputStream(imageUri)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        var scale = 1
        while (options.outWidth / scale / 2 >= maxWidth && options.outHeight / scale / 2 >= maxHeight) {
            scale *= 2
        }

        val resizedOptions = BitmapFactory.Options().apply {
            inSampleSize = scale
        }

        val resizedInputStream = contentResolver.openInputStream(imageUri)
        val resizedBitmap = BitmapFactory.decodeStream(resizedInputStream, null, resizedOptions)
        resizedInputStream?.close()

        return resizedBitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Firebase App Check
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )

        imageView = findViewById(R.id.imgAvatar)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        btnSave = findViewById(R.id.btnSave)

        imageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST
            )
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (imageUri != null && name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                uploadImageAndSaveUserInfo(imageUri!!, name, email, phone)
            } else {
                Toast.makeText(
                    this, "Please fill all fields and select an image", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri) // Hiển thị hình ảnh đã chọn
        }
    }

}