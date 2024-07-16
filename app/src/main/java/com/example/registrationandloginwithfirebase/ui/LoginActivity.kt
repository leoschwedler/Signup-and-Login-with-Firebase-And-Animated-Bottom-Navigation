package com.example.registrationandloginwithfirebase.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.model.UserData
import com.example.registrationandloginwithfirebase.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.textViewRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonLogin.setOnClickListener {
            val loginEmail = binding.editTextLoginEmail.text.toString()
            val loginPassword = binding.editTextLoginPassword.text.toString()
            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()){
                loginUser(loginEmail,loginPassword)
            }else{
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSpanshot in snapshot.children) {
                            val userData = userSpanshot.getValue(UserData::class.java)
                            if (userData != null && userData.password == password) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Database Error: ${error.message}",
                        Toast.LENGTH_SHORT
                    )
                }

            })
    }
}