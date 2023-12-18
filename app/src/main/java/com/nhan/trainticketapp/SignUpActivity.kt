package com.nhan.trainticketapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nhan.trainticketapp.databinding.ActivitySignUpBinding

private lateinit var auth: FirebaseAuth
@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set view binding

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val  password = binding.etPassword.text.toString()

            if (checkAllField()) {
                auth.createUserWithEmailAndPassword(
                    email, password

                ).addOnCompleteListener {
                    // if successful account is created
                    // also signed in
                    if (it.isSuccessful) {
                        auth.signOut()
                        Toast.makeText(this, "Account created successful", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.e("Error: ", it.exception.toString())
                    }
                }
            }
        }


    }

    private fun checkAllField(): Boolean {
        val email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString() == "") {
            binding.textInputLayoutEmail.error = "this is required"
            return false
        }
         if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
             binding.textInputLayoutEmail.error = "Check email format"
             return false
         }

        if (binding.etPassword.text.toString() =="") {
            binding.textInputLayoutPassword.error = "this is required"
           binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (binding.etPassword.length() <= 7 ) {
            binding.textInputLayoutPassword.error = "Password must at least 8 characters"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (binding.etConfirmPassword.text.toString() == "") {
            binding.textInputLayoutConfirmPassword
                .error = "this is required"
            binding.textInputLayoutConfirmPassword.errorIconDrawable = null
        }
        if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
            binding.textInputLayoutConfirmPassword.error = "password do not match"
            return false
        }
        return true
    }
}