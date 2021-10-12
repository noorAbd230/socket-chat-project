package com.example.socketchatapp.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.socketchatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = Firebase.auth
        btn_reset_password.setOnClickListener {
            var email= ed_email_address.text.toString()
            forgetPassword(email)
        }

        img_back.setOnClickListener {
            var i = Intent(
                this,
                LoginActivity::class.java)

            startActivity(i)
            finish()
        }

    }

    private fun forgetPassword(email: String) {

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Email send.", Toast.LENGTH_SHORT).show()
                }else{
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        ed_email_address.error = "Invalid Email"
                        ed_email_address.isFocusable = true

                    }
                }
            }
    }
}