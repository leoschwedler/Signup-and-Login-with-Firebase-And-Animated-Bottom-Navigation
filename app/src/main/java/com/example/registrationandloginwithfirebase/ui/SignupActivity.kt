package com.example.registrationandloginwithfirebase.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.model.UserData
import com.example.registrationandloginwithfirebase.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        binding.buttonRegister.setOnClickListener {
            val signUsername = binding.editTextRegisterUsername.text.toString()
            val signEmail = binding.editTextRegisterEmail.text.toString()
            val signPassword = binding.editTextRegisterPassword.text.toString()
            if (signUsername.isNotEmpty() && signEmail.isNotEmpty() && signPassword.isNotEmpty()){
                signupUser(signUsername,signEmail,signPassword)
            }else{
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun signupUser(username: String, email: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = UserData(id,username,email,password)
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(this@SignupActivity, "Signup Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SignupActivity, "Email already exists", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SignupActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }

            })

    }
}