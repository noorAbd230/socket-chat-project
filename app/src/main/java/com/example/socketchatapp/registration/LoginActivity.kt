package com.example.socketchatapp.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socketchatapp.MainActivity
import com.example.socketchatapp.R
import com.example.socketchatapp.SocketCreate
import com.example.socketchatapp.model.Users
import com.example.socketchatapp.registration.SignUpActivity.Companion.app
import com.example.socketchatapp.registration.SignUpActivity.Companion.mSocket
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class LoginActivity : AppCompatActivity() {

    companion object{
        var userActive: ArrayList<Users> = ArrayList()
        lateinit var isConnected: JSONObject
        // var userStatus: String ="disconnect"
    }
    private lateinit var auth: FirebaseAuth
    //lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
       // db= Firebase.firestore








        signup_txt.setOnClickListener {
            var i = Intent(this,
                    SignUpActivity::class.java)
            startActivity(i)
            finish()
        }


        forgot_password_txt.setOnClickListener {
            var i = Intent(this,
                ForgotPasswordActivity::class.java)
            startActivity(i)
            finish()
        }

        btn_login.setOnClickListener {
            var email = email_login.editText!!.text.toString()
            var password = password_login.editText!!.text.toString()
           // var userLogin :ArrayList<Users> = ArrayList()
           // userStatus="connect"
            mSocket!!.connect()
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    var i = Intent(
                        this,
                        MainActivity::class.java
                    )
                   // i.putExtra("userList",userList)
                    startActivity(i)
                    finish()

                    var sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    var editor = sharedPreferences.edit()
                    editor.putString("firstTime", "login")
                    editor.apply()


                } else {
                    Toast.makeText(
                        applicationContext,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    }


//    private fun sendMessage() {
//        var id = auth.currentUser!!.uid
//        // var name = auth.currentUser!!.displayName
//        var user = JSONObject()
//        user.put("userId", id)
//        user.put("userName", name)
//
//
//        mSocket!!.emit("isActive", user)
//
//        //Toast.makeText(this,"${join}",Toast.LENGTH_SHORT).show()
//
//    }
//
//    val  onNewMessage = Emitter.Listener { args ->
//        runOnUiThread {
//            try {
//                isConnected = args[0] as JSONObject
//               var userId = isConnected.getString("userId")
//               var userName = isConnected.getString("userName")
//                userActive.add(Users(userId,R.drawable.ic_baseline_person_24,userName))
//                // Toast.makeText(this, "$message",Toast.LENGTH_SHORT).show()
//                // isConnected = user.getString("userId")
////              for (i in 0 until isConnected.length()){
////                userActive.add(isConnected.getString(i))
////            }
////            Toast.makeText(this, "${userActive}",Toast.LENGTH_SHORT).show()
//
//            } catch (e: Exception) {
//                Log.e("NOR", "NOT FOUND>>>>>")
//            }
//
//        }
//    }

}