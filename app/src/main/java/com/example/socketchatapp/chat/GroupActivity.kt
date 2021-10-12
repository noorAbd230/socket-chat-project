package com.example.socketchatapp.chat

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketchatapp.MainActivity
import com.example.socketchatapp.R
import com.example.socketchatapp.adapter.MsgAdapter
import com.example.socketchatapp.fragments.GroupFragment
import com.example.socketchatapp.fragments.HomeFragment
import com.example.socketchatapp.fragments.ProfileFragment
import com.example.socketchatapp.fragments.ProfileFragment.Companion.name
import com.example.socketchatapp.model.Users
import com.example.socketchatapp.registration.SignUpActivity
import com.example.socketchatapp.registration.SignUpActivity.Companion.message
import com.github.nkzawa.emitter.Emitter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonArray
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class GroupActivity : AppCompatActivity() , TextWatcher {
    private lateinit var auth: FirebaseAuth
    private lateinit var sdf: SimpleDateFormat
    private var userName: String?=null
    private var text: String?=null
    private var desId: String?=null
    private var groupName: String?=null
    private var messageAdapter: MsgAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        auth = Firebase.auth



        back_group.setOnClickListener {
          var i =  Intent(this,MainActivity::class.java)
            i.putExtra("groupActivity","group")
            startActivity(i)
            finish()
        }

        sdf = SimpleDateFormat("hh:mm a")
        SignUpActivity.mSocket!!.on("groupMsg", onNewMessage)
        SignUpActivity.mSocket!!.on("groupImg", onNewImg)


        var gName = intent.getStringExtra("name")
        var img = intent.getStringExtra("img")
        var users = intent.getStringExtra("users")
        var unique = intent.getStringExtra("unique")
        var groupName = intent.getStringExtra("groupName")
        var groupImg = intent.getStringExtra("groupImg")
        var members = intent.getParcelableArrayListExtra<Users>("membersName")

        when (unique) {
            "GroupActivity" ->{
                var names = JSONArray()
                for (i in members!!){
                    names.put(i.name)
                }

                group_members.text = "Me,${names}"
                group_name.text = groupName
                val bitmap: Bitmap? = getBitmapFromString(groupImg!!)
                if (bitmap!=null){
                    group_img.setImageBitmap(bitmap)
                }else{
                    group_img.setImageResource(R.drawable.ic_baseline_person_24)
                }

            }
            "GroupFragment" ->{
                group_members.text = "Me,$users"
                group_name.text = gName
                val bitmap: Bitmap? = getBitmapFromString(img!!)
                if (bitmap!=null){
                    group_img.setImageBitmap(bitmap)
                }else{
                    group_img.setImageResource(R.drawable.ic_baseline_person_24)
                }
            }

            else -> Toast.makeText(this,"Not Found", Toast.LENGTH_SHORT).show()

        }



        messageAdapter =  MsgAdapter(layoutInflater)
        rvGroupChat.adapter = messageAdapter
        rvGroupChat.layoutManager = LinearLayoutManager(this)


        ed_messege_group.addTextChangedListener(this)

        img_group_send.setOnClickListener {
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
        msg_group_send.setOnClickListener {

            try {
                var objMsg = JSONObject()
                objMsg.put("msg", ed_messege_group.text.toString())
                objMsg.put("source_id", desId)
                objMsg.put("name", name)
                objMsg.put("date", sdf.format(Date()))
                SignUpActivity.mSocket!!.emit("groupMessage", objMsg)
                objMsg.put("isSent", true)

                messageAdapter!!.addItem(objMsg)
                rvGroupChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)
                resetMessageEdit()
            }catch (e: JSONException){
                e.printStackTrace()
            }



        }

    }


    private fun getBitmapFromString(image: String): Bitmap? {
        val bytes: ByteArray = android.util.Base64.decode(image, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun resetMessageEdit() {
        ed_messege_group.removeTextChangedListener(this)
        ed_messege_group.setText("")
        msg_group_send.visibility = View.INVISIBLE
        img_group_send.visibility = View.VISIBLE
        ed_messege_group.addTextChangedListener(this)
    }

    override fun afterTextChanged(p0: Editable?) {
       var text = p0.toString().trim()

        if (text!!.isEmpty()) {
            resetMessageEdit()
        } else {
            msg_group_send.visibility = View.VISIBLE
            img_group_send.visibility = View.INVISIBLE
//            if (userName!= ProfileFragment.name && desId!= auth.currentUser!!.uid){
//                user_typing.text = "${ProfileFragment.name} is typing.."
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
        SignUpActivity.mSocket!!.emit("groupImage",objImg)
        objImg.put("isSent", true)
        messageAdapter!!.addItem(objImg)

        rvGroupChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)
    }


    val onNewMessage = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val msg = args[0] as JSONObject


                    msg.put("isSent", false)

                    //Toast.makeText(this,msg.getString("img") + msg.getString("msg") ,Toast.LENGTH_SHORT).show()
                    // objImg.put("isSent", false)
                    messageAdapter!!.addItem(msg)
                    rvGroupChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)



            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }
    }


    val onNewImg = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val img = args[0] as JSONObject



                    img.put("isSent", false)

                    //Toast.makeText(this,msg.getString("img") + msg.getString("msg") ,Toast.LENGTH_SHORT).show()
                    // objImg.put("isSent", false)
                    messageAdapter!!.addItem(img)
                    rvGroupChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)



            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }
    }
}