package com.o7.projectapp

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.o7.projectapp.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val db = Firebase.firestore
    var collectionName = "Users"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()

        binding.sin.setOnClickListener {
            val fullName=binding.name.text.toString().trim()
            var email = binding.email.text.toString().trim()
            var password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task  ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                            try {
                                val userId=firebaseAuth.currentUser?.uid.toString().trim()

                                db.collection(collectionName).document(userId).set(categorylist(email = email,fullName,userId))
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Add Failed", Toast.LENGTH_SHORT).show()
                                    }
                            } catch (e: Exception) {
                                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                            var intent = Intent(this, HomeScreen::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(this, "signup unsuccessful", Toast.LENGTH_SHORT).show()
                        }

                    }
            } else {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.here.setOnClickListener {
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

                    }


                }

