package com.example.socketchatapp.fragments

import android.R.attr.name
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socketchatapp.R
import com.example.socketchatapp.registration.LoginActivity
import com.example.socketchatapp.registration.LoginActivity.Companion.isConnected
import com.example.socketchatapp.registration.SignUpActivity
import com.example.socketchatapp.registration.SignUpActivity.Companion.mSocket
import com.example.socketchatapp.registration.SignUpActivity.Companion.message
import com.github.nkzawa.emitter.Emitter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import kotlin.random.Random.Default.Companion


class ProfileFragment : Fragment() {
    //lateinit var db: FirebaseFirestore
    companion object{
       var name:String?=null
       var profileId:String?=null
    }
    private lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var reference: StorageReference

    lateinit var path:String


     var email:String?=null
     var img:String?=null
    lateinit var progressDialog:ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_profile, container, false)

        auth = Firebase.auth

        root.edit_img.setOnClickListener {
            PickImageDialog.build(PickSetup())
                    .setOnPickResult { r ->
                        val inputStream: InputStream? = activity!!.contentResolver.openInputStream(r.uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        sendImage(image)
                        root.img_user_profile.setImageBitmap(r.bitmap)
                       // uploadImage(r.uri)
                    }
                    .setOnPickCancel {

                    }.show(activity!!.supportFragmentManager)
        }



        for (i in 0 until message.length()) {


                if (message.getJSONObject(i).getString("id") == auth.currentUser!!.uid) {

                    if (message.getJSONObject(i).isNull("img")) {
                        root.img_user_profile.setImageResource(R.drawable.ic_baseline_person_24)
                        email = message.getJSONObject(i).getString("email")
                        name = message.getJSONObject(i).getString("name")
                        profileId = message.getJSONObject(i).getString("id")
                    } else {
                        img = message.getJSONObject(i).getString("img")
                        val bitmap: Bitmap? = getBitmapFromString(img!!)
                        root.img_user_profile.setImageBitmap(bitmap)
                        email = message.getJSONObject(i).getString("email")
                        name = message.getJSONObject(i).getString("name")
                        profileId = message.getJSONObject(i).getString("id")

                    }

                    //  Toast.makeText(context, "$email >> $name",Toast.LENGTH_SHORT).show()


            }
        }

        if (name!!.isNotEmpty()&& email!!.isNotEmpty()){
            root.txt_username.text = name
            root.tv_email.text = email
        }




        root.tv_sign_out.setOnClickListener {

            Firebase.auth.signOut()


                var i= Intent(context, LoginActivity::class.java)
                startActivity(i)
                activity!!.finish()
                progressDialog.dismiss()
            var sharedPreferences = context!!.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putString("firstTime", "logout")
            editor.apply()

        }

        return root
    }



    private fun sendImage(image: Bitmap?){
        val outputStream = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        val base64String: String = android.util.Base64.encodeToString(outputStream.toByteArray(),
                android.util.Base64.DEFAULT)
       // var imgObj = JSONObject()
       // imgObj.put("id", auth.currentUser!!.uid)
        //imgObj.put("img", base64String)
        mSocket!!.emit("profileImage",base64String)



    }


    private fun getBitmapFromString(image: String): Bitmap? {
        val bytes: ByteArray = android.util.Base64.decode(image, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }



//    private fun uploadImage(uri: Uri?){
//        progressDialog.show()
//        reference.child("chatProfile/"+ UUID.randomUUID().toString())
//            .putFile(uri!!)
//            .addOnSuccessListener { taskSnapshot ->
//                progressDialog.dismiss()
//                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                    path=uri.toString()
//                    val update= UserProfileChangeRequest.Builder()
//                        .setPhotoUri(uri)
//                        .build()
//                    auth.currentUser!!.updateProfile(update)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful){
//                                Log.w("gh","updated successfully")
//                            }else
//                                Log.w("gh",task.exception!!)
//                        }
//
//                }
//                Toast.makeText(context,"success", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { exception ->
//                Log.e("img",exception.message!!)
//            }
//
//    }



}

