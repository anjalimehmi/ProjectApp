package com.o7.projectapp.nav_fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.o7.projectapp.Asanas_adapter
import com.o7.projectapp.Asanas_dataclass
import com.o7.projectapp.HCdataclass
import com.o7.projectapp.R
import com.o7.projectapp.databinding.FragmentAsanasBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Asanas.newInstance] factory method to
 * create an instance of this fragment.
 */
class Asanas : Fragment(), Asanas_adapter.OnClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var currentUserType= "user"
    lateinit var binding: FragmentAsanasBinding
    lateinit var adapter: Asanas_adapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fab: View
    private lateinit var recyclerView: RecyclerView
    val db= Firebase.firestore
    var collectionName="Asanas"
    var itemList=arrayListOf<Asanas_dataclass>()

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
        val view = inflater.inflate(R.layout.fragment_asanas, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        fab = view.findViewById(R.id.flbtn_add_ygcategory)
        fab.visibility = View.GONE
        recyclerView = view.findViewById(R.id.rcview2)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            firestore.collection("Users").document(uid).get()
                .addOnSuccessListener { doc ->
                    currentUserType = doc.getString("userType") ?: "user"


                    if (currentUserType == "user") {
                        fab.visibility = View.GONE
                    } else {
                        fab.visibility = View.VISIBLE
                        fab.setOnClickListener {
                            showAddAsanaDialog()
                        }
                    }
                    loadAsanas()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error loading user type", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }




    private fun loadAsanas() {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()
                for (document in documents) {
                    val asana = document.toObject(Asanas_dataclass::class.java)
                    asana.docID = document.id
                    itemList.add(asana)
                }
               adapter = Asanas_adapter(itemList, this, currentUserType)
                recyclerView.adapter=adapter

//                adapter.notifyItemChanged(position)
//                adapter.notifyItemRemoved(position)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAddAsanaDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.asanafabbtn_dialog)

        val edtName = dialog.findViewById<EditText>(R.id.edttext1)
        val edtDescription = dialog.findViewById<EditText>(R.id.edttext2)
        val btnSave = dialog.findViewById<Button>(R.id.asSave_btn)
        val btnCancel = dialog.findViewById<Button>(R.id.asCancel_btn)

        btnSave.setOnClickListener {
            val name = edtName.text.toString().trim()
            val description = edtDescription.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a category name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newAsana = hashMapOf(
                "name" to name,
                "description" to description
            )

            db.collection(collectionName)
                .add(newAsana)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Asana added", Toast.LENGTH_SHORT).show()
                    loadAsanas() // Refresh list
                    dialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to add Asana", Toast.LENGTH_SHORT).show()
                }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



    override fun Edit(position: Int) {
        if (currentUserType == "admin") {
            val item = itemList[position]
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.editcategory_dialog)

            val edtName = dialog.findViewById<EditText>(R.id.edtText2)
            val saveBtn = dialog.findViewById<Button>(R.id.SSave)
            val cancelBtn = dialog.findViewById<Button>(R.id.CCancel)

            edtName.setText(item.name)

            saveBtn.setOnClickListener {
                val newName = edtName.text.toString().trim()
                if (newName.isEmpty()) {
                    Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                item.docID?.let { docID ->
                    db.collection(collectionName).document(docID)
                        .update("name", newName)
                        .addOnSuccessListener {
                            item.name = newName
                            adapter.notifyItemChanged(position)
                            Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
    override fun Delete(position: Int) {
        if (currentUserType == "Admin") {
            val item = itemList[position]
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.deleteusers_dialog)

            val sureDelete = dialog.findViewById<TextView>(R.id.SureText)
            val yesBtn = dialog.findViewById<Button>(R.id.Yes)
            val noBtn = dialog.findViewById<Button>(R.id.No)

            sureDelete.text = "Are you sure you want to delete this asana?"

            yesBtn.setOnClickListener {
                item.docID?.let { docID ->
                    db.collection(collectionName).document(docID)
                        .delete()
                        .addOnSuccessListener {
                            itemList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
            noBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }
    override fun onItemClick(position: Int) {
        val clickedAsana = itemList[position]
        val bundle = Bundle().apply {
            putString("asanaName", clickedAsana.name)
            putString("asanaDescription", clickedAsana.description)
        }
        findNavController().navigate(R.id.nav_exercise_detail, bundle)
}

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Asanas.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String,  currentUserType: String) =
            Asanas().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString("currentUserType", currentUserType)
                }
            }
    }
}