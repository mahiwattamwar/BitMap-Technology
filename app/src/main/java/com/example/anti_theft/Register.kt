package com.example.anti_theft

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.preference.PreferenceManager
import com.google.android.material.internal.ContextUtils.getActivity


class Register : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<EditText>(R.id.edname)
        val pin = findViewById<EditText>(R.id.edpin)
        val email = findViewById<EditText>(R.id.edemail)
        val password = findViewById<EditText>(R.id.edpassword)
        val btnr = findViewById<Button>(R.id.btnre)



        auth = FirebaseAuth.getInstance()


        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("User")

        btnr.setOnClickListener {

            if(name.text.isEmpty())
            {
                name.setError("Enter Username")
                return@setOnClickListener
            }else if(pin.text.isEmpty())
            {
                pin.setError("Enter pin")
                return@setOnClickListener
            }else if(email.text.isEmpty())
            {
                email.setError("Enter Emailid ")
                return@setOnClickListener
            }else if(password.text.isEmpty())
            {
                password.setError("Enter password")
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentuser = auth.currentUser
                        val currentUserdb = databaseReference?.child((currentuser?.uid!!))
                        currentUserdb?.child("Username")?.setValue(name.text.toString())
                        currentUserdb?.child("number")?.setValue(pin.text.toString())

                        Toast.makeText(applicationContext, "success", Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                    }
                }



            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = prefs.edit()

            editor.putString("pin",pin.text.toString())
            editor.putString("email",email.text.toString())


            editor.apply()
            editor.commit()
        }


    }

}
