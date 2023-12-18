package com.nhan.trainticketapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nhan.trainticketapp.databinding.ActivitySignInBinding
import com.nhan.trainticketapp.databinding.ActivitySignUpBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set view binding
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // xoa action bar
        supportActionBar?.hide()

       auth = Firebase.auth
        binding.btnSignIn.setOnClickListener {
            // if true
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (checkAllField()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // neu thanh cong thi dang nhap
                        Toast.makeText(this, "successfully sign in ", Toast.LENGTH_SHORT).show()
                        // di den activity khac
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("error: ", it.exception.toString())
                    }
                }
            }
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            // chua xoa activity cua sign in, nhan nut BACK de tro lai
            finish()
        }

        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    private fun checkAllField():Boolean {
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

        return true
    }
}