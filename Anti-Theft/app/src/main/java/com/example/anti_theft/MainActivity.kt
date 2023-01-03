package com.example.anti_theft


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {




    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnre= findViewById<Button>(R.id.btnregister)
        btnre.setOnClickListener {
            intent = Intent(applicationContext,Register::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        val useremail = findViewById<EditText>(R.id.loginusername)
        val userpassword = findViewById<EditText>(R.id.loginpassword)

        val btnlogin = findViewById<Button>(R.id.btnlogin)
        btnlogin.setOnClickListener {

            if(useremail.text.isEmpty())
            {
                useremail.setError("Enter Username")
                return@setOnClickListener
            }else if(userpassword.text.isEmpty())
            run {
                userpassword.setError("Enter password")
                return@setOnClickListener
            }



            auth.signInWithEmailAndPassword(useremail.text.toString(),userpassword.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        Toast.makeText(applicationContext,"successfully Login", Toast.LENGTH_LONG).show()
                        val i = Intent(applicationContext, readsms::class.java)
                        startActivity(i)
                    }
                    else
                    {
                        Toast.makeText(applicationContext,"Failed to login", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}