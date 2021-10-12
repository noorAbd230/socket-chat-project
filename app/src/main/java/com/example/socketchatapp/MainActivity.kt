package com.example.socketchatapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.socketchatapp.fragments.GroupFragment
import com.example.socketchatapp.fragments.HomeFragment
import com.example.socketchatapp.fragments.ProfileFragment
import com.example.socketchatapp.model.Users
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
//    lateinit var app: SocketCreate
//    private var mSocket: Socket? = null
//    private var user_id="5"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    bottom_nav.add(MeowBottomNavigation.Model(1,R.drawable.ic_outline_home_24))
    bottom_nav.add(MeowBottomNavigation.Model(2,R.drawable.ic_outline_person_24))
    bottom_nav.add(MeowBottomNavigation.Model(3,R.drawable.ic_outline_send_24))
    bottom_nav.setCount(3,"3")
    bottom_nav.show(1,true)
   // var userList = intent.getParcelableArrayListExtra<Users>("userList")

    supportFragmentManager.beginTransaction().replace(
            R.id.mainContainer,
            HomeFragment()).commit()

//    var i = intent.getStringExtra("groupActivity")
//    if (i=="group"){
//        replaceFragment(GroupFragment())
//    }

//    val bundle = Bundle()
//    bundle.putParcelableArrayList("userList",userList)
//    val fragment = FriendsFragment()
//    fragment.arguments = bundle
//    Toast.makeText(this, "$userList", Toast.LENGTH_SHORT).show()

    bottom_nav.setOnClickMenuListener {
        when(it.id){
            1->replaceFragment(HomeFragment())
            2->replaceFragment(ProfileFragment())
            3->replaceFragment(GroupFragment())
            else ->""
        }
    }






    }

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(
                R.id.mainContainer,
                fragment).commit()

    }

//    val onNewMessage = Emitter.Listener { args ->
//        runOnUiThread{
//            try{
//                val message = args[0] as JSONObject
//                if(user_id == message.getString("des_id")){
//                    txt_msg.text="${message.getString("smm")} \n"
//                }
//            }catch(e:Exception){
//                Log.e("TAG",e.toString())
//            }
//        }
//    }

}