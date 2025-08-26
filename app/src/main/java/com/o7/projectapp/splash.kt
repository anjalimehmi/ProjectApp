package com.o7.projectapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.o7.projectapp.databinding.ActivitySplashBinding

class splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        firebaseAuth= FirebaseAuth.getInstance()
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Log.d("ThemeCheck", "Dark Mode is active")
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                Log.d("ThemeCheck", "Light Mode is active")
            }
            else -> {
                Log.d("ThemeCheck", "Unknown Mode")
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val currentUser=firebaseAuth.currentUser
                if (currentUser!=null){
                    val userId=currentUser.uid
                    db.collection("Users").document(userId).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val userType = document.getString("userType") ?: "User"
                                val intent = Intent(this, HomeScreen::class.java)
                                intent.putExtra("userType", userType)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, FirstScreen::class.java))
                                finish()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to fetch user type", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, FirstScreen::class.java))
                            finish()
                        }
                } else {
                    startActivity(Intent(this, FirstScreen::class.java))
                    finish()
                }
            },2000)
    }

}

