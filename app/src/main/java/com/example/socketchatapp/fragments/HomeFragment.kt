package com.example.socketchatapp.fragments

import android.app.Activity
import android.content.Context
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
import com.example.socketchatapp.model.Users
import com.example.socketchatapp.registration.LoginActivity
import com.example.socketchatapp.registration.LoginActivity.Companion.isConnected
import com.example.socketchatapp.registration.LoginActivity.Companion.userActive
import com.example.socketchatapp.registration.SignUpActivity
import com.example.socketchatapp.registration.SignUpActivity.Companion.message
import com.github.nkzawa.emitter.Emitter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONArray
import java.util.ArrayList


class HomeFragment : Fragment() , FriendsAdapter.OnLongClickListener{

    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var userName: String
    private var friendsAdapter: FriendsAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_home, container, false)
        var user: ArrayList<Users> = ArrayList()
        var userImg: ArrayList<Users> = ArrayList()
        var connectedUser: ArrayList<String> = ArrayList()
        auth = Firebase.auth



                    if (message.length() == null) {
                        root.layout_rvRecentChat.visibility = View.GONE
                        root.recentFriend_layout.visibility = View.VISIBLE

                    } else {
                        root.layout_rvRecentChat.visibility = View.VISIBLE
                        root.recentFriend_layout.visibility = View.GONE
                        for (i in 0 until message.length()) {

                                    if (message.getJSONObject(i)
                                            .getString("id") != auth.currentUser!!.uid
                                    ) {
                                        if (message.getJSONObject(i).has("img")) {

                                            user.add(
                                                Users(
                                                    message.getJSONObject(i).getString("id"),
                                                    message.getJSONObject(i).getString("img"),
                                                    message.getJSONObject(i).getString("name")
                                                )
                                            )


                                        } else {
                                            user.add(
                                                Users(
                                                    message.getJSONObject(i).getString("id"),
                                                    "",
                                                    message.getJSONObject(i).getString("name")
                                                )
                                            )

                                        }


                                }
                            // Toast.makeText(context, "$user", Toast.LENGTH_SHORT).show()
                        }

                            root.rvRecentChat.layoutManager = LinearLayoutManager(
                                context,
                                LinearLayoutManager.VERTICAL, false
                            )
                            root.rvRecentChat.setHasFixedSize(true)
                            val friendsAdapter =
                                FriendsAdapter(this, context as Activity, user, "HomeFragment")
                            root.rvRecentChat.adapter = friendsAdapter



//
                    }







            return root
        }

    override fun addItemOnLongClickListener(position: Int) {
        Log.e("nor",position.toString())
    }

    override fun RemoveItemOnLongClickListener(position: Int) {
        Log.e("nor",position.toString())
    }


}


