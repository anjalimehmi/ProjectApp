package com.o7.projectapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.o7.projectapp.databinding.ActivityFirstScreenBinding
import com.o7.projectapp.databinding.ActivityLoginBinding

class FirstScreen : AppCompatActivity() {
    var btn: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        Glide.with(this)
//            .load("https://media.istockphoto.com/id/1381637603/photo/mountain-landscape.jpg?s=612x612&w=0&k=20&c=w64j3fW8C96CfYo3kbi386rs_sHH_6BGe8lAAAFS-y4=")
//            .into(binding.gifimageview);
//        Handler(looper.getMainLooper()).postDelayed(
//            {
//                var intent= Intent(this,)
//            }
//        )
        btn=findViewById<Button>(R.id.nextsecondsc)
        btn?.setOnClickListener {
            var intent= Intent(this, Second_Screen::class.java)
            startActivity(intent)
        }
    }
}