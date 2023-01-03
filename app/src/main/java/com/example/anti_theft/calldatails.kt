package com.example.anti_theft

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class calldatails : AppCompatActivity() {

    var msg:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calldatails)


        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CALL_LOG),
            PackageManager.PERMISSION_GRANTED
        )


        try {
            val mCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null)
            val number = mCursor.getColumnIndex(CallLog.Calls.NUMBER)
            val date = mCursor.getColumnIndex(CallLog.Calls.DATE)
            val duration = mCursor.getColumnIndex(CallLog.Calls.DURATION)
            val type = mCursor.getColumnIndex(CallLog.Calls.TYPE)

            val sb = StringBuffer()
            while (mCursor.moveToNext()) {
                val phnumber = mCursor.getString(number)
                val callDuration = mCursor.getString(duration)
                val callType = mCursor.getString(type)
                val callDate = mCursor.getString(date)
                val d = Date(callDate.toLong())
                var CalltypeStr = ""
                when (callType.toInt()) {
                    CallLog.Calls.OUTGOING_TYPE -> CalltypeStr = "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> CalltypeStr = "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> CalltypeStr = "MISSED"
                }
                sb.append("PHONE NUMBER ").append(phnumber)
                sb.append(System.getProperty("line.separator"))
                sb.append("call duration ").append(callDuration)
                sb.append(System.getProperty("line.separator"))
                sb.append("type ").append(CalltypeStr)
                sb.append(System.getProperty("line.separator"))
                sb.append("call date ").append(d)
                sb.append(System.getProperty("line.separator"))
                sb.append("---------------------------------")
                sb.append(System.getProperty("line.separator"))
            }
            mCursor.close()

             msg = sb.toString()

        }catch (e:Exception){
            e.printStackTrace()
        }



        val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(
            baseContext
        )
        val value = mSharedPreference.getString("email", "DEFAULT")


        val s = locationsend(applicationContext, value.toString(), "Call Log ", msg)
        s.execute()

        val data = FirebaseDatabase.getInstance().reference.child("call")

        val service = call(msg)
        data.push().setValue(service)
    }
}