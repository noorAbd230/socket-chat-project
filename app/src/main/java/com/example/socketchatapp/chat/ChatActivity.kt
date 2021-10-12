package com.example.socketchatapp.chat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketchatapp.R
import com.example.socketchatapp.adapter.MsgAdapter
import com.example.socketchatapp.fragments.ProfileFragment.Companion.name
import com.example.socketchatapp.fragments.ProfileFragment.Companion.profileId
import com.example.socketchatapp.registration.SignUpActivity
import com.example.socketchatapp.registration.SignUpActivity.Companion.mSocket
import com.example.socketchatapp.registration.SignUpActivity.Companion.message
import com.github.nkzawa.emitter.Emitter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity() , TextWatcher {
    private lateinit var auth: FirebaseAuth
    private lateinit var sdf: SimpleDateFormat
     private var userName: String?=null
     private var msgName: String?=null
     private var text: String?=null
     private var desId: String?=null
     private var msgId: String?=null
    private var messageAdapter: MsgAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        auth = Firebase.auth




                sdf = SimpleDateFormat("hh:mm a")
         mSocket!!.on("msg", onNewMessage)
         mSocket!!.on("img", onNewImg)


         userName = intent.getStringExtra("userName")
        var img = intent.getStringExtra("img")
         desId = intent.getStringExtra("id")
        user_name.text = userName

        msgId = desId + auth.currentUser!!.uid
        val bitmap: Bitmap? = getBitmapFromString(img!!)
        if (bitmap!=null){
            user_img.setImageBitmap(bitmap)
        }else{
            user_img.setImageResource(R.drawable.ic_baseline_person_24)
        }


        messageAdapter =  MsgAdapter(layoutInflater)
        rvUserChat.adapter = messageAdapter
        rvUserChat.layoutManager = LinearLayoutManager(this)


        ed_messege.addTextChangedListener(this)

        img_send.setOnClickListener {
            PickImageDialog.build(PickSetup())
                    .setOnPickResult { r ->

                        val inputStream: InputStream? = contentResolver.openInputStream(r.uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        sendImage(image)
                        //objImg.put("isSent", true)
                    }
                    .setOnPickCancel {

                    }.show(this)
        }
        msg_send.setOnClickListener {

            try {
                var objMsg = JSONObject()
                objMsg.put("msg", ed_messege.text.toString())
                objMsg.put("source_id", desId)
                objMsg.put("msgId", msgId)
                objMsg.put("name", name)
                objMsg.put("date", sdf.format(Date()))
                mSocket!!.emit("message", objMsg)
                objMsg.put("isSent", true)

                messageAdapter!!.addItem(objMsg)
                rvUserChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)
                resetMessageEdit()
            }catch (e: JSONException){
                e.printStackTrace()
            }



        }

    }

    private fun sendImage(image: Bitmap?) {
        val outputStream = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        val base64String: String = android.util.Base64.encodeToString(outputStream.toByteArray(),
                android.util.Base64.DEFAULT)

        var objImg = JSONObject()
        objImg.put("img", base64String)
        objImg.put("date", sdf.format(Date()))
        objImg.put("name", name)
        objImg.put("source_id", desId)
        objImg.put("msgId", msgId)
        mSocket!!.emit("image",objImg)
        objImg.put("isSent", true)
        messageAdapter!!.addItem(objImg)

        rvUserChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)
    }


//    private fun sendMessage() {
//
//        objMsg.put("msg", ed_messege.text.toString())
//        objMsg.put("source_id", desId)
//        objMsg.put("name", userName)
//        objMsg.put("date", sdf.format(Date()))
//        mSocket!!.emit("message", objMsg)
//        objMsg.put("isSent", true)
//        messageAdapter!!.addItem(objMsg)
//        rvUserChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)
//
//    }


    val onNewMessage = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val msg = args[0] as JSONObject


                if (msg.getString("source_id")==auth.currentUser!!.uid) {
                    msg.put("isSent", false)

                    //Toast.makeText(this,msg.getString("img") + msg.getString("msg") ,Toast.LENGTH_SHORT).show()
                   // objImg.put("isSent", false)
                    messageAdapter!!.addItem(msg)
                    rvUserChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)

                }

            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }
    }


    val onNewImg = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val img = args[0] as JSONObject


                 if (img.getString("source_id")==auth.currentUser!!.uid) {
                    img.put("isSent", false)

                    //Toast.makeText(this,msg.getString("img") + msg.getString("msg") ,Toast.LENGTH_SHORT).show()
                    // objImg.put("isSent", false)
                    messageAdapter!!.addItem(img)
                    rvUserChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)

               }

            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }
    }


    private fun resetMessageEdit() {
        ed_messege.removeTextChangedListener(this)
        ed_messege.setText("")
        msg_send.visibility = View.INVISIBLE
        img_send.visibility = View.VISIBLE
        //user_typing.text = "online"
        ed_messege.addTextChangedListener(this)
    }

    override fun afterTextChanged(p0: Editable?) {
           text = p0.toString().trim()

        if (text!!.isEmpty()) {
            resetMessageEdit()
        } else {
            msg_send.visibility = View.VISIBLE
            img_send.visibility = View.INVISIBLE
//            if (userName!= name && desId!= auth.currentUser!!.uid){
//                user_typing.text = "$name is typing.."
//            }else{
//                user_typing.text = "online"
//
//            }

        }

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


    }

    private fun getBitmapFromString(image: String): Bitmap? {
        val bytes: ByteArray = android.util.Base64.decode(image, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}