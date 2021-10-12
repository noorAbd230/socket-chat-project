package com.example.socketchatapp.registration

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.foodiesapp.admin.adapter.model.WalkThrough
import com.example.socketchatapp.R
import com.example.socketchatapp.adapter.WalkThroughAdapter
import kotlinx.android.synthetic.main.activity_walk_through.*

class WalkThroughActivity : AppCompatActivity() {
    lateinit var dots:Array<ImageView>
    lateinit var data:MutableList<WalkThrough>
    var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_through)

        data= mutableListOf()
        data.add(WalkThrough("Live your life smarter",  R.drawable.sliderone,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et."))
        data.add(WalkThrough("Live your life smarter",R.drawable.slider2, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et."))
        data.add(WalkThrough("Live your life smarter", R.drawable.slider3, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et."))

        var adapter = WalkThroughAdapter(this, data)
        view_pager.adapter = adapter
        createDots(0)

        btn_continue.setOnClickListener {
            if (btn_continue.text=="Continue"){
                view_pager.setCurrentItem(currentPage+1,true)
            }else{
                var i= Intent(this,SignUpActivity::class.java)
                startActivity(i)
                finish()
            }

        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                createDots(position)
                currentPage=position

                if (position==2){
                    btn_continue.setBackgroundResource(R.drawable.btn_last_style)
                    btn_continue.setTextColor(resources.getColor(R.color.white))
                    btn_continue.text="Get started"
                    var param=  RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                    }
                    btn_continue.layoutParams=param

                }


            }

        })

    }

    fun createDots(position: Int){
        if (dots_layout!=null){
            dots_layout.removeAllViews()
        }
        dots=Array(data.size, {i -> ImageView(this)  })
        for (i in 0 until data.size){
            dots[i]=ImageView(this)
            if (i==position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_indicator_selected))
            }else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_indicator_default))

            }
            var params= LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,0,10,0)
            dots_layout.addView(dots[i],params)
        }

    }
}