package com.o7.projectapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.o7.projectapp.databinding.ActivityHomeScreenBinding

class HomeScreen : AppCompatActivity() {
    lateinit var navController: NavController
//    lateinit var mainmenu: Unit
    lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeScreenBinding
    lateinit var HCadapter: HCadapter
    private lateinit var db: FirebaseFirestore
    var NotesList = arrayListOf<HCdataclass>()
    private var userType: String = "User"
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userType = intent.getStringExtra("userType") ?: "User"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNavigationView = binding.bottomNavigation
        navController = findNavController(R.id.nav_host_fragment_activity_bottom_nav)

        if (savedInstanceState == null) {
            if (userType == "Admin") {
                navController.navigate(R.id.nav_adminHome)
            } else {
                navController.navigate(R.id.nav_userHome)
            }
        }


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_Home -> {
                    if (userType == "Admin") {
                        if (navController.currentDestination?.id != R.id.nav_adminHome) {
                            navController.navigate(R.id.nav_adminHome)
                        }
                    } else {
                        if (navController.currentDestination?.id != R.id.nav_userHome) {
                            navController.navigate(R.id.nav_userHome)
                        }
                    }
                }
                R.id.nav_users -> navController.navigate(R.id.nav_users)
                R.id.nav_asanas -> navController.navigate(R.id.nav_asanas)
                R.id.nav_profile -> navController.navigate(R.id.nav_profile)
            }
            true
        }


        }
    }
