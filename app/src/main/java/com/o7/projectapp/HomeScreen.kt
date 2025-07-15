package com.o7.projectapp

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.o7.projectapp.databinding.ActivityHomeScreenBinding

class HomeScreen : AppCompatActivity(), HCadapter.OnClick {
    lateinit var binding: ActivityHomeScreenBinding
    lateinit var HCadapter: HCadapter
    var NotesList = arrayListOf<HCdataclass>()

    val db = Firebase.firestore
    val collectionName = "Users data"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
       NotesList.add(HCdataclass(title = "My title", description = "My description"))
       NotesList.add(HCdataclass(title = "My Name", description = "My description"))
        NotesList.add(HCdataclass(title = "My description", description = "My description"))
        NotesList.add(HCdataclass(title = "hello", description = "My description"))
        NotesList.add(HCdataclass(title = "branch", description = "My description"))

        HCadapter= HCadapter(NotesList,this)
        binding.rcview.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rcview.adapter= HCadapter
        HCadapter.notifyDataSetChanged()


        binding.flbtn.setOnClickListener {
            var dialog= Dialog(this)

            dialog.setContentView(R.layout.flbbtn_dialog)

            var mytext=dialog.findViewById<EditText>(R.id.input1)
            var mytext2=dialog.findViewById<EditText>(R.id.input1)
            var mytext3=dialog.findViewById<EditText>(R.id.input1)


            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            dialog.show()
            var btnCancel=dialog.findViewById<Button>(R.id.ipbtn2)
            var btnSave=dialog.findViewById<Button>(R.id.ipbtn1)

            btnCancel.setOnClickListener {
                NotesList.add(HCdataclass(title=mytext.text.toString()))
                Toast.makeText(this@HomeScreen,"${mytext.text}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            btnSave.setOnClickListener {
                NotesList.add(HCdataclass(title=mytext.text.toString()))
                Toast.makeText(this@HomeScreen,"${mytext.text}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

    override fun update(position: Int) {
        Toast.makeText(this,"Update Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun delete(position: Int) {
        Toast.makeText(this,"Delete Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}