package com.example.socketchatapp.registration

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socketchatapp.R
import com.example.socketchatapp.SocketCreate
import com.example.socketchatapp.model.Users
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {

    companion object{
        lateinit var app: SocketCreate
        //var isConnected = true
        lateinit var message: JSONArray
        var mSocket: Socket? = null
        var userList: ArrayList<Users> = ArrayList()

    }

    lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
   // lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
      //  db = Firebase.firestore
        auth = Firebase.auth
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering User....")
        progressDialog.setCancelable(false)

        app=application as SocketCreate
        mSocket=app.getSocket()

//        mSocket!!.connect()


        login_txt.setOnClickListener {
            var i = Intent(
                    this,
                    LoginActivity::class.java
            )
            startActivity(i)
            finish()
        }


        mSocket!!.on(Socket.EVENT_CONNECT_ERROR) {
            runOnUiThread {
                Log.e("EVENT_CONNECT_ERROR", "EVENT_CONNECT_ERROR: ")
            }
        };
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT,  Emitter.Listener {
            runOnUiThread {
                Log.e("EVENT_CONNECT_TIMEOUT", "EVENT_CONNECT_TIMEOUT: ")

            }
        })
        mSocket!!.on(
                Socket.EVENT_CONNECT
        ) {
            Log.e("onConnect", "Socket Connected!")
            //isConnected = true
        }
        mSocket!!.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
            runOnUiThread {
                Log.e("onDisconnect", "Socket onDisconnect!")
                //isConnected = false
            }
        })

        SignUpActivity.mSocket!!.on("get-data",onNewMessage)

        mSocket!!.connect()

        signup_btn.setOnClickListener {

             var name = username_signup.editText!!.text.toString()
            var email = email_signup.editText!!.text.toString()
            var password = password_signup.editText!!.text.toString()
            var confirmPassword = confirm_password_signup.editText!!.text.toString()



            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                email_signup.error = "Invalid Email"
                email_signup.isFocusable = true
            } else if (password.length < 6) {
                password_signup.error = "Password length at least 6 characters"
                password_signup.isFocusable = true
            } else if (confirmPassword!=password){
                confirm_password_signup.error = "Passwords not match"
                confirm_password_signup.isFocusable = true
            }else{

                    registerUser(name, email, password)
                    // Toast.makeText(this, "$userList",Toast.LENGTH_SHORT).show()

                }





        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        progressDialog.show()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                  sendMessage()
                progressDialog.dismiss()
                //val user = auth.currentUser!!
               // addUserToDb(user.uid, name, user.email!!)
            } else {
                progressDialog.dismiss()

            }
        }
    }



    val  onNewMessage = Emitter.Listener { args ->
        runOnUiThread {
            try {
                message = args[0] as JSONArray

            } catch (e: Exception) {
                Log.e("NOR", "NOT FOUND>>>>>")
            }
        }
    }


    private fun sendMessage() {


            var id = auth.currentUser!!.uid
            // var name = auth.currentUser!!.displayName
            var user = JSONObject()
            user.put("name",username_signup.editText!!.text.toString())
            user.put("email",email_signup.editText!!.text.toString())
            user.put("id", id)
            mSocket!!.emit("login", user)


            var i = Intent(
                this,
                LoginActivity::class.java
              )
          startActivity(i)
           finish()

    }






}