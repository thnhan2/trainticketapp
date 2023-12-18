package com.nhan.trainticketapp

import android.annotation.SuppressLint
import android.content.Intent
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
            val password = binding.etPassword.text.toString()

            if (checkAllField()) {
                // Kiểm tra xem email đã được đăng ký hay chưa
                auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods ?: emptyList<String>()

                            if (signInMethods.isNotEmpty()) {
                                // Email đã được đăng ký
                                Toast.makeText(this, "Email is already registered", Toast.LENGTH_SHORT).show()
                            } else {
                                // Email chưa được đăng ký, thực hiện đăng ký
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { registrationTask ->
                                        if (registrationTask.isSuccessful) {
                                            // Đăng ký thành công, đăng nhập ngay
                                            auth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener { signInTask ->
                                                    if (signInTask.isSuccessful) {
                                                        // Đăng nhập thành công, mở Activity Sign In
                                                        Toast.makeText(this, "Account created and signed in successfully", Toast.LENGTH_SHORT).show()
                                                        startActivity(Intent(this, SignInActivity::class.java))
                                                        finish()
                                                    } else {
                                                        Log.e("Error: ", signInTask.exception.toString())
                                                    }
                                                }
                                        } else {
                                            Toast.makeText(this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show()
                                            Log.e("Error: ", registrationTask.exception.toString())
                                        }
                                    }
                            }
                        } else {
                            Log.e("Error: ", task.exception.toString())
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