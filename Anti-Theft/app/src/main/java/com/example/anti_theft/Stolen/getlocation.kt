package com.example.anti_theft.Stolen

import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.media.AudioManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.anti_theft.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Context
import java.io.IOException
import java.util.*

class getlocation : AppCompatActivity() {

    var lat: String? = null
    var log: String? = null
    var address: String? = null
    var msg: String? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getlocation)


//        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val am = getSystemService(android.content.Context.AUDIO_SERVICE) as AudioManager

        am.ringerMode = AudioManager.RINGER_MODE_NORMAL
        Toast.makeText(applicationContext, "Ring Mode", Toast.LENGTH_SHORT).show()



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        fusedLocationProviderClient!!.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(this@getlocation, Locale.getDefault())
                try {
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)


                    lat = addresses[0].latitude.toString()
                    log = addresses[0].longitude.toString()
                    address = addresses.get(0).getAddressLine(0)
                    Toast.makeText(applicationContext, address.toString(), Toast.LENGTH_LONG).show()

                    val sb = StringBuffer()
                    sb.append("Current location latitude").append(lat)
                    sb.append(System.getProperty("line.separator"))
                    sb.append("current location longitutde").append(log)
                    sb.append(System.getProperty("line.separator"))
                    sb.append("Current Address").append(address)
                    msg = sb.toString()


                    val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(
                        baseContext
                    )
                    val value = mSharedPreference.getString("email", "DEFAULT")


                    val se =
                        locationsend(applicationContext, value.toString(), "Current location", msg)
                    se.execute()


                    val data = FirebaseDatabase.getInstance().reference.child("location")

                    val service = call(msg)
                    data.push().setValue(service)


                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }


        val capture = findViewById<Button>(R.id.capture)
        capture.setOnClickListener {
            val intent = Intent(applicationContext, Autocapture::class.java)
            startActivity(intent)


            val btncall = findViewById<Button>(R.id.btncall)
            btncall.setOnClickListener {
                val intent = Intent(applicationContext, calldatails::class.java)
                startActivity(intent)
            }

        }
    }
}