package com.example.socketchatapp

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketchatapp.adapter.FriendsAdapter
import com.example.socketchatapp.model.Users
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import java.util.ArrayList

class SocketCreate : Application() {

    private var mSocket: Socket? = IO.socket("http://192.168.1.105:4000")


    fun getSocket(): Socket? {
       // mSocket!!.on("get-data",onNewMessage)
        return mSocket
    }

//    val onNewMessage = Emitter.Listener { args ->
//         val userList=ArrayList<Users>()
//            try{
//                val message = args[0] as JSONArray
//                // Toast.makeText(this, "$message",Toast.LENGTH_SHORT).show()
//
//                for (i in 0 until message.length()){
//                    userList.add(Users(R.drawable.ic_baseline_person_24,message.getJSONObject(i).getString("user-name")))
//                }
//
//                EventBus.getDefault().post(userList)
//
//            }catch(e:Exception){
//                Log.e("NOR","Not Found <<<<<<")
//            }
//
//    }

//    override fun onTerminate() {
//        super.onTerminate()
//        mSocket!!.off("get-data")
//    }
}