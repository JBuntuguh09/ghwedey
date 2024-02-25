package com.lonewolf.ghwedey.adapters


import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lonewolf.ghwedey.fragments.Ghana
import com.lonewolf.ghwedey.fragments.Urban


 class MainPageAdapter(fm:FragmentActivity) : FragmentStateAdapter(fm) {

     fun getItemName(position: Int):String{
         return when (position){
             0->"GH We Dey"
             1->"Urban"

             else->""
         }
     }
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->Ghana()
            1->Urban()

            else -> Ghana()
        }
    }




}