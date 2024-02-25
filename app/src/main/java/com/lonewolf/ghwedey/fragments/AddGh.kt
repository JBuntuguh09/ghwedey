package com.lonewolf.ghwedey.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import com.lonewolf.ghwedey.R
import com.lonewolf.ghwedey.databinding.FragmentAddGhBinding
import com.lonewolf.ghwedey.databinding.LayoutAddBinding
import com.lonewolf.ghwedey.resources.ShortCut_To
import com.lonewolf.ghwedey.resources.Storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddGh.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddGh : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: FragmentAddGhBinding
    private lateinit var storage: Storage
    private  var arrayList = ArrayList<HashMap<String, String>>()
    private  var listSub = ArrayList<String>()


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
        val view = inflater.inflate(R.layout.fragment_add_gh, container, false)
        binding = FragmentAddGhBinding.bind(view)
        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(requireContext())


        getButtons()
        return view
    }

    private fun getButtons() {
        val adapter = ArrayAdapter(requireContext(), R.layout.layout_spinner_list, ShortCut_To.getCategory())
        adapter.setDropDownViewResource(R.layout.layout_dropdown)
        binding.spinCategory.adapter = adapter

        binding.btnSubmit.setOnClickListener {
            if(binding.edtWord.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Enter the word or phrase", Toast.LENGTH_SHORT).show()
            }else if(binding.edtMeaning.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Enter the meaning", Toast.LENGTH_SHORT).show()
            }else if(binding.edtExample.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Enter the word or phrase used in an example", Toast.LENGTH_SHORT).show()
            }else if(binding.edtWord.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Enter the word or phrase", Toast.LENGTH_SHORT).show()
            }else if(binding.edtWord.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Enter the word or phrase", Toast.LENGTH_SHORT).show()
            }else{
                clean(listSub)
            }
        }

        binding.imgAddAlt.setOnClickListener {
            val layoutInflater = LayoutInflater.from(requireContext())
            val view = layoutInflater.inflate(R.layout.layout_add, binding.linMain, false)
            val bind = LayoutAddBinding.bind(view)
            val tag = listSub.size
            bind.edtAlt.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    listSub[tag] = p0.toString()
                }

            })

            bind.edtAlt.setOnTouchListener { v: View?, event: MotionEvent ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= bind.edtAlt.right - bind.edtAlt.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        listSub[tag] = ""
                        binding.linMain.removeView(view)
                        ShortCut_To.hideKeyboard(requireActivity())
                        return@setOnTouchListener true
                    }
                }
                false
            }

            listSub.add(tag, "")
            binding.linMain.addView(view)
        }
    }

    private fun sendData(id: String, word: String) {
        val hash = HashMap<String, String>()
        hash["word"] = word
        hash["definition"] = binding.edtMeaning.text.toString()
        hash["author"] = storage.uSERNAME!!
        hash["written_on"] = ShortCut_To.getCurrentDatewithTime()
        hash["example"] = binding.edtExample.text.toString()
        hash["defid"] = id
        hash["authorId"] = storage.uSERID!!
        hash["thumbs_up"] = "0"
        hash["thumbs_down"] = "0"
        hash["permalink"] = ""
        hash["current_vote"] = "0"
        hash["category"] = binding.spinCategory.selectedItem.toString()
//         var path = word.substring(0 until 1).uppercase()
//        if(word.length>1){
//            path = "$path/${word.substring(0 until 1).uppercase()}"
//        }
        databaseReference.child("Dictionary").child(word.substring(0 until 1).uppercase())
            .child(word).child(id).setValue(hash).addOnSuccessListener {

            }.addOnFailureListener{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }


    }

    private fun sendCategory(id:String, word : String, category: String) {
        val hash = HashMap<String, String>()
        hash["word"] = word
        hash["definition"] = binding.edtMeaning.text.toString()
        hash["author"] = storage.uSERNAME!!
        hash["written_on"] = ShortCut_To.getCurrentDatewithTime()
        hash["example"] = binding.edtExample.text.toString()
        hash["defid"] = id
        hash["category"] = category

//         var path = word.substring(0 until 1).uppercase()
//        if(word.length>1){
//            path = "$path/${word.substring(0 until 1).uppercase()}"
//        }
        databaseReference.child("Category").child(category)
            .child(word.substring(0 until 1).uppercase())
            .child(word).child(id).setValue(hash).addOnSuccessListener {

            }.addOnFailureListener{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }


    }

    private fun clean(list : ArrayList<String>){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSubmit.isEnabled = false
        list.add(binding.edtWord.text.toString())
        var numId = 0
        databaseReference.child("Identifiers").child("Word_Id")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                     numId = (snapshot.value.toString().toInt() + 1)

                    for(a in 0 until list.size){

                        if(!list[a].equals("")){
                            numId += a
                            val id = "$numId-GHWD-${ShortCut_To.getRandomNumber(10000, 100000)}"
                            sendData(id, list[a])
                            if(binding.spinCategory.selectedItemPosition==2){
                                sendCategory(id, list[a], "Insult")
                                sendCategory(id, list[a], "Curse Words")
                            }else{
                                sendCategory(id, list[a], binding.spinCategory.selectedItem.toString())
                            }

                            if(a>=list.size-1){
                                databaseReference.child("Identifiers").child("Word_Id").setValue(numId)
                                binding.edtWord.setText("")
                                binding.edtMeaning.setText("")
                                binding.edtExample.setText("")
                                binding.btnSubmit.isEnabled = true
                                listSub.clear()
                                binding.linMain.removeAllViews()
                                binding.progressBar.visibility = View.GONE

                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddGh.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddGh().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}