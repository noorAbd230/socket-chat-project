package com.example.socketchatapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.socketchatapp.R
import com.example.socketchatapp.chat.ChatActivity
import com.example.socketchatapp.chat.GroupActivity
import com.example.socketchatapp.model.Groups
import com.example.socketchatapp.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.friends_item.view.*
import kotlinx.android.synthetic.main.group_item.view.*
import kotlinx.android.synthetic.main.group_item.view.linear_user


class GroupAdapter( var activity: Activity, var data: ArrayList<Groups>) :
    RecyclerView.Adapter<GroupAdapter.MyViewHolder>()  {
    lateinit var sharedPreferences: SharedPreferences
   // lateinit var userObj: JSONObject
    lateinit var userArr: ArrayList<Users>
//    init {
//        setHasStableIds(true)
//    }

    //lateinit var user:Users
   // var name:String?=null
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvGroupName = itemView.tvGroupName
        val imgGroup= itemView.img_group
        val card= itemView.group_card




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.friends_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getBitmapFromString(image: String): Bitmap? {
        val bytes: ByteArray = android.util.Base64.decode(image, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }




    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      //  var users: Users =data[position]
      //  val user = data[position]
       // holder.setIsRecyclable(false)
          userArr=ArrayList()
          var auth: FirebaseAuth = Firebase.auth
        holder.tvGroupName.text = data[position].groupName

        val bitmap: Bitmap? = getBitmapFromString(data[position].groupImg!!)
        if (bitmap!=null){
            holder.imgGroup.setImageBitmap(bitmap)
        }else{
            holder.imgGroup.setImageResource(R.drawable.ic_baseline_person_24)
        }



            holder.card.setOnClickListener {
                var i= Intent(activity, GroupActivity::class.java)
                i.putExtra("name",data[position].groupName)
                i.putExtra("img",data[position].groupImg)
                i.putExtra("users",data[position].membersName)
                i.putExtra("unique","GroupFragment")
                activity.startActivity(i)
            }















    }





    fun makeToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }



}