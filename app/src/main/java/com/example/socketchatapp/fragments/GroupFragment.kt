package com.example.socketchatapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketchatapp.R
import com.example.socketchatapp.adapter.FriendsAdapter
import com.example.socketchatapp.adapter.GroupAdapter
import com.example.socketchatapp.chat.GroupActivity
import com.example.socketchatapp.chat.NewGroupActivity
import com.example.socketchatapp.model.Groups
import com.example.socketchatapp.model.Users
import com.example.socketchatapp.registration.LoginActivity
import com.example.socketchatapp.registration.SignUpActivity
import com.github.nkzawa.emitter.Emitter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_new_group.*
import kotlinx.android.synthetic.main.fragment_group.*
import kotlinx.android.synthetic.main.fragment_group.view.*
import org.json.JSONArray
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class GroupFragment : Fragment() {
    lateinit var groupInfo: JSONArray
    var userList: ArrayList<Groups> = ArrayList()
    private lateinit var auth: FirebaseAuth
    lateinit var groupAdapter: GroupAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for0.1 this fragment
        var root = inflater.inflate(R.layout.fragment_group, container, false)
        auth = Firebase.auth

        groupAdapter= GroupAdapter(context as Activity,userList)

        root.new_group.setOnClickListener {
            var i = Intent(context, NewGroupActivity::class.java)
            startActivity(i)
        }

        SignUpActivity.mSocket!!.emit("group", "true")
        SignUpActivity.mSocket!!.on("group-data", Emitter.Listener { args ->
            this.activity?.runOnUiThread {
                 groupInfo = args[0] as JSONArray
                groupAdapter.data.clear()
                for (i in 0 until groupInfo.length()) {
                    if (groupInfo.getString(i) == "true") {
                        Log.e("no", "groupInfo")

                    } else if (groupInfo.getJSONObject(i).has("groupName")) {

                        for (j in 0 until groupInfo.getJSONObject(i).getJSONArray("members")
                            .length()) {

                            if (groupInfo.getJSONObject(i).getJSONArray("members")[j] == auth.currentUser!!.uid
                            ) {
                                    userList.add(
                                            Groups(
                                                    groupInfo.getJSONObject(i).getString("groupName"),
                                                    groupInfo.getJSONObject(i).getString("groupImg"),
                                                    groupInfo.getJSONObject(i)["members"]
                                                            .toString(),
                                                    groupInfo.getJSONObject(i)["membersName"]
                                                            .toString()

                                            )
                                    )
                                }


                        }




                        // Toast.makeText(context, "${groupInfo.getJSONObject(i)}",Toast.LENGTH_SHORT).show()


                    }
                }


                if (userList.isNotEmpty()){
                    root.layout_rvFriend.visibility = View.VISIBLE
                    root.friends_layout.visibility = View.GONE

                    root.rvFriend.layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL, false
                    )
                    root.rvFriend.setHasFixedSize(true)
                    groupAdapter = GroupAdapter(context as Activity, userList)
                    root.rvFriend.adapter = groupAdapter
                    groupAdapter.notifyDataSetChanged()
                }else{
                    root.layout_rvFriend.visibility = View.GONE
                    root.friends_layout.visibility = View.VISIBLE
                }


            }


        })




//        var userActive: ArrayList<Users> = ArrayList()
//        auth = Firebase.auth
//
//
//
//
//        if (SignUpActivity.message.length() == null) {
//            root.layout_rvFriend.visibility = View.GONE
//            root.friends_layout.visibility = View.VISIBLE
//
//        } else {
//            root.layout_rvFriend.visibility = View.VISIBLE
//            root.friends_layout.visibility = View.GONE
//            for (i in 0 until SignUpActivity.message.length()) {
//                if (SignUpActivity.message.getJSONObject(i).getString("id")== auth.currentUser!!.uid){
//                    SignUpActivity.message.getJSONObject(i).remove("login")
//                }else if (SignUpActivity.message.getJSONObject(i).getString("id")!= auth.currentUser!!.uid) {
//                    userActive.add(Users(SignUpActivity.message.getJSONObject(i).getString("id"), R.drawable.ic_baseline_person_24, SignUpActivity.message.getJSONObject(i).getString("name")))
//                }
//            }
//
//            //Toast.makeText(context, "${userActive}",Toast.LENGTH_SHORT).show()
//
//            root.rvFriend.layoutManager = LinearLayoutManager(context,
//                LinearLayoutManager.VERTICAL, false)
//            root.rvFriend.setHasFixedSize(true)
//            val friendsAdapter = FriendsAdapter(context as Activity, userActive,"GroupFragment")
//            root.rvFriend.adapter = friendsAdapter
//
//
//        }


        return root
    }


}

