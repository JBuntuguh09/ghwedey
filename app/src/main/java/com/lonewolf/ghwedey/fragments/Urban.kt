package com.lonewolf.ghwedey.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.lonewolf.ghwedey.MainActivity
import com.lonewolf.ghwedey.R
import com.lonewolf.ghwedey.adapters.RecyclerViewAdapterList
import com.lonewolf.ghwedey.databinding.FragmentUrbanBinding
import com.lonewolf.ghwedey.resources.API
import com.lonewolf.ghwedey.resources.ShortCut_To
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Urban.newInstance] factory method to
 * create an instance of this fragment.
 */
class Urban : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentUrbanBinding
    private var arrayList = ArrayList<HashMap<String, String>>()


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
        val view = inflater.inflate(R.layout.fragment_urban, container, false)
        binding = FragmentUrbanBinding.bind(view)
       // linearLayoutManager = LinearLayoutManager(requireActivity())


        getButtons()
        return view
    }

    private fun getButtons() {
        binding.edtSearch.setOnTouchListener { v: View?, event: MotionEvent ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {

                    getApi()
                    ShortCut_To.hideKeyboard(requireActivity())
                    return@setOnTouchListener true
                }
            }
            false
        }

    }

    private fun getApi() {

        binding.progressBar.visibility = View.VISIBLE
       GlobalScope.launch {
           val result : String? = API.getUrban(binding.edtSearch.text.toString())
           println("mooo "+result)
           withContext(Dispatchers.Main){

               setInfo(result)
           }

       }
    }

    private fun  setInfo(result : String?){
        arrayList.clear()
        try {
            val jsonobject = JSONObject(result)

            val jsonArray = JSONArray(jsonobject.getString("list"))
            for(a in 0 until jsonArray.length()){
                val jObject = jsonArray.getJSONObject(a)
                val hash = HashMap<String, String>()
                hash["word"] = jObject.getString("word")
                hash["meaning"] = jObject.getString("definition")
                hash["tUp"] = jObject.getString("thumbs_up")
                hash["tDown"] = jObject.getString("thumbs_down")
                hash["date"] = jObject.getString("written_on")
                hash["example"] = jObject.getString("example")
                hash["by"] = jObject.getString("author")
                hash["type"] = "Urban"
                var tUp = 0
                var tDown = 0
                if(jObject.getString("thumbs_up").isNotEmpty()){
                    tUp = jObject.getString("thumbs_up")!!.toInt()
                }

                if(jObject.getString("thumbs_down").isNotEmpty()){
                    tDown = jObject.getString("thumbs_down").toInt()
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

                binding.recyclerView.removeAllViews()

            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        (activity as MainActivity).binding.txtTopic.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
        (activity as MainActivity).binding.tabSettings.setBackgroundColor(resources.getColor(R.color.colorSecondary))
        (activity as MainActivity).binding.tabLayout.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.colorSecondary)
        (activity as MainActivity).binding.tabLayout.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.white)
        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Urban.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Urban().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}