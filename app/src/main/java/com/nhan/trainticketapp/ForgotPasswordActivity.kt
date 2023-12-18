package com.nhan.trainticketapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nhan.trainticketapp.databinding.ActivityForgotPasswordBinding
import com.nhan.trainticketapp.databinding.ActivitySignInBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.btnSendResetPasswordEmail.setOnClickListener {
            val email = binding.etEmail.text.toString()
           if (checkEmail()) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    // email da duoc gui
                    Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show()
                    // destroy activity
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
           }
            }
        }

    }
    private fun checkEmail(): Boolean {
        val email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString() == "") {
            binding.etEmail.error = "You need enter email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "check email format"
            return false
        }
        return true
    }
}