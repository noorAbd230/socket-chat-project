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
import com.example.socketchatapp.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.group_item.view.*


class FriendsAdapter(val longClick:OnLongClickListener,var activity: Activity, var data: ArrayList<Users>, var fragmentname:String) :
    RecyclerView.Adapter<FriendsAdapter.MyViewHolder>()  {
    lateinit var sharedPreferences: SharedPreferences
   // lateinit var userObj: JSONObject
    lateinit var userArr: ArrayList<Users>


    //lateinit var user:Users
   // var name:String?=null
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvUserName = itemView.tvUserName
        val imgUser= itemView.img_user
        val card= itemView.linear_user
        val checkBox= itemView.checkBox



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.group_item, parent, false)
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
          userArr=ArrayList()
          var auth: FirebaseAuth = Firebase.auth
        holder.tvUserName.text = data[position].name

        val bitmap: Bitmap? = getBitmapFromString(data[position].img!!)
        if (bitmap!=null){
            holder.imgUser.setImageBitmap(bitmap)
        }else{
            holder.imgUser.setImageResource(R.drawable.ic_baseline_person_24)
        }


        if (fragmentname == "GroupFragment"){


            holder.card.setOnLongClickListener {
                holder.card.setBackgroundResource(R.drawable.btn_login)
                longClick.addItemOnLongClickListener(position)
                true
            }




            holder.card.setOnClickListener {
                holder.card.setBackgroundResource(R.color.backgroundColor)
                longClick.RemoveItemOnLongClickListener(position)

            }


        }else{

            holder.card.setOnClickListener {
                var i= Intent(activity, ChatActivity::class.java)
                i.putExtra("userName",data[position].name)
                i.putExtra("img",data[position].img)
                i.putExtra("id",data[position].id)

                activity.startActivity(i)
            }
        }














    }





    fun makeToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    interface OnLongClickListener{
        fun addItemOnLongClickListener(position: Int)
        fun RemoveItemOnLongClickListener(position: Int)
    }


}