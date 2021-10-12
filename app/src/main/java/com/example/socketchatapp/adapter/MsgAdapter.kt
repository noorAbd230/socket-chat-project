package com.example.socketchatapp.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.img_receive_item.view.*
import kotlinx.android.synthetic.main.img_send_item.view.*
import kotlinx.android.synthetic.main.msg_receive_item.view.*
import kotlinx.android.synthetic.main.msg_send_item.view.*
import org.json.JSONException
import org.json.JSONObject


class MsgAdapter(var inflator: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val TYPE_MESSAGE_SENT = 0
    private val TYPE_MESSAGE_RECEIVED = 1
    private val TYPE_IMAGE_SENT = 2
    private val TYPE_IMAGE_RECEIVED = 3
    private lateinit var auth: FirebaseAuth
   // lateinit var inflater:LayoutInflater
    private val messages: ArrayList<JSONObject> = ArrayList()



    private class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val send_msg = itemView.send_msg
        val date_send= itemView.date_send

    }

    private class SentImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val send_img = itemView.send_img
        val date_img_send = itemView.date_img_send
    }

    private class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val date_receive= itemView.receive_date
        val msg_receive= itemView.receive_msg
        val name_receive= itemView.name_receive

    }

    private class ReceivedImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receive_img = itemView.receive_img
        val date_img_receive = itemView.date_img_receive
        val name_img_receive = itemView.name_img_receive
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        //this.inflator = inflator
        when (viewType) {
            TYPE_MESSAGE_SENT -> {
                view = inflator.inflate(com.example.socketchatapp.R.layout.msg_send_item,parent,false)
                return SentMessageHolder(view)
            }
            TYPE_MESSAGE_RECEIVED -> {
                view = inflator.inflate(com.example.socketchatapp.R.layout.msg_receive_item,parent,false)
                return ReceivedMessageHolder(view)
            }
            TYPE_IMAGE_SENT -> {
                view = inflator.inflate(com.example.socketchatapp.R.layout.img_send_item,parent,false)
                return SentImageHolder(view)
            }
            TYPE_IMAGE_RECEIVED -> {
                view = inflator.inflate(com.example.socketchatapp.R.layout.img_receive_item,parent,false)
                return ReceivedImageHolder(view)
            }
        }

        return null!!

    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        auth = Firebase.auth
        try {
            return if (message.getBoolean("isSent")) {
                if (message.has("msg")) TYPE_MESSAGE_SENT else TYPE_IMAGE_SENT
            } else {
                if (message.has("msg")) TYPE_MESSAGE_RECEIVED else TYPE_IMAGE_RECEIVED
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return -1
    }

    override fun getItemCount(): Int {
        return messages.size
    }



    private fun getBitmapFromString(image: String): Bitmap? {
        val bytes: ByteArray = android.util.Base64.decode(image, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


    fun addItem(jsonObject: JSONObject?) {
        messages.add(jsonObject!!)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        val message = messages[position]

        try {
            if (message.getBoolean("isSent")) {
                if (message.has("msg")) {


                    val messageHolder = holder as SentMessageHolder
                    messageHolder.send_msg.text = message.getString("msg")
                    messageHolder.date_send.text = message.getString("date")

                    messageHolder.send_msg.setOnLongClickListener {
                        messageHolder.date_send.visibility = View.VISIBLE
                        true
                    }

                    messageHolder.send_msg.setOnClickListener{
                        messageHolder.date_send.visibility = View.INVISIBLE
                    }
                } else if (message.has("img")){
                    val imageHolder = holder as SentImageHolder
                    val bitmap: Bitmap? = getBitmapFromString(message.getString("img"))
                    imageHolder.send_img.setImageBitmap(bitmap)
                    imageHolder.date_img_send.text = message.getString("date")

                    imageHolder.send_img.setOnLongClickListener {
                        imageHolder.date_img_send.visibility = View.VISIBLE
                        true
                    }

                    imageHolder.send_img.setOnClickListener{
                        imageHolder.date_img_send.visibility = View.INVISIBLE
                    }
                }
            } else{
                if (message.has("msg")){
                    val messageHolder = holder as ReceivedMessageHolder
                    messageHolder.msg_receive.text = message.getString("msg")
                    messageHolder.date_receive.text = message.getString("date")
                    messageHolder.name_receive.text = message.getString("name")

                    messageHolder.msg_receive.setOnLongClickListener {
                        messageHolder.date_receive.visibility = View.VISIBLE
                        true
                    }

                    messageHolder.msg_receive.setOnClickListener{
                        messageHolder.date_receive.visibility = View.INVISIBLE
                    }
                } else if (message.has("img")) {
                    val imageHolder = holder as ReceivedImageHolder
                    imageHolder.date_img_receive.text = message.getString("date")
                    imageHolder.name_img_receive.text = message.getString("name")
                    val bitmap: Bitmap? = getBitmapFromString(message.getString("img"))
                    imageHolder.receive_img.setImageBitmap(bitmap)

                    imageHolder.receive_img.setOnLongClickListener {
                        imageHolder.date_img_receive.visibility = View.VISIBLE
                        true
                    }

                    imageHolder.receive_img.setOnClickListener{
                        imageHolder.date_img_receive.visibility = View.INVISIBLE
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}

