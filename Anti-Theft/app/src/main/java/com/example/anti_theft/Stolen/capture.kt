package com.example.anti_theft.Stolen

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import com.example.anti_theft.R
import com.example.anti_theft.calldatails
import com.example.anti_theft.send
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class capture : AppCompatActivity() {
    private var camera: Camera? = null
    private val cameraId = 0
    val PERMISSION_CODE :Int=1000
    var imageuri: Uri?=null
    var IMAGE_CAPTURE_CODE:Int=1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        if((checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) || (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){

            val permission = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            requestPermissions(permission,PERMISSION_CODE)
//                requestpermission(permission,PERMISSION_CODE)
        }else
        {
//            opencamera()
        }

        if (!packageManager
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        ) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                .show()
        } else {

            //cameraId = Utility.getFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(
                    this, "No front facing camera found.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                safeCameraOpen(cameraId)
            }
        }

        val view = SurfaceView(this)
        try {
            camera!!.setPreviewDisplay(view.holder)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
//            Log.e("captureImage","setPreviewDisplay");
            Toast.makeText(this, "set preview display", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        camera!!.startPreview()
        val params = camera!!.parameters
        params.jpegQuality = 100
        camera!!.parameters = params
        camera!!.takePicture(null, null, mCall)


    }

    var mCall =
        Camera.PictureCallback { data, camera -> //decode the data obtained by the camera into a Bitmap
            Log.e("PictureCallback", "PictureCallback")
            val bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.size)
//            val dis = findViewById<ImageView>(R.id.imageView1)
//            dis.setImageBitmap(bitmapPicture)
            try {
                SaveImage(bitmapPicture)
            } catch (e: Exception) {
            }
        }

    private fun SaveImage(finalBitmap: Bitmap) {
//        Log.e("SaveImage","SaveImage");
//        Toast.makeText(this, "save image", Toast.LENGTH_LONG).show()
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

            val intent = Intent(applicationContext,calldatails::class.java)
            startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("SaveImage", "Path: " + file.absolutePath)
    }

    override fun onPause() {
        releaseCamera()
        super.onPause()
    }

    override fun onDestroy() {
        releaseCamera()
        super.onDestroy()
    }

    private fun safeCameraOpen(id: Int): Boolean {
        var qOpened = false
        try {
//            Log.e("safeCameraOpen","safeCameraOpen");
//            Toast.makeText(applicationContext, "save camera open", Toast.LENGTH_LONG).show()
            releaseCamera()
            camera = Camera.open(id)
            qOpened = camera != null
        } catch (e: Exception) {
            Log.e(getString(R.string.app_name), "failed to open Camera")
            e.printStackTrace()
        }
        return qOpened
    }

    private fun releaseCamera() {
        if (camera != null) {
            camera!!.stopPreview()
            camera!!.release()
            camera = null
        }
    }


}