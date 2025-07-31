package com.o7.projectapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
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


        val genderArray = resources.getStringArray(R.array.gender)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = spinnerAdapter

        val ageArray = resources.getStringArray(R.array.Age)
        val ageSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ageArray)
        ageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.ageSpinner.adapter = ageSpinnerAdapter

        val userTypeArray = resources.getStringArray(R.array.UserType)
        val typeSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeArray)
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.typeSpinner.adapter = typeSpinnerAdapter



        binding.sin.setOnClickListener {
            val fullName = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val gender=binding.spinner.selectedItem.toString().trim()
            val age=binding.ageSpinner.selectedItem.toString().trim()
            val contact=binding.contact.text.toString().trim()
            val userType = binding.typeSpinner.selectedItem.toString().trim()



            if (email.isNotEmpty() && password.isNotEmpty() && gender!= "Select Gender" && age!="Select Age"  && userType!="Select User Type")  {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userId = firebaseAuth.currentUser?.uid.toString().trim()
                            val user = categorylist(
                                fullName = fullName,
                                email = email,
                                contact = contact,
                                gender = gender,
                                age = age,
                                userId = userId,
                                userType = userType
                            )
                            db.collection(collectionName).document(userId).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show()
                                }
                            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeScreen::class.java)
                            intent.putExtra("userType", userType)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Signup unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show()
            }
        


        }
        binding.here.setOnClickListener {
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }


    }
}
