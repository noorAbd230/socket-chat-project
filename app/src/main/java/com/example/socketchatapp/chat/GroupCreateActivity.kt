package com.example.socketchatapp.chat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketchatapp.R
import com.example.socketchatapp.adapter.FriendsAdapter
import com.example.socketchatapp.adapter.GroupMemberAdapter
import com.example.socketchatapp.model.Users
import com.example.socketchatapp.registration.SignUpActivity
import com.github.nkzawa.emitter.Emitter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_group_create.*
import kotlinx.android.synthetic.main.activity_new_group.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.ArrayList

class GroupCreateActivity : AppCompatActivity() {
    lateinit var base64String:String
    lateinit var  membersName :String
    var userArr: ArrayList<Users>? = ArrayList()
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_create)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        auth = Firebase.auth
        userArr = intent.getParcelableArrayListExtra<Users>("usersMember")


        make_group.setOnClickListener {
            sendGroup()
            var i= Intent(this, GroupActivity::class.java)
            i.putExtra("groupName",ed_group_name.text.toString())
            i.putExtra("groupImg",base64String)
            i.putExtra("unique","GroupActivity")
            i.putExtra("membersName",userArr)
            startActivity(i)
        }



        userNum.text = "${userArr!!.size}"
        rvUserMember.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        rvUserMember.setHasFixedSize(true)
       var memberAdapter =  GroupMemberAdapter(this, userArr!!)
        rvUserMember.adapter = memberAdapter

        put_group_img.setOnClickListener {
            PickImageDialog.build(PickSetup())
                .setOnPickResult { r ->
                    val inputStream: InputStream? = contentResolver.openInputStream(r.uri)
                    val image = BitmapFactory.decodeStream(inputStream)
                    sendImage(image)
                    img_new_group.setImageBitmap(r.bitmap)

                }
                .setOnPickCancel {

                }.show(this)
        }
    }

        private fun sendGroup(){
        var group = JSONObject()
        group.put("groupName",ed_group_name.text.toString())
        group.put("groupImg",base64String)
       // group.put("members",userArr)


            var users = JSONArray()
            for (i in userArr!!){
                users.put(i.id)
                users.put(auth.currentUser!!.uid)
            }
           // var mem = JSONObject()
            group.put("members",users)


            val names = JSONArray()
            for (j in userArr!!){
                names.put(j.name)
            }
            // var mem = JSONObject()
            group.put("membersName",names)

        SignUpActivity.mSocket!!.emit("group", group)



    }

    private fun sendImage(image: Bitmap?){
        val outputStream = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

          base64String = android.util.Base64.encodeToString(outputStream.toByteArray(),
            android.util.Base64.DEFAULT)


    }

//        val  onNewGroup = Emitter.Listener { args ->
//        runOnUiThread {
//            try {
//               var groupInfo = args[0] as JSONArray
//
//                Toast.makeText(this,"$groupInfo",Toast.LENGTH_SHORT).show()
//            } catch (e: Exception) {
//                Log.e("NOR", "NOT FOUND>>>>>")
//            }
//        }
//    }
}