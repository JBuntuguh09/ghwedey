package com.lonewolf.ghwedey

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.ghwedey.adapters.MainPageAdapter
import com.lonewolf.ghwedey.databinding.ActivityMainBinding
import com.lonewolf.ghwedey.resources.Storage

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var tabs : TabLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Storage(this)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val adapter = MainPageAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0

        tabs = binding.tabLayout
        TabLayoutMediator(tabs, binding.viewPager) { tab, position ->
            tab.text = adapter.getItemName(position)

        }.attach()

        getButtons()
    }

    private fun getButtons() {

    }

    override fun onResume() {
        //auth = FirebaseAuth.getInstance()
        if (auth.currentUser==null){
            binding.imgLogout.imageTintList = ContextCompat.getColorStateList(this, R.color.black)
            binding.imgLogout.setOnClickListener {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }else{
            binding.imgLogout.imageTintList = ContextCompat.getColorStateList(this, R.color.red)
            binding.imgLogout.setOnClickListener {
                auth.signOut()
                binding.imgLogout.imageTintList = ContextCompat.getColorStateList(this, R.color.black)
                binding.imgLogout.setOnClickListener {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }
            }
        }

        super.onResume()
    }
}