package com.example.anti_theft

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.lang.System.out

class takepicture : AppCompatActivity() {


    val PERMISSION_CODE :Int=1000
    var imageuri:Uri?=null
    var IMAGE_CAPTURE_CODE:Int=1001
    var img:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_takepicture)

        val btn = findViewById<Button>(R.id.button2)
        img = findViewById<ImageView>(R.id.imageView)

        btn.setOnClickListener {
            if((checkSelfPermission(android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) || (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){

                val permission = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission,PERMISSION_CODE)
//                requestpermission(permission,PERMISSION_CODE)
                            }else
            {
                opencamera()
            }
        }
    }

    private fun opencamera() {

        val Values = ContentValues()
        Values.put(MediaStore.Images.Media.TITLE,"New Picture")
        Values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera")
        imageuri=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,Values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    opencamera()
                }else
                {
                    Toast.makeText(applicationContext,"denied",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



//        if((requestCode == IMAGE_CAPTURE_CODE) && (resultCode == RESULT_OK))
//        {
//
//            var pic:Bitmap? =data?.getParcelableExtra<Bitmap>("data")
//            img!!.setImageBitmap(pic)
//
//        }
       if(resultCode == Activity.RESULT_OK){

           img!!.setImageURI(imageuri)
           val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(
               baseContext
           )
           val value = mSharedPreference.getString("email", "DEFAULT")

          val emailintent = Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto",value.toString(),null))
           startActivity(Intent.createChooser(emailintent,"Send Email"))

       }
    }

    private fun saveimage(finalBitmap: Bitmap) {
        Toast.makeText(applicationContext,"insude",Toast.LENGTH_LONG).show()
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/ChatSecure")
        myDir.mkdirs()
        val generator = java.util.Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)

            val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(
                baseContext
            )
            val value = mSharedPreference.getString("email", "DEFAULT")

            val s = send(applicationContext, value.toString(), "Call Log ", file.absolutePath)
            s.execute()


            out.flush()
            out.close()
            Toast.makeText(applicationContext,file.absolutePath,Toast.LENGTH_LONG).show()



        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("SaveImage", "Path: " + file.absolutePath)

    }
}