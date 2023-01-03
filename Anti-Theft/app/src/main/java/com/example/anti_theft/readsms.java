package com.example.anti_theft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anti_theft.Stolen.getlocation;

public class readsms extends AppCompatActivity {

    private TextView myTextView;

        String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readsms);


        myTextView = findViewById(R.id.textView);
        ActivityCompat.requestPermissions(readsms.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        SharedPreferences prf = getSharedPreferences("My", Context.MODE_PRIVATE);
        username = prf.getString("pin", "");


    }

    public void Read_SMS(View view){

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null,null,null);
        cursor.moveToFirst();

        myTextView.setText(cursor.getString(12));

        Intent intent = new Intent(getApplicationContext(), getlocation.class);
        startActivity(intent);

        

    }
}