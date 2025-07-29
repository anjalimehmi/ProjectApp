package com.o7.projectapp.nav_fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.o7.projectapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var textContact: TextView
    private lateinit var textGender: TextView
    private lateinit var textAge: TextView
    private lateinit var btnEditProfile: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()


        textName=view.findViewById(R.id.ed1)
        textEmail=view.findViewById(R.id.ed2)
        textContact=view.findViewById(R.id.ed3)
        textGender=view.findViewById(R.id.ed4)
        textAge=view.findViewById(R.id.ed5)
        btnEditProfile=view.findViewById(R.id.btnEditProfile)

        loadProfileData()

        btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        return view
    }

    private fun loadProfileData() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("Users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    textName.text = document.getString("fullName")
                    textEmail.text = document.getString("email")
                    textContact.text = document.getString("contact")
                    textGender.text = document.getString("gender")
                    textAge.text = document.getString("age")
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEditProfileDialog() {
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.editprofile_dialog)

        val UpdatedName = dialog.findViewById<EditText>(R.id.edit1)
        val UpdatedContact = dialog.findViewById<EditText>(R.id.edit3)
        val UpdatedAge = dialog.findViewById<EditText>(R.id.edit5)
        val textEEmail = dialog.findViewById<TextView>(R.id.edit2)
        val textGGender = dialog.findViewById<TextView>(R.id.edit4)
        val btnSSave = dialog.findViewById<Button>(R.id.svbtn)
        val btnCCancel = dialog.findViewById<Button>(R.id.canbtn)

        dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )


        UpdatedName.setText(textName.text.toString())
        UpdatedContact.setText(textContact.text.toString())
        UpdatedAge.setText(textAge.text.toString())
        textEEmail.text = textEmail.text.toString()
        textGGender.text = textGender.text.toString()

        btnSSave.setOnClickListener {
            val updatedName = UpdatedName.text.toString().trim()
            val updatedContact = UpdatedContact.text.toString().trim()
            val updatedAge = UpdatedAge.text.toString().trim()

            val userId = firebaseAuth.currentUser?.uid ?:
            return@setOnClickListener

            val updates = mapOf(
                "fullName" to updatedName,
                "contact" to updatedContact,
                "age" to updatedAge
            )

            firestore.collection("Users").document(userId)
                .update(updates)
                .addOnSuccessListener {
                    textName.text = updatedName
                    textContact.text = updatedContact
                    textAge.text = updatedAge

                    Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                }
        }

        btnCCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}