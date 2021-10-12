package com.example.socketchatapp.chat

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketchatapp.R
import com.example.socketchatapp.adapter.FriendsAdapter
import com.example.socketchatapp.adapter.MsgAdapter
import com.example.socketchatapp.model.Users
import com.example.socketchatapp.registration.LoginActivity
import com.example.socketchatapp.registration.SignUpActivity
import com.example.socketchatapp.registration.SignUpActivity.Companion.message
import com.github.nkzawa.emitter.Emitter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_new_group.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_group.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class NewGroupActivity : AppCompatActivity() , FriendsAdapter.OnLongClickListener{

    companion object{
        var userName:String?=null
        var userId:String?=null
    }

    private lateinit var auth: FirebaseAuth
    var userActive: ArrayList<Users> = ArrayList()
    var userGroup: ArrayList<Users> = ArrayList()
    private var friendsAdapter: FriendsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        auth = Firebase.auth

        continue_group.setOnClickListener {

            var i= Intent(this, GroupCreateActivity::class.java)
            i.putExtra("usersMember",userGroup)
           startActivity(i)
        }


       // Toast.makeText(this,"$userName >> $userId",Toast.LENGTH_SHORT).show()

      //  SignUpActivity.mSocket!!.on("group-data",onNewGroup)
        if (message.length() == null) {
            rvUserGroup.visibility = View.GONE
            new_group_layout.visibility = View.VISIBLE

        } else {
            rvUserGroup.visibility = View.VISIBLE
            new_group_layout.visibility = View.GONE
            for (i in 0 until message.length()) {

                        if (message.getJSONObject(i)
                                .getString("id") != auth.currentUser!!.uid
                        ) {
                            if (message.getJSONObject(i).has("img")) {

                                userActive.add(
                                    Users(
                                        message.getJSONObject(i).getString("id"),
                                        message.getJSONObject(i).getString("img"),
                                        message.getJSONObject(i).getString("name")
                                    )
                                )


                            } else {
                                userActive.add(
                                    Users(
                                        message.getJSONObject(i).getString("id"),
                                        "",
                                        message.getJSONObject(i).getString("name")
                                    )
                                )

                            }


                    }
            }

            rvUserGroup.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL, false
            )
            rvUserGroup.setHasFixedSize(true)
             friendsAdapter = FriendsAdapter(this,this, userActive, "GroupFragment")
            rvUserGroup.adapter = friendsAdapter




        }



    }



    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun addItemOnLongClickListener(position: Int) {
        var groupUser = userActive[position]
         userGroup.add(groupUser)
       // makeToast("$userGroup")
    }

    override fun RemoveItemOnLongClickListener(position: Int) {
        var groupUser = userActive[position]
        for (i in  userGroup.size -1 downTo 0){
                if (userGroup[i]==groupUser){
                    userGroup.removeAt(i)
                }

        }
    }
}