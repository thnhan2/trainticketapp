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
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var imageView: ImageView
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var etGender: EditText

    private var imageUri: Uri? = null

    private fun uploadImageAndSaveUserInfo(
        imageUri: Uri, name: String,  phone: String, age: String, gender:String
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
                        "phone" to phone,
                        "age" to age,
                        "gender" to gender,
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
//        firebaseAppCheck.installAppCheckProviderFactory(
//            SafetyNetAppCheckProviderFactory.getInstance()
//        )
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val userRef = userId?.let { FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(it) }
        imageView = findViewById(R.id.imgAvatar)
        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etPhone = findViewById(R.id.etPhone)
        etGender = findViewById(R.id.etGender)
        btnSave = findViewById(R.id.btnSave)


        userRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.value as? Map<*, *>
                val name = userData?.get("name") as? String
                val phone = userData?.get("phone") as? String
                val gender = userData?.get("gender") as? String
                val age = userData?.get("age") as? String
                val uri = userData?.get("image")

                etName.setText(name)
                etPhone.setText(phone)
                etGender.setText(gender)
                etAge.setText(age)
                Glide.with(this@EditProfileActivity).load(uri).into(imageView)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý lỗi khi không thể đọc dữ liệu từ Firebase
            }
        })

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
            val age = etAge.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val gender = etGender.text.toString().trim()

            if (name.isNotEmpty() && age.isNotEmpty() && phone.isNotEmpty() && gender.isNotEmpty()) {
                uploadImageAndSaveUserInfo(imageUri!!, name, phone, age, gender)
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