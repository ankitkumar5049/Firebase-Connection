package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.model.UserData
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var address: EditText
    private lateinit var phone: EditText
    private lateinit var contact: EditText
    private lateinit var submit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        initialise()
        intiClicks()
    }

    private fun initialise() {
        name = findViewById(R.id.edtName)
        address = findViewById(R.id.edtAddress)
        phone = findViewById(R.id.edtPhone)
        contact = findViewById(R.id.edtContact)
        submit = findViewById(R.id.btnSubmit)
    }

    private fun intiClicks() {
        submit.setOnClickListener {
            if (name.text.isEmpty() || address.text.isEmpty() || phone.text.isEmpty() || contact.text.isEmpty()) {
                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            } else {

                val userData = UserData(
                    name.text.toString(),
                    address.text.toString(),
                    contact.text.toString(),
                    phone.text.toString()
                )
                Log.d("TAG", "intiClicks: $userData")
                val db = FirebaseDatabase.getInstance()
                val reference = db.getReference("users")
                reference.child(name.text.toString()).setValue(userData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            name.text.clear()
                            address.text.clear()
                            contact.text.clear()
                            phone.text.clear()
                            Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to upload: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data Uploading failed", Toast.LENGTH_SHORT).show()
                    }

            }
        }
    }
}