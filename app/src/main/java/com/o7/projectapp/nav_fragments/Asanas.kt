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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
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
    lateinit var binding: FragmentAsanasBinding
    lateinit var adapter: Asanas_adapter
    private lateinit var firebaseAuth: FirebaseAuth
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
        binding = FragmentAsanasBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        adapter = Asanas_adapter(itemList, this)
        binding.rcview2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcview2.adapter = adapter
//        adapter.notifyDataSetChanged()

        db.collection("Asanas")
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()
                for (document in documents){
                    val user=document.toObject(Asanas_dataclass::class.java)
                    user.docID = document.id
                    itemList.add(user)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to load users", Toast.LENGTH_SHORT).show()
            }

        binding.flbtnAddYgcategory.setOnClickListener {
            var dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.asanafabbtn_dialog)

            var AsanaName = dialog.findViewById<EditText>(R.id.edttext1)
//            var Purpose = dialog.findViewById<EditText>(R.id.edttext2)
//            var Duration = dialog.findViewById<EditText>(R.id.edttext3)
//            var Benefits = dialog.findViewById<EditText>(R.id.edttext4)
            var Save = dialog.findViewById<Button>(R.id.asSave_btn)
            var Cancel = dialog.findViewById<Button>(R.id.asCancel_btn)


            Save.setOnClickListener {
                val AsaneInfo = AsanaName.text.toString().trim()
//                val PurposeInfo = Purpose.text.toString().trim()
//                val DurationInfo = Duration.text.toString().trim()
//                val BenefitsInfo = Benefits.text.toString().trim()


                if (AsaneInfo.isNotEmpty() ) {
                    val newAsana =
                        Asanas_dataclass(AsaneInfo)

                    db.collection("Asanas")
                        .add(newAsana)
                        .addOnSuccessListener {documentReference ->
                            newAsana.docID = documentReference.id
                            Toast.makeText(requireContext(), "Saved to Firestore", Toast.LENGTH_SHORT).show()
                            itemList.add(newAsana)
                            adapter.notifyItemInserted(itemList.size - 1)
                            dialog.dismiss()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Fill Information", Toast.LENGTH_SHORT).show()
                }

            }
            Cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        return binding.root
    }

    override fun Edit(position: Int) {
        val CategoryItem = itemList[position]

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.editcategory_dialog)

        val edtCategory = dialog.findViewById<EditText>(R.id.edtText2)
        val Savebtn = dialog.findViewById<Button>(R.id.SSave)
        val CancelBtn = dialog.findViewById<Button>(R.id.CCancel)

        edtCategory.setText(CategoryItem.name)

        Savebtn.setOnClickListener {
            val newCategoryname = edtCategory.text.toString().trim()


            if (newCategoryname.isEmpty()) {
                Toast.makeText(requireContext(), "Username cannot be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val docID = CategoryItem.docID
            if (docID != null) {
                db.collection(collectionName).document(docID)
                    .update("Username", newCategoryname)
                    .addOnSuccessListener {
                        CategoryItem.name = newCategoryname
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
        CancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun Delete(position: Int) {
        var dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.deleteusers_dialog)

        val SureDelete=dialog.findViewById<TextView>(R.id.SureText)
        val btnyes=dialog.findViewById<Button>(R.id.Yes)
        val btnNo=dialog.findViewById<Button>(R.id.No)

        SureDelete.text="Are you sure you want to delete this user?"

        btnyes.setOnClickListener {
            itemList.removeAt(position)
            adapter.notifyItemRemoved(position)
            Toast.makeText(requireContext(), "Category removed", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }



    override fun onItemClick(position: Int) {
            findNavController().navigate(R.id.nav_exercise_detail)
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
        fun newInstance(param1: String, param2: String) =
            Asanas().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}