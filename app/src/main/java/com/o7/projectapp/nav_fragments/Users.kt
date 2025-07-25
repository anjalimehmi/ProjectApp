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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.o7.projectapp.HCadapter
import com.o7.projectapp.HCdataclass
import com.o7.projectapp.R
import com.o7.projectapp.databinding.FragmentUsersBinding
import com.o7.projectapp.itemclickusers_details

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Users.newInstance] factory method to
 * create an instance of this fragment.
 */
class Users : Fragment(), HCadapter.OnClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentUsersBinding
    lateinit var adapter: HCadapter
    private lateinit var firebaseAuth: FirebaseAuth
    val db = Firebase.firestore
    var collectionName = "Users"
    var itemList = arrayListOf<HCdataclass>()

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
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()


        adapter = HCadapter(itemList, this)
        binding.rcview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcview.adapter = adapter
//        adapter.notifyDataSetChanged()

        db.collection("Users")
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()
                for (document in documents) {
                    val user = document.toObject(HCdataclass::class.java)
                    user.docID = document.id
                    itemList.add(user)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to load users", Toast.LENGTH_SHORT).show()
            }

        binding.flbtnAddUser.setOnClickListener {
            var dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.flbbtn_dialog)


            var Username = dialog.findViewById<EditText>(R.id.input1)
            var password = dialog.findViewById<EditText>(R.id.input2)
            var Email = dialog.findViewById<EditText>(R.id.input3)
            var btnSave = dialog.findViewById<Button>(R.id.ipbtn1)
            var btnCancel = dialog.findViewById<Button>(R.id.ipbtn2)



            btnSave.setOnClickListener {
                val ipuser = Username.text.toString().trim()
                val ippassword = password.text.toString().trim()
                val ipemail = Email.text.toString().trim()

                if (ipuser.isNotEmpty() && ippassword.isNotEmpty() && ipemail.isNotEmpty()) {
                    val newUser = HCdataclass(ipuser, ippassword, ipemail)

                    db.collection("Users")
                        .add(newUser)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Saved to Firestore",
                                Toast.LENGTH_SHORT
                            ).show()
                            itemList.add(newUser)
                            adapter.notifyItemInserted(itemList.size - 1)
                            dialog.dismiss()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT)
                        .show()
                }

//                itemList.add(HCdataclass(Username = mytext1.text.toString(),
//                     Password = mytext2.text.toString(),
//                    Email = mytext3.text.toString()))
//                Toast.makeText(requireContext(),"yes", Toast.LENGTH_SHORT).show()
//                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
//                itemList.add(HCdataclass(Username = mytext1.text.toString(),
//                    Password = mytext2.text.toString(),
//                    Email = mytext3.text.toString()))
//                Toast.makeText(requireContext(),"no", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            dialog.show()
        }
        return binding.root
    }




    override fun update(position: Int) {
        val selectedUser = itemList[position]


        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.updateusers_dialog)

        val edtUsername = dialog.findViewById<EditText>(R.id.edt1)
        val edtEmail = dialog.findViewById<TextView>(R.id.edt3)
        val updateBtn = dialog.findViewById<Button>(R.id.updatebtn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelbtn)

        edtUsername.setText(selectedUser.Username)
        edtEmail.setText(selectedUser.Email)
        edtEmail.isEnabled = false

        updateBtn.setOnClickListener {
            val newUsername = edtUsername.text.toString().trim()

            if (newUsername.isEmpty()) {
                Toast.makeText(requireContext(), "Username cannot be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val docID = selectedUser.docID
            if (docID != null) {
                db.collection(collectionName).document(docID)
                    .update("Username", newUsername)
                    .addOnSuccessListener {
                        selectedUser.Username = newUsername
                        adapter.notifyItemChanged(position)
                        Toast.makeText(requireContext(), "Username updated", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(requireContext(), "Document ID missing", Toast.LENGTH_SHORT).show()
            }
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun delete(position: Int) {
        var dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.deleteusers_dialog)

        val SureDelete=dialog.findViewById<TextView>(R.id.SureText)
        val btnyes=dialog.findViewById<Button>(R.id.Yes)
        val btnNo=dialog.findViewById<Button>(R.id.No)

        SureDelete.text="Are you sure you want to delete this user?"

        btnyes.setOnClickListener {
            itemList.removeAt(position)
            adapter.notifyItemRemoved(position)
            Toast.makeText(requireContext(), "User removed", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onItemClick(position: Int) {
        val selectedUser = itemList[position]

        val intent = Intent(requireContext(), itemclickusers_details::class.java)
        intent.putExtra("username", selectedUser.Username)
        intent.putExtra("email", selectedUser.Email)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Users.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                Users().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}

