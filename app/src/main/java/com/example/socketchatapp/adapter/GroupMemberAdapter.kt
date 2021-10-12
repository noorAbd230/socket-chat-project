package com.example.socketchatapp.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.socketchatapp.R
import com.example.socketchatapp.model.Users
import kotlinx.android.synthetic.main.user_item.view.*


class GroupMemberAdapter( var activity: Activity, var data: ArrayList<Users>) :
    RecyclerView.Adapter<GroupMemberAdapter.MyViewHolder>()  {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvUserMember = itemView.tvUserMember
        val imgUserMember= itemView.img_user_member


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.user_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getBitmapFromString(image: String): Bitmap? {
        val bytes: ByteArray = android.util.Base64.decode(image, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvUserMember.text = data[position].name

        val bitmap: Bitmap? = getBitmapFromString(data[position].img!!)
        if (bitmap!=null){
            holder.imgUserMember.setImageBitmap(bitmap)
        }else{
            holder.imgUserMember.setImageResource(R.drawable.ic_baseline_person_24)
        }


    }





    fun makeToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }




}