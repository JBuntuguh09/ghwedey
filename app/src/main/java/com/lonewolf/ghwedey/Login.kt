package com.lonewolf.ghwedey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.ghwedey.databinding.ActivityLoginBinding
import com.lonewolf.ghwedey.resources.Constant
import com.lonewolf.ghwedey.resources.Storage

class Login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        storage = Storage(this)
        databaseReference = FirebaseDatabase.getInstance().reference

        getButtons()
    }

    private fun getButtons() {
        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()

        }

        binding.btnSubmit.setOnClickListener {
            if(binding.edtEmail.text.toString().isEmpty()){
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                binding.edtPassword.error = "Enter email"
            }else if(!binding.edtEmail.text.toString().contains("@")){
                Toast.makeText(this, "Enter a properly formatted email", Toast.LENGTH_SHORT).show()
                binding.edtPassword.error = "Enter a properly formatted email"
            }else if(binding.edtPassword.text.toString().isEmpty()){
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                binding.edtPassword.error = "Enter password"
            }else {
                loginTo()
            }
        }
    }

    private fun loginTo() {
        binding.progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
            .addOnSuccessListener {
                auth.signInWithEmailAndPassword(Constant.username, Constant.password)
                    .addOnSuccessListener {
                        binding.progressBar.visibility = View.GONE
                        finish()
                    }.addOnFailureListener {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}