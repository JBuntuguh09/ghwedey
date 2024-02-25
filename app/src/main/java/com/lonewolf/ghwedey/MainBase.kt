package com.lonewolf.ghwedey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lonewolf.ghwedey.databinding.ActivityMainBaseBinding
import com.lonewolf.ghwedey.fragments.AddGh

class MainBase : AppCompatActivity() {
    private lateinit var binding: ActivityMainBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_main_base)

        binding = ActivityMainBaseBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getButtons()
    }

    private fun getButtons() {
        navTo(AddGh(), "Add New", "Main", 0)
    }

    fun navTo(frag : Fragment, page:String, prev: String, returnable : Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if(returnable==1){
            fragmentTransaction.replace(R.id.frameLayout, frag, page).addToBackStack(page)
        }else if(returnable==2){
            fragmentTransaction.replace(R.id.frameLayout, frag, page).addToBackStack(prev)
        }else if(returnable==3){
            fragmentTransaction.replace(R.id.frameLayout, frag).addToBackStack("prev")
        }else{
            fragmentTransaction.replace(R.id.frameLayout, frag, page)
        }

        fragmentTransaction.commit()
        binding.txtTopic.text = page


    }
}