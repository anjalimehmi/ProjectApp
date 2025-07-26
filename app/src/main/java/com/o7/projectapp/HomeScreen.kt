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

class HomeScreen :AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var mainmenu: Unit
    lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeScreenBinding
    lateinit var HCadapter: HCadapter
    private lateinit var db: FirebaseFirestore
    var NotesList = arrayListOf<HCdataclass>()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView=findViewById(R.id.bottom_navigation)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navController=findNavController(R.id.nav_host_fragment_activity_bottom_nav)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home->navController.navigate(R.id.nav_home)
                R.id.nav_profile->navController.navigate(R.id.nav_profile)
                R.id.nav_users->navController.navigate(R.id.nav_users)
                R.id.nav_asanas->navController.navigate(R.id.nav_asanas)
            }
            true
        }

    }

}