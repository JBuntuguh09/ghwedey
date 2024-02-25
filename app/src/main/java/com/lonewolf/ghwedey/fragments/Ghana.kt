package com.lonewolf.ghwedey.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lonewolf.ghwedey.Login
import com.lonewolf.ghwedey.MainActivity
import com.lonewolf.ghwedey.MainBase
import com.lonewolf.ghwedey.R
import com.lonewolf.ghwedey.adapters.RecyclerViewAdapterList
import com.lonewolf.ghwedey.databinding.FragmentGhanaBinding
import com.lonewolf.ghwedey.resources.ShortCut_To
import com.lonewolf.ghwedey.resources.Storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Ghana.newInstance] factory method to
 * create an instance of this fragment.
 */
class Ghana : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGhanaBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage
    private  var arrayList = ArrayList<HashMap<String, String>>()
    private lateinit var auth: FirebaseAuth
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
        val view = inflater.inflate(R.layout.fragment_ghana, container, false)
        binding = FragmentGhanaBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(requireContext())

        getButtons()
        //getGhanaWords()

        return view
    }

    private fun getGhanaWords() {
        binding.recyclerView.removeAllViews()
        binding.progressBar.visibility = View.VISIBLE
        databaseReference.child("Dictionary").child(binding.edtSearch.text
            .toString().substring(0 until 1).uppercase()).child(binding.edtSearch.text.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrayList.clear()
                    for(father in snapshot.children){
                        val hash = HashMap<String, String>()
                        hash["word"] = father.child("word").value.toString()
                        hash["meaning"] = father.child("definition").value.toString()
                        hash["by"] = father.child("author").value.toString()
                        hash["date"] = father.child("written_on").value.toString()
                        hash["example"] = father.child("example").value.toString()
                        hash["defid"] = father.child("defid").value.toString()
                        hash["tUp"] = father.child("thumbs_up").value.toString()
                        hash["tDown"] = father.child("thumbs_down").value.toString()
                        hash["permalink"] = father.child("permalink").value.toString()
                        hash["current_vote"] = father.child("current_vote").value.toString()
                        hash["type"] = "Ghana"

                        var tUp = 0
                        var tDown = 0
                        if(father.child("thumbs_up").value.toString().isNotEmpty()){
                            tUp = father.child("thumbs_up").value.toString().toInt()
                        }

                        if(father.child("thumbs_down").value.toString().isNotEmpty()){
                            tDown = father.child("thumbs_down").value.toString().toInt()
                        }

                        val tTot = tUp+ tDown
                        val thumbsUp  = Math.round(((tUp.toFloat()/tTot)*100)*100.0)/100.00
                        val thumbsDown = Math.round(((tDown.toFloat()/tTot)*100)*100.0)/100.00

                        hash["rating"] = thumbsUp.toString()


                        arrayList.add(hash)
                    }
                    binding.progressBar.visibility = View.GONE
                    if(arrayList.size>0){
                        ShortCut_To.sortData(arrayList, "rating")
                        val recyclerViewAdapterList = RecyclerViewAdapterList(requireActivity(), arrayList)
                        val linearLayoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.layoutManager = linearLayoutManager
                        binding.recyclerView.itemAnimator = DefaultItemAnimator()
                        binding.recyclerView.adapter = recyclerViewAdapterList

                    }else{
                        Toast.makeText(requireContext(), "No results for ${binding.edtSearch.text.toString()}", Toast.LENGTH_SHORT).show()
                        binding.recyclerView.removeAllViews()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun getButtons() {
        binding.floatingActionButton.setOnClickListener{
            if (auth.currentUser!=null){
                val intent = Intent(requireContext(), MainBase::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(requireContext(), Login::class.java)
                startActivity(intent)
            }
        }

        binding.edtSearch.setOnTouchListener { v: View?, event: MotionEvent ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {

                    getGhanaWords()
                    ShortCut_To.hideKeyboard(requireActivity())
                    return@setOnTouchListener true
                }
            }
            false
        }


    }

    override fun onResume() {
        (activity as MainActivity).binding.txtTopic.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.black))
        //= ContextCompat.getColorStateList(requireContext(), R.color.black)
        (activity as MainActivity).binding.tabSettings.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        (activity as MainActivity).binding.tabLayout.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
        (activity as MainActivity).binding.tabLayout.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.black)

        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Ghana.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Ghana().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}