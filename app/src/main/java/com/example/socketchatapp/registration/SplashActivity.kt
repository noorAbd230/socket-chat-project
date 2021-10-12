package com.example.socketchatapp.registration

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.socketchatapp.MainActivity
import com.example.socketchatapp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    var firstTime = "false"
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        var  topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        logo_img.animation= topAnimation
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        firstTime= sharedPreferences.getString("firstTime","true")!!


        when (firstTime) {
            "true" -> {

                Handler().postDelayed({
                    var editor= sharedPreferences.edit()
                    firstTime="false"
                    editor.putString("firstTime",firstTime)
                    editor.apply()
                    var i = Intent(this,
                        WalkThroughActivity::class.java)
                    startActivity(i)
                    finish()

                },4000)

            }
            "login" -> {
                var i = Intent(
                    this,
                    MainActivity::class.java
                )
                startActivity(i)
                finish()
            }
            "logout" -> {
                var i = Intent(this,
                   LoginActivity::class.java)
                startActivity(i)
                finish()
            }
            else -> {
                var i = Intent(this,
                    SignUpActivity::class.java)
                startActivity(i)
                finish()
            }
        }

    }
}