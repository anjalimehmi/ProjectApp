package com.o7.projectapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.o7.projectapp.nav_fragments.Users

class itemclickusers_details : AppCompatActivity() {
    var btn: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_itemclickusers_details)


        val name = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")

        findViewById<TextView>(R.id.nameuser).text = "Username: $name"
        findViewById<TextView>(R.id.emailuser).text = "Email: $email"


        }

    }
