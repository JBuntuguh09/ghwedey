package com.lonewolf.ghwedey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.ghwedey.databinding.ActivityRegisterBinding
import com.lonewolf.ghwedey.resources.Constant
import com.lonewolf.ghwedey.resources.ShortCut_To
import com.lonewolf.ghwedey.resources.Storage

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(this)

        getButtons()
    }

    private fun getButtons() {
        binding.btnSubmit.setOnClickListener {
            if (binding.edtName.text.toString().isEmpty()){
                Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show()
            }else if (binding.edtPhone.text.toString().isEmpty()){
                Toast.makeText(this, "Enter your Phone number", Toast.LENGTH_SHORT).show()
            }else if (binding.edtEmail.text.toString().isEmpty()){
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
            }else if (!binding.edtEmail.text.toString().contains("@")){
                Toast.makeText(this, "Enter a properly formatted email", Toast.LENGTH_SHORT).show()
            }else if (binding.edtPassword.text.toString().isEmpty()){
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
            }else if (binding.edtPassword.text.toString().length<8){
                Toast.makeText(this, "Password should be 8 characters or more", Toast.LENGTH_SHORT).show()
            }else if (binding.edtPassword.text.toString() != binding.edtConfirm.text.toString()){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }else{
                registerTo()
            }
        }
    }

    private fun registerTo(){
        var userId =""
        binding.progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(Constant.username, Constant.password)
            .addOnSuccessListener {
                userId = auth.currentUser!!.uid
                storage.uSERID = userId
                auth.createUserWithEmailAndPassword(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
                    .addOnSuccessListener {
                        val hash = HashMap<String, String>()
                        hash["name"] = binding.edtName.text.toString()
                        hash["email"] = binding.edtEmail.text.toString()
                        hash["createddatetime"] = ShortCut_To.getCurrentDatewithTime()
                        hash["userId"] = userId
                        hash["Phone"] = binding.edtPhone.text.toString()

                        storage.uSERNAME = binding.edtName.text.toString()
                        databaseReference.child("Users").child(userId).setValue(hash).addOnSuccessListener {
                            Toast.makeText(this, "Successfully signed up", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this, MainActivity::class.java)
//                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }

                    }.addOnFailureListener {
                        auth.signOut()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}